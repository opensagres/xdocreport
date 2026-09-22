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
package org.apache.poi.xwpf.converter.pdf;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import com.lowagie.text.Font;
import com.lowagie.text.pdf.BaseFont;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.xwpf.converter.core.AbstractXWPFPOIConverterTest;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.junit.Test;

import fr.opensagres.poi.xwpf.converter.pdf.PdfConverter;
import fr.opensagres.poi.xwpf.converter.pdf.PdfOptions;

public class TableHeaderColorTestCase
{

    @Test
    public void tableHeaderColor()
        throws IOException
    {
        String root = "target";
        String fileOutName = root + "/tableHeaderColor.pdf";

        long startTime = System.currentTimeMillis();

        ZipSecureFile.setMinInflateRatio( 0 );
        XWPFDocument document = new XWPFDocument(
            AbstractXWPFPOIConverterTest.class.getResourceAsStream( "tableInDocx/tableHeaderColor.docx" ) );

        OutputStream out = new FileOutputStream( new File( fileOutName ) );
        PdfOptions options = PdfOptions.create();
        Map<String, BaseFont> fontMap = new HashMap<>();
        // 中文字体处理
        options.fontProvider((familyName, encoding, size, style, color) -> {
            try {
                BaseFont bfChinese = fontMap.get(familyName);
                if(bfChinese==null) {
                    if (familyName.contains("仿")) { //仿宋
                        bfChinese = BaseFont.createFont("font/simfang.ttf", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
                    } else if (familyName.contains("宋")) { //宋体
                        bfChinese = BaseFont.createFont("font/simsun.ttf", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
                    } else if (familyName.contains("楷")) { //楷体
                        bfChinese = BaseFont.createFont("font/simkai.ttf", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
                    } else { // 黑体
                        bfChinese = BaseFont.createFont("font/simhei.ttf", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
                    }
                    fontMap.put(familyName, bfChinese);
                }
                Font fontChinese = new Font(bfChinese, size, style, color);
                fontChinese.setFamily(familyName);
                return fontChinese;
            } catch (Exception e) {
                return null;
            }
        });
        PdfConverter.getInstance().convert( document, out, options );

        System.out.println( "Generate " + fileOutName + " with " + ( System.currentTimeMillis() - startTime ) + " ms." );
    }
}
