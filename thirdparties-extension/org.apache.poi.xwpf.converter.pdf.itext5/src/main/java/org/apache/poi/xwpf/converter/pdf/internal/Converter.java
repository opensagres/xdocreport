package org.apache.poi.xwpf.converter.pdf.internal;

import org.apache.poi.xwpf.converter.core.Color;

import com.itextpdf.text.BaseColor;

public class Converter {

	public static BaseColor toBaseColor(Color color) {
    	if(color == null){
    		return null;
    	}
		return new BaseColor(color.getRGB());
	}
}
