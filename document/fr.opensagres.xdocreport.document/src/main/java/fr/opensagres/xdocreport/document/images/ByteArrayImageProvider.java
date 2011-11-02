package fr.opensagres.xdocreport.document.images;

import java.io.IOException;
import java.io.OutputStream;

import fr.opensagres.xdocreport.core.document.ImageFormat;
import fr.opensagres.xdocreport.core.io.IOUtils;

public class ByteArrayImageProvider extends AbstractImageProvider {

	private byte[] imageContent;
	private String resourceName;
	private ImageFormat imageFormat;

	public ByteArrayImageProvider(byte[] imageContent, String resourceName) {
		this(imageContent, resourceName, true);
	}

	public ByteArrayImageProvider(byte[] imageContent, String resourceName,
			boolean keepTemplateImageSize) {
		super(keepTemplateImageSize);
		setImageContent(imageContent, resourceName);
	}

	public void setImageContent(byte[] imageContent, String resourceName) {
		this.imageContent = imageContent;
		setResourceName(resourceName);
	}

	public void setImageContent(byte[] imageContent) {
		this.imageContent = imageContent;
		super.setImageInfo(null);
	}

	public byte[] getImageContent() {
		return imageContent;
	}

	public void write(OutputStream output) throws IOException {
		IOUtils.write(imageContent, output);
	}

	public ImageFormat getImageFormat() {
		return imageFormat;
	}

	public void setImageFormat(ImageFormat imageFormat) {
		this.imageFormat = imageFormat;
	}

	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
		this.imageFormat = ImageFormat.getFormatByResourceName(resourceName);
	}

	public String getResourceName() {
		return resourceName;
	}

	@Override
	protected SimpleImageInfo loadImageInfo() throws IOException {
		return new SimpleImageInfo(imageContent);
	}
}
