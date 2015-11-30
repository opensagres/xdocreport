package org.odftoolkit.odfdom.converter.pdf.internal;

import java.io.ByteArrayOutputStream;

import com.lowagie.text.Image;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;

/**
 * A pojo containing all the information of a background image.
 */
public class BackgroundImage {

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
	public void insert(ByteArrayOutputStream out) {

		try {
			Image image = Image.getInstance(imageBytes);

			switch (repeat) {
			case BOTH:
				//TODO: maybe we could get better results if we tiled the byteArray instead of the images themselves.
				for (float x = leftMargin; x < pageWidth - rightMargin; x += image.getWidth()) {
					for (float y = pageHeight - topMargin; y > bottomMargin; y -= image.getHeight()) {

						if (x + image.getWidth() > pageWidth - rightMargin || y - image.getHeight() < bottomMargin) {
							byte[] data = new byte[(int)image.getWidth() * (int)image.getHeight()];
							for (int k = 0; k < (int)image.getHeight(); k++) {
								for (int i = 0; i < image.getWidth(); i++) {
									if (x + i < pageWidth - rightMargin && y - k > bottomMargin) {
										data[i + k * (int)image.getWidth()] = (byte) 0xff;
									}
								}
							}

							Image clone = Image.getInstance(image);
							Image mask = Image.getInstance((int)image.getWidth(), (int)image.getHeight(), 1, 8, data);
							mask.makeMask();
							clone.setImageMask(mask);
							clone.setAbsolutePosition(x, y - image.getHeight());
							insertImage(out, clone);
						} else {
							image.setAbsolutePosition(x, y - image.getHeight());
							insertImage(out, image);
						}
					}
				}
				break;
			case NONE:

				float y;
				if (position.name().split("_")[0].equals("TOP")) {
					y = pageHeight - image.getHeight() - topMargin;
				} else if (position.name().split("_")[0].equals("CENTER")) {
					y = (pageHeight - image.getHeight() - topMargin) / 2;
				} else if (position.name().split("_")[0].equals("BOTTOM")) {
					y = bottomMargin;
				} else {
					throw new UnsupportedOperationException(position + " is not supported");
				}
				float x;
				if (position.name().split("_")[1].equals("LEFT")) {
					x = leftMargin;
				} else if (position.name().split("_")[1].equals("CENTER")) {
					x = (pageWidth - image.getWidth() - rightMargin) / 2;
				} else if (position.name().split("_")[1].equals("RIGHT")) {
					x = pageWidth - image.getWidth() - rightMargin;
				} else {
					throw new UnsupportedOperationException(position + " is not supported");
				}

				image.setAbsolutePosition(x, y);
				insertImage(out, image);
				break;
			case STRETCH:
				image.setAbsolutePosition(leftMargin, bottomMargin);
				image.scaleAbsolute(pageWidth - leftMargin - rightMargin, pageHeight - topMargin - bottomMargin);
				insertImage(out, image);
				break;
			default:
				throw new UnsupportedOperationException(repeat + " is not implemented");
			}

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private void insertImage(ByteArrayOutputStream out, Image image) {
		try {
			PdfReader reader = new PdfReader(out.toByteArray());
			PdfStamper stamper = new PdfStamper(reader, out);

			for (int i = 1; i <= reader.getNumberOfPages(); i++) {
				PdfContentByte canvas = stamper.getUnderContent(i);
				canvas.addImage(image);
			}
			stamper.close();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
