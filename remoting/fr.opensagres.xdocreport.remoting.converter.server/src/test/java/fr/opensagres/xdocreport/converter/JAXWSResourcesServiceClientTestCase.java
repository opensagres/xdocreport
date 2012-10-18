package fr.opensagres.xdocreport.converter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.jaxrs.client.JAXRSClientFactory;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import fr.opensagres.xdocreport.converter.internal.BinaryFileMessageBodyReader;
import fr.opensagres.xdocreport.converter.internal.BinaryFileMessageBodyWriter;
import fr.opensagres.xdocreport.converter.internal.ConverterResourceImpl;
import fr.opensagres.xdocreport.core.io.IOUtils;

public class JAXWSResourcesServiceClientTestCase {

	private static final int PORT = 8082;

	private static final String ROOT_ADDRESS = "http://localhost:" + PORT;

	private static final String BASE_ADDRESS = ROOT_ADDRESS;

	private String root = JAXWSResourcesServiceClientTestCase.class
			.getClassLoader().getResource(".").getFile();

	@BeforeClass
	public static void startServer() throws Exception {
		JAXRSServerFactoryBean sf = new JAXRSServerFactoryBean();
		sf.setResourceClasses(ConverterResourceImpl.class);
		sf.setProvider(new BinaryFileMessageBodyWriter());
		sf.setAddress(ROOT_ADDRESS);
		Server server = sf.create();
		System.out.println(server.getEndpoint());
	}

	@Test
	public void convertPDF_OLD() throws Exception {
		List<Object> providers = new ArrayList<Object>();
		providers.add(new BinaryFileMessageBodyReader());

		ConverterResource converterService = JAXRSClientFactory.create(
				BASE_ADDRESS, ConverterResource.class, providers);

		String fileName = "ODTCV.odt";

		FileInputStream fileInputStream = new FileInputStream(new File(root,
				fileName));
		Request request = new Request();
		request.setFilename(fileName);

		request.setContent(IOUtils.toByteArray(fileInputStream));

		BinaryFile response = converterService.convertPDF(request);
		Assert.assertEquals(fileName + ".pdf", response.getFileName());

		FileOutputStream out = new FileOutputStream(new File(root,
				response.getFileName()));

		IOUtils.copyLarge(response.getContent(), out);
		out.close();
		System.out.println(response.getContent().available());
	}

	@Test
	public void convertPDF() throws Exception {

		PostMethod post = new PostMethod("http://localhost:" + PORT + "/convert");

		String ct = "multipart/mixed";
		post.setRequestHeader("Content-Type", ct);
		Part[] parts = new Part[4];
		String fileName = "ODTCV.odt";



		parts[0] = new StringPart("outputFormat", ConverterTypeTo.PDF.name());
		parts[1] = new FilePart("datafile", new File(root,fileName),"application/vnd.oasis.opendocument.text","UTF-8");
		parts[2] = new StringPart("operation", "download");
		parts[3] = new StringPart("via", ConverterTypeVia.ITEXT.name());
		post.setRequestEntity(new MultipartRequestEntity(parts, post
				.getParams()));

		HttpClient httpclient = new HttpClient();

		try {
			int result = httpclient.executeMethod(post);
			Assert.assertEquals(200, result);
			Assert.assertEquals("attachment; filename=\"ODTCV_odt.pdf\"", post
					.getResponseHeader("Content-Disposition").getValue());

		} finally {

			post.releaseConnection();
		}
	}

}
