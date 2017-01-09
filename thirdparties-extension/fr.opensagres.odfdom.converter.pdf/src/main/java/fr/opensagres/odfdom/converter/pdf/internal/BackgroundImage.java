/**
 * Copyright (C) 2011-2015 The XDocReport Team <xdocreport@googlegroups.com>
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
package fr.opensagres.odfdom.converter.pdf.internal;

import java.io.ByteArrayOutputStream;

import com.lowagie.text.Image;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;

/**
 * A pojo containing all the information of a background image.
 */
public class BackgroundImage {

	private static final int DEFAULT_DPI = 72;

	private final byte[] imageBytes;
	private final Repeat repeat;
	private final Float pageWidth;
	private final Float pageHeight;
	private final Position position;
	private final Float leftMargin;
	private final Float rightMargin;
	private final Float topMargin;
	private final Float bottomMargin;

	/**
	 * A type of background image.
	 */
	public static enum Repeat {
		STRETCH("stretch"),
		NONE("no-repeat"),
		BOTH("repeat");

		private String odtValue;

		private Repeat(String odtValue) {
			this.odtValue = odtValue;
		}

		/**
		 * Returns the correct {@link Repeat} for a image.
		 * @param str the string containing the ODF repeat value.
		 * @return the correct Repeat
		 * @exception  IllegalArgumentException when the value is not defined
		 */
		public static Repeat fromODT(String str) {
			for (Repeat repeat : Repeat.values()) {
				if (repeat.odtValue.equals(str)) return repeat;
			}
			throw new IllegalArgumentException(str + " odt value not recognized.");
		}

	}

	/**
	 * The position of a background image.
	 */
	public static enum Position {
			TOP_LEFT("top left"),
			TOP_CENTER("top center"),
			TOP_RIGHT("top right"),
			CENTER_LEFT("center left"),
			CENTER_CENTER("center center", "center"),
			CENTER_RIGHT("center right"),
			BOTTOM_LEFT("bottom left"),
			BOTTOM_CENTER("bottom center"),
			BOTTOM_RIGHT("bottom right");

			private String[] odtValues;

			private Position(String... odtValues) {
				this.odtValues = odtValues;
			}

			/**
			 * Returns the correct {@link Position} for a image.
			 * @param str the string containing the ODF repeat value.
			 * @return the correct Position
			 * @exception  IllegalArgumentException when the value is not defined
			 */
			public static Position fromODT(String str) {
				for (Position position : Position.values()) {
					for (String odtValue : position.odtValues) if (odtValue.equals(str)) return position;
				}
				throw new IllegalArgumentException(str + " odt value not recognized.");
			}
	}

	/**
	 * The builder class for a {@link BackgroundImage}.
	 */
	public static class Builder {

		private final byte[] bytes;
		private Float pageWidth;
		private Float pageHeight;
		private Repeat repeat;
		private Position position;
		private Float leftMargin;
		private Float rightMargin;
		private Float topMargin;
		private Float bottomMargin;

		public Builder(byte[] image) {
			this.bytes = image;
		}

		public Builder setPageWidth(float width) {
			this.pageWidth = width;
			return this;
		}

		public Builder setPageHeight(float height) {
			this.pageHeight = height;
			return this;
		}

		public Builder setRepeat(Repeat repeat) {
			this.repeat = repeat;
			return this;
		}

		public Builder setPosition(Position position) {
			this.position = position;
			return this;
		}

		public Builder setLeftMargin(Float margin) {
			this.leftMargin = margin;
			return this;
		}

		public Builder setRightMargin(Float margin) {
			this.rightMargin = margin;
			return this;
		}

		public Builder setTopMargin(Float margin) {
			this.topMargin = margin;
			return this;
		}

		public Builder setBottomMargin(Float margin) {
			this.bottomMargin = margin;
			return this;
		}

		public BackgroundImage build() {
			return new BackgroundImage(this);
		}
	}

	private BackgroundImage(Builder builder) {
		this.imageBytes = builder.bytes;
		this.repeat = builder.repeat;
		this.pageWidth = builder.pageWidth;
		this.pageHeight = builder.pageHeight;
		this.position = builder.position;
		this.leftMargin = builder.leftMargin;
		this.rightMargin = builder.rightMargin;
		this.topMargin = builder.topMargin;
		this.bottomMargin = builder.bottomMargin;
	}

	/**
	 * Insert the backgroundImage in the given OutputStream.
	 * @param out the pdf as a ByteArrayOutputStream
	 */
	public ByteArrayOutputStream insert(ByteArrayOutputStream out) {

		try {
			Image image = Image.getInstance(imageBytes);
			float imageWidth = image.getWidth() * DEFAULT_DPI / image.getDpiX();
			float imageHeight = image.getHeight() * DEFAULT_DPI / image.getDpiY();
			switch (repeat) {
			case BOTH:
				ByteArrayOutputStream stream = out;
				//TODO: maybe we could get better results if we tiled the byteArray instead of the images themselves.
				for (float x = leftMargin; x < pageWidth - rightMargin; x += imageWidth) {
					for (float y = pageHeight - topMargin; y > bottomMargin; y -= imageHeight) {

						if (x + imageWidth > pageWidth - rightMargin || y - imageHeight < bottomMargin) {
							byte[] data = new byte[(int)imageWidth * (int)imageHeight];
							for (int k = 0; k < (int)imageHeight; k++) {
								for (int i = 0; i < imageWidth; i++) {
									if (x + i < pageWidth - rightMargin && y - k > bottomMargin) {
										data[i + k * (int)imageWidth] = (byte) 0xff;
									}
								}
							}

							Image clone = Image.getInstance(image);
							Image mask = Image.getInstance((int)imageWidth, (int)imageHeight, 1, 8, data);
							mask.makeMask();
							clone.setImageMask(mask);
							clone.setAbsolutePosition(x, y - imageHeight);
							stream = insertImage(stream, clone);
						} else {
							image.setAbsolutePosition(x, y - imageHeight);
							image.scaleAbsolute(imageWidth, imageHeight);
							stream = insertImage(stream, image);
						}
					}
				}
				return stream;
			case NONE:

				float y;
				if (position.name().split("_")[0].equals("TOP")) {
					y = pageHeight - imageHeight - topMargin;
				} else if (position.name().split("_")[0].equals("CENTER")) {
					y = (pageHeight - imageHeight - topMargin) / 2;
				} else if (position.name().split("_")[0].equals("BOTTOM")) {
					y = bottomMargin;
				} else {
					throw new UnsupportedOperationException(position + " is not supported");
				}
				float x;
				if (position.name().split("_")[1].equals("LEFT")) {
					x = leftMargin;
				} else if (position.name().split("_")[1].equals("CENTER")) {
					x = (pageWidth - imageWidth - rightMargin) / 2;
				} else if (position.name().split("_")[1].equals("RIGHT")) {
					x = pageWidth - imageWidth - rightMargin;
				} else {
					throw new UnsupportedOperationException(position + " is not supported");
				}

				image.setAbsolutePosition(x, y);
				image.scaleAbsolute(imageWidth, imageHeight);
				return insertImage(out, image);
			case STRETCH:
				image.setAbsolutePosition(leftMargin, bottomMargin);
				image.scaleAbsolute(pageWidth - leftMargin - rightMargin, pageHeight - topMargin - bottomMargin);
				return insertImage(out, image);
			default:
				throw new UnsupportedOperationException(repeat + " is not implemented");
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private ByteArrayOutputStream insertImage(ByteArrayOutputStream out, Image image) {


		try {
			ByteArrayOutputStream os = new ByteArrayOutputStream(out.size());
			PdfReader reader = new PdfReader(out.toByteArray());
			PdfStamper stamper = new PdfStamper(reader, os);

			for (int i = 1; i <= reader.getNumberOfPages(); i++) {
				PdfContentByte canvas = stamper.getUnderContent(i);
				canvas.addImage(image);
			}
			reader.close();
			stamper.close();
			return os;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
