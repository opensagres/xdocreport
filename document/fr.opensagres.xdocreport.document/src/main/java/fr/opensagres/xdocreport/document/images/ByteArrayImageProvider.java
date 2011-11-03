package fr.opensagres.xdocreport.document.images;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import fr.opensagres.xdocreport.core.document.ImageFormat;
import fr.opensagres.xdocreport.core.io.IOUtils;

public class ByteArrayImageProvider extends AbstractImageProvider {

	private byte[] imageByteArray;

	public ByteArrayImageProvider(InputStream imageStream) throws IOException {
		this(imageStream, false);
	}

	public ByteArrayImageProvider(InputStream imageStream, boolean useImageSize)
			throws IOException {
		this(imageStream != null ? IOUtils.toByteArray(imageStream) : null,
				useImageSize);
	}

	public ByteArrayImageProvider(byte[] imageByteArray) {
		this(imageByteArray, false);
	}

	public ByteArrayImageProvider(byte[] imageByteArray, boolean useImageSize) {
		super(useImageSize);
		setImageByteArray(imageByteArray);
	}

	public void setImageStream(InputStream imageStream) throws IOException {
		setImageByteArray(IOUtils.toByteArray(imageStream));
	}

	public void setImageByteArray(byte[] imageByteArray) {
		this.imageByteArray = imageByteArray;
		super.resetImageInfo();
	}

	public InputStream getImageStream() {
		return new ByteArrayInputStream(getImageByteArray());
	}

	public byte[] getImageByteArray() {
		return imageByteArray;
	}

	public void write(OutputStream output) throws IOException {
		IOUtils.write(imageByteArray, output);
	}

	public ImageFormat getImageFormat() {
		try {
			SimpleImageInfo imageInfo = getImageInfo();
			if (imageInfo == null) {
				return null;
			}
			return imageInfo.getMimeType();
		} catch (IOException e) {
			return null;
		}
	}

	@Override
	protected SimpleImageInfo loadImageInfo() throws IOException {
		if (imageByteArray == null) {
			return null;
		}
		return new SimpleImageInfo(imageByteArray);
	}

}
