package fr.opensagres.xdocreport.document.images;

import java.io.IOException;

public abstract class AbstractImageProvider implements IImageProvider {

	private SimpleImageInfo imageInfo;
	private Float width;
	private Float height;

	private boolean keepTemplateImageSize;

	public AbstractImageProvider(boolean keepTemplateImageSize) {
		this.keepTemplateImageSize = keepTemplateImageSize;
	}

	public boolean isKeepTemplateImageSize() {
		return keepTemplateImageSize;
	}

	public void setKeepTemplateImageSize(boolean keepTemplateImageSize) {
		this.keepTemplateImageSize = keepTemplateImageSize;
		setImageInfo(null);
	}

	public Float getWidth() throws IOException {
		if (width != null) {
			return width;
		}
		if (isKeepTemplateImageSize()) {
			return null;
		}
		return width = new Float(getImageInfo().getWidth());
	}

	public Float getHeight() throws IOException {
		if (height != null) {
			return height;
		}
		if (isKeepTemplateImageSize()) {
			return null;
		}
		return height = new Float(getImageInfo().getHeight());
	}

	public void setWidth(Float width) {
		this.width = width;
	}

	public void setHeight(Float height) {
		this.height = height;
	}

	public SimpleImageInfo getImageInfo() throws IOException {
		if (imageInfo == null) {
			imageInfo = loadImageInfo();
		}
		return imageInfo;
	}

	public void setImageInfo(SimpleImageInfo imageInfo) {
		this.imageInfo = imageInfo;
	}

	protected abstract SimpleImageInfo loadImageInfo() throws IOException;
}
