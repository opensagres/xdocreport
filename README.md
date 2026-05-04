# xodfreport

[![Java CI](https://github.com/SergeyLizin/xodtreport/actions/workflows/maven.yml/badge.svg)](https://github.com/SergeyLizin/xodtreport/actions/workflows/maven.yml)

Streaming ODT report generator on top of FreeMarker. A focused fork of
[XDocReport](https://github.com/opensagres/xdocreport) reduced to the
minimum needed for ODT + FreeMarker, with a streaming `process()` path
that does not allocate a full copy of the preprocessed archive on every
request.

* **Group ID:** `ru.evolenta.render.xodfreport`
* **Java:** 17+
* **Template engine:** FreeMarker only
* **Output format:** ODT (`application/vnd.oasis.opendocument.text`)
* **License:** MIT (inherited from upstream XDocReport)

## Why this fork

The original XDocReport supports DOCX, PPTX, ODT, ODS, ODP, multiple
template engines (FreeMarker, Velocity), and PDF/XHTML conversion. Its
`AbstractXDocReport.process()` always materialises a full per-request
copy of the preprocessed archive (`outputArchive = preprocessedArchive
.createCopy()`), so peak heap is roughly `2×–3×` the uncompressed ODT
size for every report generated.

This fork:

* keeps only ODT + FreeMarker;
* adds [`StreamingOdtReport`](document/ru.evolenta.render.xodfreport.document.odt/src/main/java/ru/evolenta/render/xodfreport/document/odt/StreamingOdtReport.java)
  whose `process()` pipes FreeMarker output straight into a
  `ZipOutputStream` instead of buffering an output archive;
* removes ~9 module trees (DOCX, PPTX, ODS, ODP, Velocity, all
  converters except the API, web/dump/json/sql/dispatcher infrastructure,
  OSGi bundle wiring, third-party PDF and iText integrations).

See [`ANALYSIS.md`](ANALYSIS.md) for the architectural rationale and the
post-refactor implementation status.

## Module layout

```
core/ru.evolenta.render.xodfreport.core                  – XDocArchive, IO, DocumentKind
template/ru.evolenta.render.xodfreport.template          – ITemplateEngine, FieldsMetadata, IDocumentFormatter
template/ru.evolenta.render.xodfreport.template.freemarker
                                                         – FreemarkerTemplateEngine, FreemarkerDocumentFormatter
converter/ru.evolenta.render.xodfreport.converter        – converter API only (MimeMapping, IConverter, Options)
                                                           — no concrete converters
document/ru.evolenta.render.xodfreport.document          – AbstractXDocReport, SAX preprocessor framework
document/ru.evolenta.render.xodfreport.document.odt      – ODTReport, StreamingOdtReport, ODT preprocessors
```

Six leaf Maven modules total; ~253 Java files / ~32k lines of main code.

## Quick start

Add the parent and the ODT module to your build:

```xml
<dependency>
  <groupId>ru.evolenta.render.xodfreport</groupId>
  <artifactId>ru.evolenta.render.xodfreport.document.odt</artifactId>
  <version>2.2.1-SNAPSHOT</version>
</dependency>
<dependency>
  <groupId>ru.evolenta.render.xodfreport</groupId>
  <artifactId>ru.evolenta.render.xodfreport.template.freemarker</artifactId>
  <version>2.2.1-SNAPSHOT</version>
</dependency>
```

### Authoring a template

In LibreOffice / OpenOffice Writer, write your ODT normally, then insert
FreeMarker placeholders as **Input Fields** (`Insert → Field → More
fields → Functions → Input field`, or `Ctrl+F2`):

| Field type | Example                                                      |
|------------|--------------------------------------------------------------|
| Scalar     | `${name}`, `${user.firstName}`                               |
| List loop  | `@before-row[#list developers as item_developers]` in a cell |
|            | `@after-row[/#list]` in the closing cell of the row          |

The square-bracket FreeMarker syntax (`[#list ...]`, `[#if ...]`,
`[#escape ...]`) is used because `<#list ...>` collides with XML.

### Generating a document

```java
import ru.evolenta.render.xodfreport.document.odt.StreamingOdtReport;
import ru.evolenta.render.xodfreport.document.odt.discovery.ODTTemplateEngineConfiguration;
import ru.evolenta.render.xodfreport.template.IContext;
import ru.evolenta.render.xodfreport.template.freemarker.FreemarkerTemplateEngine;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

// 1) Configure FreeMarker the same way the registry would for ODT documents:
//    - ODTTemplateEngineConfiguration: line-break / tab replacements
//    - setForceModifyReader(true): wraps each entry in [#escape any as any?xml],
//      required by the ODT styles preprocessor's [#noescape] inject
FreemarkerTemplateEngine engine = new FreemarkerTemplateEngine();
engine.setConfiguration(ODTTemplateEngineConfiguration.INSTANCE);
engine.setForceModifyReader(true);

// 2) Load template and run streaming pipeline
StreamingOdtReport report = new StreamingOdtReport();
report.setTemplateEngine(engine);
try (InputStream in = new FileInputStream("template.odt")) {
    report.load(in);
}

IContext ctx = report.createContext();
ctx.put("name", "World");

try (OutputStream out = new FileOutputStream("report.odt")) {
    report.process(ctx, out);
}
```

The preprocessed archive is built once on `load()` and cached on the
report instance. Subsequent `process()` calls reuse it and stream
FreeMarker output directly into the supplied `OutputStream` — heap stays
proportional to the largest single ZIP entry, not the whole archive.

### Limitations

* **Dynamic images** (fields registered via `FieldsMetadata.addFieldAsImage`)
  fall back transparently to the buffered `ODTReport.process()` path,
  because image post-processing requires writing new files into the
  output archive. Pure text / list templates run on the streaming path.
* **No DOCX, PPTX, ODS, ODP support.** ODT only.
* **No PDF / XHTML conversion.** Pipe the output through LibreOffice
  headless or [jodconverter](https://github.com/sbraconnier/jodconverter)
  if you need it.
* **Not published to Maven Central.** Build locally with `mvn install`.

## Build

```sh
mvn clean install
```

Builds in ~15 seconds. Runs 78 tests, all green.

The CI workflow (`.github/workflows/maven.yml`) runs the same on every
push/PR to `master` against JDK 17.

## Testing

The streaming generator is covered by
[`StreamingOdtReportTestCase`](document/ru.evolenta.render.xodfreport.document.odt/src/test/java/ru/evolenta/render/xodfreport/document/odt/StreamingOdtReportTestCase.java)
(6 tests):

| Test                                       | Verifies                                        |
|--------------------------------------------|-------------------------------------------------|
| `testOutputIsNonEmptyZip`                  | output starts with ZIP local-file signature     |
| `testMimetypeIsFirstAndStored`             | ODF spec §2.2.1 — mimetype first, STORED method |
| `testFreemarkerSubstitutionApplied`        | `${name}` is substituted in `content.xml`       |
| `testAllMandatoryEntriesPresent`           | mimetype, content.xml, styles.xml, manifest.xml |
| `testMultipleProcessCallsAreIdempotent`    | preprocessed archive is not mutated by streaming|
| `testEndToEndRealOdtTemplate`              | end-to-end on a real LibreOffice-saved ODT      |

The end-to-end test writes its output to `/tmp/streaming-odt-smoke.odt`,
which can be opened in LibreOffice for manual inspection.

## License

MIT, inherited from upstream XDocReport.

* Original copyright: The XDocReport Team
  ([opensagres/xdocreport](https://github.com/opensagres/xdocreport)),
  2011–2015.
* Modifications: Evolenta, 2026.

See the `header.txt` license header applied to every source file.
