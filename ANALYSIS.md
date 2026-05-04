# XDocReport под микроскопом: что нужно для ODT + Freemarker

**Главный вывод:** XDocReport — это десятимодульный Maven-проект (~96% Java, порядка 1500–2000 Java-файлов), но из всей этой массы для пути «ODT + Freemarker без PDF» реально работают **5–6 модулей и около 25–40 ODT-специфичных классов**. Архитектура построена вокруг полностью буферизованного `XDocArchive` — **никакой настоящей потоковой генерации в коде нет**: и вход, и препроцессинг, и вывод проходят через несколько копий zip в памяти. Если цель — «только ODT + Freemarker + потоковая генерация», то переиспользовать у XDocReport имеет смысл идею (SAX-препроцессор, `@before-row`/`@after-row` → `[#list]`), а не код: сам ODT-слой это **примерно 2–4 тыс. строк**, плюс ~3–5 тыс. строк общего «document»-каркаса, который под потоковую модель пришлось бы переписывать почти целиком. Альтернатив с такой же связкой Java + ODT + Freemarker фактически нет — единственный исторический конкурент JODReports мёртв с 2011 года.

## 1. Размер кодовой базы и какие модули отвечают за ODT

Корневой `pom.xml` объявляет **ровно 10 агрегаторов верхнего уровня** (модуль `gae` закомментирован): `thirdparties-extension`, `core`, `tools`, `document`, `template`, `converter`, `integrationtests`, `sandbox`, `remoting`, `uberjar`. Внутри них находятся около **25–30 «листовых» Maven-модулей**. Репозиторий по статистике GitHub — **96.3% Java, 1303 коммита, 1.3k звёзд, 11 тегов релизов, MIT-лицензия**, на master сейчас версия `2.2.1-SNAPSHOT` (последний опубликованный релиз — 2.1.0 в 2025 г., с целевой Java 17). Точные `git`-метрики (`find … -name '*.java' | wc -l`) через GitHub-API в этой сессии получить не удалось — приведённые ниже LOC являются обоснованными оценками на основе исторически известных размеров JAR-артефактов и подтверждённой структуры исходников.

**Модули ODT-пути (нужны для ODT)** и оценочный объём:

| Модуль | Назначение | Java-файлы (main) | LOC main (≈) |
|--------|------------|-------------------|--------------|
| `core/fr.opensagres.xdocreport.core` | `XDocArchive`, IO, `DocumentKind`, исключения | 30–50 | 2 000–3 500 |
| `document/fr.opensagres.xdocreport.document` | Абстрактный `AbstractXDocReport`, SAX-фреймворк препроцессоров (`BufferedDocument`, `TableRowBufferedRegion`, `TransformedBufferedDocumentContentHandler`), реестры | 40–60 | 3 500–5 000 |
| `document/fr.opensagres.xdocreport.document.odt` | **Только ODT**: `ODTReport`, `ODTPreprocessor`, `ODTBufferedDocument`, `ODTBufferedDocumentContentHandler`, `ODTStylesPreprocessor`, `ODTManifestXMLProcessor`, `ODTAnnotationParsingHelper`, `ODTImageRegistry`, text-styling | 25–40 | 2 000–3 500 |
| `template/fr.opensagres.xdocreport.template` | Абстрактный `ITemplateEngine`, `IContext`, `FieldsMetadata`, `IDocumentFormatter` | 30–45 | 2 000–3 000 |
| `template/fr.opensagres.xdocreport.template.freemarker` | `FreemarkerTemplateEngine`, `FreemarkerDocumentFormatter`, `XDocFreemarkerContext` | 10–20 | 700–1 500 |
| `converter/fr.opensagres.xdocreport.converter` | API конвертеров (нужен только при PDF/XHTML) | 10–20 | 500–1 200 |
| `converter/fr.opensagres.xdocreport.converter.odt.odfdom` | **ODT-специфика для конвертации** (тонкая обёртка) | 5–15 | 300–800 |
| `thirdparties-extension/fr.opensagres.odfdom.converter.{core,pdf,xhtml}` | Реальные конвертеры ODT→PDF/XHTML на iText/ODFDOM | 130–250 | **15 000–25 000** |
| `thirdparties-extension/fr.opensagres.xdocreport.itext.extension` | Низкоуровневые расширения iText | 50–100 | 6 000–12 000 |

**Прямо ODT-специфичный код — это `document/fr.opensagres.xdocreport.document.odt` плюс `converter/.../converter.odt.odfdom`** (если нужен PDF — добавляются `odfdom.converter.*`). Остальное либо общее (используется и для DOCX), либо относится к другим форматам и удаляется.

**Удаляется при ограничении ODT-only:** `document/fr.opensagres.xdocreport.document.docx` (~80–120 файлов, 10–15 тыс. LOC — самый тяжёлый «документный» модуль), `document/fr.opensagres.xdocreport.document.pptx` (~25–40 файлов, ~2–3.5 тыс. LOC), `document/fr.opensagres.xdocreport.document.ods` (если ODS не нужен), все `converter.docx.*` и `thirdparties-extension/fr.opensagres.poi.xwpf.converter.*` (DOCX→PDF через POI+iText, десятки тысяч LOC), а также `template/.../template.velocity` (10–20 файлов). Опционально удаляются `remoting`, `sandbox`, `tools`, `integrationtests`, `uberjar`. **В сумме это легко более половины общей кодовой базы.**

## 2. Что XDocReport делает при генерации ODT

**Загрузка шаблона.** Точка входа — `XDocReportRegistry.getRegistry().loadReport(InputStream, TemplateEngineKind.Freemarker)`. По mimetype/расширению создаётся `fr.opensagres.xdocreport.document.odt.ODTReport extends AbstractXDocReport`. В `AbstractXDocReport.load(InputStream)` вызывается `setDocumentArchive(XDocArchive.readZip(sourceStream))` — **весь .odt zip распаковывается в `XDocArchive`, который по сути является `Map<String, byte[]>` всех записей**. Лениво/потоково ничего не читается. Сразу после этого выполняется `doPreprocessorIfNeeded()`, который прогоняет зарегистрированные препроцессоры по записям `content.xml`, `styles.xml`, `META-INF/manifest.xml` и сохраняет результат в **отдельный** `preprocessedArchive` (плюс может остаться `originalArchive`, если включён `cacheOriginalDocument`).

**Конструктор `ODTReport.registerPreprocessors()`** регистрирует три препроцессора: `ODTPreprocessor.INSTANCE` на `content.xml`, `ODTStylesPreprocessor.INSTANCE` на `styles.xml`, `ODTManifestXMLProcessor.INSTANCE` на `META-INF/manifest.xml`. Все три наследуют `SAXXDocPreprocessor` из общего модуля `document` — **парсинг XML в XDocReport основан на SAX**, не DOM и не regex.

**Обработка `@before-row` / `@after-row`** идёт через **«буферизованный SAX»**: `ODTPreprocessor` запускает `ODTBufferedDocumentContentHandler` (сам extends `TransformedBufferedDocumentContentHandler<ODTBufferedDocument>`). При SAX-обходе элемент `<table:table-row>` оборачивается в `TableRowBufferedRegion`; `<text:text-input>` и текстовые узлы внутри ячеек буферизуются. Когда строка завершается, обработчик сканирует текст ячеек на токены (по умолчанию — литералы `@before-row` и `@after-row`, настраиваются через `FieldsMetadata.setBeforeRowToken/setAfterRowToken`). Найденные директивы через `setStartLoopDirective(...)`/`setEndLoopDirective(...)` **выводятся непосредственно перед `<table:table-row>` и после `</table:table-row>`**, а сам токен и обёртка `text:text-input` из вывода удаляются. Когда же пользователь не пишет скрипт, а только декларирует список через `FieldsMetadata.addFieldAsList("developers.name")`, ту же обёртку строит сам `IDocumentFormatter` — **«ленивая» автогенерация цикла**.

**Интеграция с Freemarker.** `FreemarkerDocumentFormatter` (реализация `IDocumentFormatter`) специально эмитит **квадратно-скобочный синтаксис Freemarker `[#list developers as item_developers] … [/#list]`**, потому что `<#list…>` коллидирует с XML. Внутри цикла он переписывает поля типа `${developers.name}` в `${item_developers.name}`. На стороне выполнения `FreemarkerTemplateEngine` extends `AbstractTemplateEngine` и держит статический `freemarker.template.Configuration` (с лениво создаваемым `MultiTemplateLoader` из `XDocReportEntryTemplateLoader`-ов для in-memory архива и недавно добавленным `NEW_BUILTIN_CLASS_RESOLVER_KEY = "safer"` для защиты от FreeMarker SSTI). На каждый из `getDefaultXMLEntries()` (`content.xml`, `styles.xml`, `META-INF/manifest.xml`) вызывается `template.process(model, writer)`, где модель — `XDocFreemarkerContext`-обёртка над пользовательским контекстом. Плейсхолдеры `${name}` пользователь пишет прямо в OOo Writer (через **Input Field**, Ctrl+F2), и они передаются в content.xml в неизменном виде — как готовый Freemarker-синтаксис.

**Ключевые классы ODT-пути с полными пакетами:**

- Документная модель: `IXDocReport`, `AbstractXDocReport`, `ProcessState`, `IXDocPreprocessor`, `SAXXDocPreprocessor`, `BufferedDocument`, `BufferedElement`, `TableRowBufferedRegion`, `TransformedBufferedDocumentContentHandler` (все в `fr.opensagres.xdocreport.document.*`).
- ODT-специфика: `ODTReport`, `ODTConstants`, `ODTPreprocessor`, `ODTBufferedDocument`, `ODTBufferedDocumentContentHandler`, `ODTAnnotationParsingHelper`, `ODTStylesPreprocessor`, `ODTManifestXMLProcessor`, `ODTImageRegistry`, `ODTContextHelper`, `ODTDefaultStyle`.
- IO: `XDocArchive`, `IEntryReaderProvider`/`IEntryWriterProvider`/`IEntryOutputStreamProvider` (`fr.opensagres.xdocreport.core.io.*`).
- Template-API: `ITemplateEngine`, `AbstractTemplateEngine`, `IContext`, `TemplateEngineKind`, `FieldsMetadata`, `IDocumentFormatter`, `TemplateEngineInitializerRegistry`.
- Freemarker: `FreemarkerTemplateEngine`, `FreemarkerDocumentFormatter`, `FreemarkerConstants`, `XDocFreemarkerContext`, SPI `FreemarkerTemplateEngineInitializerDiscovery`.
- Реестры: `XDocReportRegistry`, `XDocReportFactoryDiscovery`.

**Поток выполнения `process(context, out)`** идёт строго: `internalGetTemplateEngine()` → `doPreprocessorIfNeeded()` → **`outputArchive = preprocessedArchive.createCopy()`** (третья копия архива в памяти) → `onBeforeProcessTemplateEngine` (регистрирует `ODTDefaultStyle`, image registry, кладёт `templateEngine`/`elementsCache` в FreeMarker-контекст) → цикл `templateEngine.process(...)` по `content.xml`/`styles.xml`/`manifest.xml` (FreeMarker читает запись как `Reader`, пишет обратно через `Writer` в ту же in-memory запись) → `onAfterProcessTemplateEngine` (постпроцессинг изображений) → `doPostprocessIfNeeded` → **`XDocArchive.writeZip(outputArchive, out)`** одним проходом в самом конце.

**Никакой потоковости тут нет.** Память масштабируется как **2×–3× размера распакованного .odt**: `originalArchive` (опционально) + `preprocessedArchive` + `outputArchive` + промежуточные `byte[]` каждой записи в FreeMarker. Для шаблонов с большими таблицами (десятки тысяч строк) это реальная боль — XDocReport архитектурно построен на «загрузить-преобразовать-склеить», а не на pipe.

## 3. Что нужно переписать для «только ODT + Freemarker + потоковая генерация»

Если требования — **`только ODT + Freemarker + настоящий streaming`**, то реалистичная оценка такая:

**Можно взять почти как есть** (~1 500–3 000 LOC): `template/fr.opensagres.xdocreport.template.freemarker` (700–1500 LOC) и часть `template/fr.opensagres.xdocreport.template` — `ITemplateEngine`/`IContext`/`FieldsMetadata`/`IDocumentFormatter` (берём ~30–50% из 2–3 тыс. LOC). Эти слои не привязаны к буферной модели и уже почти потоковые: FreeMarker сам по себе читает `Reader` и пишет в `Writer`.

**Нужно переписать полностью или почти полностью** (~5 000–8 000 LOC):

- `core/fr.opensagres.xdocreport.core` (`XDocArchive` и спутники, ~2–3.5 тыс. LOC) — это и есть «всё в память» антипаттерн. Под streaming нужен другой примитив: оборачивать `ZipInputStream`/`ZipOutputStream` напрямую и обрабатывать запись `content.xml` поэлементно через `XMLEventReader`/StAX или SAX→`XMLStreamWriter`, не материализуя `byte[]`.
- `document/fr.opensagres.xdocreport.document` — абстрактный каркас (`AbstractXDocReport`, `ProcessState`, `BufferedDocument`, `BufferedElement`, `TableRowBufferedRegion`, `TransformedBufferedDocumentContentHandler`). Это ~3–5 тыс. LOC, и именно эта «буферная сборка дерева» противоречит потоковой модели. Идею «при `endElement` строки решаем — обернуть ли её директивой» можно сохранить, но реализацию надо писать поверх StAX, эмитя XML-события напрямую и удерживая в памяти максимум одну строку таблицы.
- `document/fr.opensagres.xdocreport.document.odt` (~2–3.5 тыс. LOC) — `ODTReport`, три препроцессора, `ODTBufferedDocument*`, image registry. Логику `@before-row/@after-row` (в рамках одной строки) и обработку `<text:text-input>`-полей переиспользовать реалистично, но сами классы плотно сидят на старом каркасе и потребуют почти полной перезаписи.

**Можно выкинуть целиком** (десятки тысяч LOC): все `document.docx*`, `document.pptx`, `template.velocity`, `converter.docx.*`, `fr.opensagres.poi.xwpf.converter.*`, `remoting`, `sandbox`, `gae`, `uberjar`, `tools` и (если PDF не нужен) всю ветку `converter.*` + `odfdom.converter.*` + `itext.extension` (это обычно крупнейший кусок ODT-стороны, ~15–25 тыс. LOC).

**Итого для «ODT + Freemarker + streaming с нуля»:** ядро для перезаписи — **~5–8 тыс. строк нового Java-кода** (потоковый zip-IO + StAX-препроцессор + интеграция с FreeMarker по `Reader`/`Writer` каждой записи). Это объективно меньше, чем поддерживать форк XDocReport, потому что вы избавляетесь от параллельной поддержки DOCX, Velocity, OSGi-обвязки и ~80% «общего» каркаса, который существует именно ради мультиформатности.

## 4. Альтернативы для ODT + Freemarker

**Прямого активного аналога в Java не существует.** Из практически реальных опций:

- **JODReports** (Java, ODT-only, **Freemarker**): идейный предшественник XDocReport, последний релиз 2.4.0 от **28 января 2011 г.**, форки есть, но ни один не активен. Технически — единственный, кроме XDocReport, кто когда-либо склеивал Java + ODT + Freemarker. Использовать в продакшне в 2026 нельзя.
- **ODF Toolkit (TDF/Apache, ODFDOM)** — Apache 2.0, активен (0.12.0 — декабрь 2023, 0.13.0 в работе под TDF), Java 11+. Но это **низкоуровневая DOM-библиотека без шаблонизатора**: Freemarker-слой над ней пришлось бы писать самостоятельно (это ровно то, что уже делает XDocReport).
- **JOpenDocument** (Java, ODT) — GPL/коммерческий, последний релиз 1.5 ~2021. Свой синтаксис полей и `<jod:forEach>`, **Freemarker не поддерживает**.
- **mz-office-document-api** (Java, ODT+DOCX) — простые `MERGEFIELD`-плейсхолдеры, не Freemarker, малая активность.
- **docx-stamper, docx4j** — DOCX-only, не подходят.
- **Relatorio** (Python, Tryton) — активен, **0.12.0 от 21 марта 2026**, но Genshi-синтаксис и Python; Java-порта нет. Из всех альтернатив самый близкий к настоящему streaming в zip (использует ZipFile с уровнями сжатия и ZIP64 для >2 ГБ).
- **py3o.template** (Python) — активен в форке OCA, Genshi-производный синтаксис, Python-only.
- **LOTemplate (Probesys)** — Python + headless LibreOffice через UNO. Java-нет.
- **Carbone** (Node.js core, Java SDK = REST-клиент) — активен, mustache-синтаксис, требует поднятого сервиса; не in-process Java.
- **jodconverter** — только конвертация (LO в headless), без шаблонизации.

**Практический вывод:** если стек обязан остаться Java + Freemarker + ODT — **выбора между XDocReport и «свой код поверх ODFDOM» нет, и оба варианта сводятся к примерно той же работе**, что описана в разделе 3. Если же Freemarker не догма, релевантной альтернативой 2026 года для ODT-генерации в Java является **связка ODF Toolkit + любой шаблонизатор по вашему выбору**, либо переход на сторонний сервис (Relatorio в Python-микросервисе или Carbone-сервер).

## Итог: цифры одной таблицей

| Параметр | Значение |
|----------|----------|
| Maven-агрегаторов верхнего уровня | 10 (плюс закомментированный `gae`) |
| «Листовых» Maven-модулей | ~25–30 |
| Java в репозитории | 96.3% языкового состава, ~150–250 тыс. LOC, ~1 500–2 500 файлов (оценка) |
| ODT-специфичных модулей | 2 для генерации (`document.odt`, `converter.odt.odfdom`) + 3 «толстых» для PDF/XHTML (`odfdom.converter.{core,pdf,xhtml}`) |
| ODT-специфичный код для генерации (без PDF) | ~25–40 классов, ~2 000–3 500 LOC |
| Минимальный «нужный» Java-стек для ODT+Freemarker без PDF | core + document + document.odt + template + template.freemarker = ~115–215 файлов, ~10–15 тыс. LOC |
| Размер DOCX-веток к удалению | ~120–160 файлов, **>20 000 LOC** только в `document.docx` + `converter.docx.*` |
| Реалистичный объём перезаписи под streaming | ~5 000–8 000 строк нового Java-кода |
| Альтернативы Java + ODT + Freemarker | **только XDocReport** (JODReports заброшен с 2011) |

**Заключение.** XDocReport — единственная живая Java-библиотека на стыке ODT и Freemarker, но её внутренности оптимизированы под мультиформатность и in-memory обработку, а не под поток. Если ваша цель — узкий ODT-only с настоящим streaming, выгоднее не форкать XDocReport, а переиспользовать только Freemarker-формирователь (`FreemarkerDocumentFormatter` и квадратно-скобочный синтаксис) и идею SAX-распознавания `@before-row`/`@after-row`, переписав ядро архива и препроцессор поверх StAX и `ZipInputStream`/`ZipOutputStream`. Объём работы — порядка нескольких тысяч строк, что меньше, чем стоимость поддержки форка с вырезанием DOCX/PPTX/Velocity. Альтернативы вне Java (Relatorio, Carbone) реальны только при готовности уйти от Freemarker и in-process модели.
