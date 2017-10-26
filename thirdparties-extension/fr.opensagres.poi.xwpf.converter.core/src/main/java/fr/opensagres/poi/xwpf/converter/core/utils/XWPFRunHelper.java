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
package fr.opensagres.poi.xwpf.converter.core.utils;

import java.util.List;

import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFldChar;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTR;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTText;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STBrType;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STFldCharType;

/**
 * Helper for w:r run.
 */
public class XWPFRunHelper
{

    private static final String PAGE = "page";

    private static final String NUMPAGES = "numpages";
    
    private static final String PAGE_AND_SPACE = "page ";

    private static final String HYPERLINK = "HYPERLINK";

    private static final String QUOTE = "\"";

    /**
     * Returns the fldChar of the given run and null otherwise.
     * <p>
     * <w:r> <w:rPr /> <w:fldChar w:fldCharType="begin" /> </w:r>
     * </p>
     * 
     * @param r
     * @return
     */
    public static CTFldChar getFldChar( CTR r )
    {
        List<CTFldChar> fldChars = r.getFldCharList();
        if ( fldChars == null || fldChars.size() < 1 )
        {
            return null;
        }
        return fldChars.get( 0 );
    }

    /**
     * Returns the fldCharType of the given run and null otherwise.
     * <p>
     * <w:r> <w:rPr /> <w:fldChar w:fldCharType="begin" /> </w:r>
     * </p>
     * 
     * @param r
     * @return
     */
    public static STFldCharType.Enum getFldCharType( CTR r )
    {
        CTFldChar fldChar = getFldChar( r );
        if ( fldChar == null )
        {
            return null;
        }
        return fldChar.getFldCharType();
    }

    /**
     * Returns the instr text of the given run and null otherwise.
     * 
     * @param r
     * @return
     */
    public static String getInstrText( CTR r )
    {
        List<CTText> instrTextList = r.getInstrTextList();
        if ( instrTextList == null || instrTextList.size() < 1 )
        {
            return null;
        }
        if ( instrTextList.size() == 1 )
        {
            return instrTextList.get( 0 ).getStringValue();
        }

        StringBuilder instrText = new StringBuilder();
        for ( CTText ctText : instrTextList )
        {
            instrText.append( ctText.getStringValue() );
        }
        return instrText.toString();
    }

    /**
     * Returns true if the given instr is PAGE and false otherwise.
     * 
     * @param instr
     * @return
     */
    public static boolean isInstrTextPage( String instr )
    {
        if ( StringUtils.isEmpty( instr ) )
        {
            return false;
        }
        instr = instr.trim().toLowerCase();
        return instr.equals( PAGE ) || instr.startsWith( PAGE_AND_SPACE );
    }

    /**
     * Returns the hyperlink of teh given instr and null otehrwise.
     * <p>
     * <w:instrText>HYPERLINK "http://code.google.com/p/xdocreport"</w:instrText>
     * </p>
     * 
     * @param instr
     * @return
     */
    public static String getInstrTextHyperlink( String instr )
    {
        if ( StringUtils.isEmpty( instr ) )
        {
            return null;
        }
        instr = instr.trim();
        // test if it's <w:instrText>HYPERLINK "http://code.google.com/p/xdocreport"</w:instrText>
        if ( instr.startsWith( HYPERLINK ) )
        {
            instr = instr.substring( HYPERLINK.length(), instr.length() );
            instr = instr.trim();
            if ( instr.startsWith( QUOTE ) )
            {
                instr = instr.substring( 1, instr.length() );
            }
            if ( instr.endsWith( QUOTE ) )
            {
                instr = instr.substring( 0, instr.length() - 1 );
            }
            return instr;
        }
        return null;
    }

    /**
     * Returns the br type;
     * 
     * @param br
     * @return
     */
    public static STBrType.Enum getBrType( CTBr br )
    {
        if ( br != null )
        {
            STBrType.Enum brType = br.getType();
            if ( brType != null )
            {
                return brType;
            }
        }
        return STBrType.TEXT_WRAPPING;
    }
        
        /**
         * Returns true if the given instr is PAGE and false otherwise.
         * 
         * @param instr
         * @return
         */
        public static boolean isInstrTextNumpages( String instr )
        {
            if ( StringUtils.isEmpty( instr ) )
            {
                return false;
            }
            
            instr = instr.trim().toLowerCase();
            return instr.startsWith( NUMPAGES );
        }    
}
