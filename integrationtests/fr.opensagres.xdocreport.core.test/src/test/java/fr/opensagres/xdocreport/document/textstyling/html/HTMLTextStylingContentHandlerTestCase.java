package fr.opensagres.xdocreport.document.textstyling.html;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.logging.Logger;

import fr.opensagres.xdocreport.core.document.SyntaxKind;
import fr.opensagres.xdocreport.core.logging.LogUtils;
import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.registry.XDocReportRegistry;
import fr.opensagres.xdocreport.template.FieldExtractor;
import fr.opensagres.xdocreport.template.FieldsExtractor;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.TemplateEngineKind;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;
import junit.framework.TestCase;

public class HTMLTextStylingContentHandlerTestCase extends TestCase {
	private static final Logger LOGGER = LogUtils.getLogger(HTMLTextStylingContentHandlerTestCase.class.getName());

	public static final String HTML_WITH_TH = "<h2>Análise de Sentimento das Respostas:</h2>\n"
			+ "<p><strong>1. Identificação de Palavras Mais Recorrentes:</strong></p>\n" + "<p>As palavras mais recorrentes nas respostas são:</p>\n"
			+ "<ul>\n"
			+ "<li><strong>&quot;Nada a declarar&quot;</strong>:  Apresenta-se como um padrão, indicando a ausência de algo a ser dito.</li>\n"
			+ "<li><strong>&quot;Teste com o Welkey que...&quot;</strong>:  Indica uma tentativa de iniciar ou completar a tarefa.</li>\n"
			+ "<li><strong>&quot;Parece que tudo está bem&quot;</strong>:  Expressa uma sensação geral positiva.</li>\n" + "</ul>\n"
			+ "<p><strong>2. Classificação das Respostas:</strong></p>\n" + "<table>\n" + "<thead>\n" + "<tr>\n" + "<th>Classificação</th>\n"
			+ "<th>Número de Respostas</th>\n" + "<th>Porcentagem (%)</th>\n" + "</tr>\n" + "</thead>\n" + "<tbody>\n" + "<tr>\n"
			+ "<td>Positiva</td>\n" + "<td>10</td>\n" + "<td>50%</td>\n" + "</tr>\n" + "<tr>\n" + "<td>Negativa</td>\n" + "<td>6</td>\n"
			+ "<td>30%</td>\n" + "</tr>\n" + "<tr>\n" + "<td>Neutra</td>\n" + "<td>4</td>\n" + "<td>20%</td>\n" + "</tr>\n" + "</tbody>\n"
			+ "</table>\n" + "<p><strong>3. Cálculo dos Percentuais:</strong></p>\n" + "<ul>\n"
			+ "<li><strong>Positiva:</strong>  50% das respostas expressam uma sensação positiva, com foco na ausência de algo a declarar e na sensação geral de bem-estar.</li>\n"
			+ "<li><strong>Negativa:</strong> 30% das respostas apresentam um tom negativo, com foco em falhas ou limitações.</li>\n"
			+ "<li><strong>Neutra:</strong> 20% das respostas são neutras, sem uma predominância clara de termos positivos ou negativos.</li>\n"
			+ "</ul>\n" + "<p><strong>4. Apresentação dos Percentuais:</strong></p>\n" + "<ul>\n" + "<li><strong>Positiva: 50%</strong></li>\n"
			+ "<li><strong>Negativa: 30%</strong></li>\n" + "<li><strong>Neutra: 20%</strong></li>\n" + "</ul>\n"
			+ "<p><strong>Observações:</strong></p>\n"
			+ "<p>A análise do sentimento das respostas mostra uma tendência predominante de neutra, com a ausência de algo a declarar e a sensação geral de bem-estar. A presença de palavras como &quot;Teste com o Welkey que...&quot; sugere um contexto de tarefa ou atividade específica.</p>\n"
			+ "";

	/**
	 * Test for issue 701 - https://github.com/opensagres/xdocreport/issues/701
	 * 
	 * @throws Exception
	 */
	public void testfieldWithHtmlTagTh() throws Exception {
		try (var inputStream = HTMLTextStylingContentHandlerTestCase.class.getResourceAsStream("template_th.odt")) {

			IXDocReport report = XDocReportRegistry.getRegistry().loadReport(inputStream, TemplateEngineKind.Freemarker);

			inputStream.close();
			FieldsMetadata fieldsMetadata = report.createFieldsMetadata();
			String fieldName = "html";
			fieldsMetadata.addFieldAsTextStyling(fieldName, SyntaxKind.Html, true);

			IContext context = report.createContext();
			context.put(fieldName, HTML_WITH_TH);

			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			report.process(context, bos);
			Path outputPath = Path.of(System.getProperty("java.io.tmpdir"), "newTest-" + System.currentTimeMillis() + ".odt");
			Files.write(outputPath, bos.toByteArray(), StandardOpenOption.CREATE_NEW);

			// Try to open generated ODT
			try {
				var result = new FileInputStream(outputPath.toFile());
				var resultReport = XDocReportRegistry.getRegistry().loadReport(result, TemplateEngineKind.Freemarker);
				resultReport.extractFields(new FieldsExtractor<FieldExtractor>());
			} catch (Exception ex) {
				fail("The generated document should be parseable: " + ex.getMessage());
			}
			LOGGER.fine("Output file saved on " + outputPath.toAbsolutePath().toString());

		}
	}
}
