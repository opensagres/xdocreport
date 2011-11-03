package fr.opensagres.xdocreport.document.images;

import java.io.IOException;

public abstract class AbstractImageProvider implements IImageProvider {

	private SimpleImageInfo imageInfo;
	private Float width;
	private Float height;
	private Float widthFromImageInfo;
	private Float heightFromImageInfo;

	private boolean useImageSize;
	private boolean resize;

	public AbstractImageProvider(boolean useImageSize) {
		this.useImageSize = useImageSize;
	}

	public boolean isUseImageSize() {
		return useImageSize;
	}

	public void setUseImageSize(boolean useImageSize) {
		this.useImageSize = useImageSize;
		resetImageInfo();
	}

	public Float getWidth() throws IOException {
		if (width != null) {
			return width;
		}
		if (!isUseImageSize()) {
			return null;
		}
		if (widthFromImageInfo != null) {
			return widthFromImageInfo;
		}
		// Width is null, test if height is setted and if resize flag is setted
		// to true, to compute width
		if (height != null && isResize()) {
			float ratio = new Float(getImageInfo().getWidth()).floatValue()
					/ new Float(getImageInfo().getHeight()).floatValue();
			widthFromImageInfo = new Float(Math.round(height.floatValue()
					* ratio));
			return widthFromImageInfo;
		}
		return widthFromImageInfo = new Float(getImageInfo().getWidth());
	}

	public Float getHeight() throws IOException {
		if (height != null) {
			return height;
		}
		if (!isUseImageSize()) {
			return null;
		}
		if (heightFromImageInfo != null) {
			return heightFromImageInfo;
		}
		// Height is null, test if width is setted and if resize flag is setted
		// to true, to compute height
		if (width != null && isResize()) {
			float ratio = new Float(getImageInfo().getHeight()).floatValue()
					/ new Float(getImageInfo().getWidth()).floatValue();
			heightFromImageInfo = new Float(Math.round(width.floatValue()
					* ratio));
			return heightFromImageInfo;
		}
		return heightFromImageInfo = new Float(getImageInfo().getHeight());
	}

	public void setWidth(Float width) {
		this.width = width;
		resetImageInfo();
	}

	public void setHeight(Float height) {
		this.height = height;
		resetImageInfo();
	}

	public void setSize(Float width, Float height) {
		setWidth(width);
		setHeight(height);
	}

	public SimpleImageInfo getImageInfo() throws IOException {
		if (imageInfo == null) {
			imageInfo = loadImageInfo();
		}
		return imageInfo;
	}

	public void resetImageInfo() {
		this.widthFromImageInfo = null;
		this.heightFromImageInfo = null;
		this.imageInfo = null;
	}

	public void setResize(boolean resize) {
		this.resize = resize;
		resetImageInfo();
	}

	public boolean isResize() {
		return resize;
	}

	protected abstract SimpleImageInfo loadImageInfo() throws IOException;
}
