package org.apache.poi.xwpf.converter.pdf.internal;

import org.apache.poi.xwpf.converter.core.Color;

public class Converter {

	public static java.awt.Color toAwtColor(Color fontColor) {
		if (fontColor == null) {
			return null;
		}
		return new java.awt.Color(fontColor.getRGB());
	}
}
