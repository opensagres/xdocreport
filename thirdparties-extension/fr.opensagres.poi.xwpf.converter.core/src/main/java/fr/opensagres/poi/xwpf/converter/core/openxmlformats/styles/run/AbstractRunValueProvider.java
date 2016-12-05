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
package fr.opensagres.poi.xwpf.converter.core.openxmlformats.styles.run;

import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocDefaults;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTP;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTParaRPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTR;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPrDefault;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTStyle;

import fr.opensagres.poi.xwpf.converter.core.styles.XWPFStylesDocument;

public abstract class AbstractRunValueProvider<Value>
{

    public Value getValue( CTR run, CTP paragraph, XWPFStylesDocument document )
    {
        Value value = null;
        // from run
        CTRPr rPr = run.getRPr();
        if ( rPr != null )
        {
            // from run inline
            value = getValue( rPr, document );
            if ( value != null )
            {
                return value;
            }
            // from run style
            value = getValueFromStyle( rPr.getRStyle(), document );
            if ( value != null )
            {
                return value;
            }
            // from run default style
            value = getValueFromStyle( document.getDefaultCharacterStyle(), document );
            if ( value != null )
            {
                return value;
            }
        }
        // from paragraph
        CTPPr pPr = paragraph.getPPr();
        if ( pPr != null )
        {
            // from paragraph inline
            value = getValue( pPr.getRPr(), document );
            if ( value != null )
            {
                return value;
            }
            // from paragraph style
            value = getValueFromStyle( pPr.getPStyle(), document );
            if ( value != null )
            {
                return value;
            }
            // from paragraph default style
            value = getValueFromStyle( document.getDefaultParagraphStyle(), document );
            if ( value != null )
            {
                return value;
            }
        }
        // from defaults docs paragraph
        CTDocDefaults docDefaults = document.getDocDefaults();
        if ( docDefaults == null )
        {
            return null;
        }
        value = getValueFromDocDefaultsStyle( docDefaults, document );
        if ( value != null )
        {
            return value;
        }
        return null;
    }

    private Value getValueFromStyle( CTString styleId, XWPFStylesDocument document )
    {
        // Get the style
        CTStyle style = document.getStyle( styleId );
        return getValueFromStyle( style, document );
    }

    private Value getValueFromStyle( CTStyle style, XWPFStylesDocument document )
    {
        if ( style != null )
        {
            Value value = getValue( style.getRPr(), document );
            if ( value != null )
            {
                return value;
            }
            return getValueFromStyle( style.getBasedOn(), document );
        }
        return null;
    }

    private Value getValueFromDocDefaultsStyle( CTDocDefaults docDefaults, XWPFStylesDocument document )
    {
        return getValue( getCTRPr( docDefaults ), document );
    }

    public CTRPr getCTRPr( CTDocDefaults docDefaults )
    {
        CTRPrDefault prDefault = docDefaults.getRPrDefault();
        if ( prDefault == null )
        {
            return null;
        }
        return prDefault.getRPr();
    }

    public abstract Value getValue( CTRPr rPr, XWPFStylesDocument stylesDocument );

    public abstract Value getValue( CTParaRPr rPr, XWPFStylesDocument stylesDocument );

}
