/**
 * Copyright (C) 2011 Angelo Zerr <angelo.zerr@gmail.com> and Pascal Leclercq <pascal.leclercq@gmail.com>
 *
 * All rights reserved.
 *
 * Permission is hereby granted, free  of charge, to any person obtaining
 * a  copy  of this  software  and  associated  documentation files  (the
 * "Software"), to  deal in  the Software without  restriction, including
 * without limitation  the rights to  use, copy, modify,  merge, publish,
 * distribute,  sublicense, and/or sell  copies of  the Software,  and to
 * permit persons to whom the Software  is furnished to do so, subject to
 * the following conditions:
 *
 * The  above  copyright  notice  and  this permission  notice  shall  be
 * included in all copies or substantial portions of the Software.
 *
 * THE  SOFTWARE IS  PROVIDED  "AS  IS", WITHOUT  WARRANTY  OF ANY  KIND,
 * EXPRESS OR  IMPLIED, INCLUDING  BUT NOT LIMITED  TO THE  WARRANTIES OF
 * MERCHANTABILITY,    FITNESS    FOR    A   PARTICULAR    PURPOSE    AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE,  ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package fr.opensagres.xdocreport.document.images;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.core.io.IEntryOutputStreamProvider;
import fr.opensagres.xdocreport.core.io.IEntryReaderProvider;
import fr.opensagres.xdocreport.core.io.IEntryWriterProvider;
import fr.opensagres.xdocreport.core.io.IOUtils;

/**
 * 
 * Abstract class for {@link IImageRegistry}.
 * 
 */
public abstract class AbstractImageRegistry implements IImageRegistry {

	private static final String XDOCREPORT_PREFIX = "xdocreport_";

	private List<ImageProviderInfo> imageProviderInfos;

	protected final IEntryReaderProvider readerProvider;
	protected final IEntryWriterProvider writerProvider;
	protected final IEntryOutputStreamProvider outputStreamProvider;

	public AbstractImageRegistry(IEntryReaderProvider readerProvider,
			IEntryWriterProvider writerProvider,
			IEntryOutputStreamProvider outputStreamProvider) {
		this.readerProvider = readerProvider;
		this.writerProvider = writerProvider;
		this.outputStreamProvider = outputStreamProvider;
	}

	public String registerImage(IImageProvider imageProvider)
			throws XDocReportException {
		if (imageProvider == null) {
			throw new XDocReportException("Image provider cannot be null!");
		}
		ImageProviderInfo info = createImageProviderInfo(imageProvider);
		getImageProviderInfos().add(info);
		return getPath(info);
	}

	public List<ImageProviderInfo> getImageProviderInfos() {
		if (imageProviderInfos == null) {
			imageProviderInfos = new ArrayList<ImageProviderInfo>();
		}
		return imageProviderInfos;
	}

	protected ImageProviderInfo createImageProviderInfo(
			IImageProvider imageProvider) {
		String imageId = getImageId();
		String imageBasePath = getImageBasePath();
		String imageFileName = imageId + "." + imageProvider.getImageFormat();
		return new ImageProviderInfo(imageProvider, imageId, imageBasePath,
				imageFileName);
	}

	protected String getImageId() {
		return XDOCREPORT_PREFIX + getImageProviderInfos().size();
	}

	public void preProcess() throws XDocReportException {
		// Do nothing
	}

	public void postProcess() throws XDocReportException {
		if (imageProviderInfos != null) {
			// There are dynamic images, images binary must be stored in the
			// document
			// archive and some XML entries must be modified.
			// 1) Save binary images
			saveBinaryImages();
			
			// 3) dispose
			imageProviderInfos.clear();
			imageProviderInfos = null;
		}
	}

	protected void saveBinaryImages() throws XDocReportException {
		for (ImageProviderInfo imageProviderInfo : imageProviderInfos) {
			saveBinaryImage(imageProviderInfo);
		}
	}
	

	protected void saveBinaryImage(ImageProviderInfo imageProviderInfo)
			throws XDocReportException {
		String entryName = getImageEntryName(imageProviderInfo);
		OutputStream out = outputStreamProvider.getEntryOutputStream(entryName);
		try {
			imageProviderInfo.getImageProvider().write(out);
		} catch (IOException e) {
			throw new XDocReportException(e);
		} finally {
			IOUtils.closeQuietly(out);
		}
	}
	
	protected String getImageEntryName(ImageProviderInfo imageProviderInfo) {
		return imageProviderInfo.getImageBasePath()
				+ imageProviderInfo.getImageFileName();
	}

	protected abstract String getImageBasePath();

	protected abstract String getPath(ImageProviderInfo info);
	
}
