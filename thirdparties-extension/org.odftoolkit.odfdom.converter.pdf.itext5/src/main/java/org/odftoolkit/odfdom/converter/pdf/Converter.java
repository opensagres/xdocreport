package org.odftoolkit.odfdom.converter.pdf;

import org.odftoolkit.odfdom.converter.core.Color;

import com.itextpdf.text.BaseColor;

public class Converter {

	public static BaseColor toBaseColor(Color color) {
    	if(color==null)
    		return null;
    	return new BaseColor(color.getRGB());
	}
}
