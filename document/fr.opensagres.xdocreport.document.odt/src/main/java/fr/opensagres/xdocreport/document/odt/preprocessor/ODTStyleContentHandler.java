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
package fr.opensagres.xdocreport.document.odt.preprocessor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import fr.opensagres.xdocreport.document.odt.template.ODTContextHelper;
import fr.opensagres.xdocreport.document.odt.textstyling.IODTStylesGenerator;
import fr.opensagres.xdocreport.document.odt.textstyling.ODTDefaultStylesGenerator;
import fr.opensagres.xdocreport.document.preprocessor.sax.IBufferedRegion;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;
import fr.opensagres.xdocreport.template.formatter.IDocumentFormatter;

/**
 * Preprocess styles.xml in order to add global styles (also manages mergeFields extension).
 * 
 * @author <a href="mailto:tdelprat@nuxeo.com">Tiry</a>
 */
public class ODTStyleContentHandler
    extends ODTBufferedDocumentContentHandler
{

    private static final String STYLES_ELT = "styles";

    private static final String STYLE_ELT = "style";

    protected List<Integer> existingStyles = new ArrayList<Integer>();

    // FIXME : manage header style with styles generator comming from IContext (see ODTContextHelper).
    private final static IODTStylesGenerator styleGen = new ODTDefaultStylesGenerator();

    public ODTStyleContentHandler( String entryName, FieldsMetadata fieldsMetadata, IDocumentFormatter formatter,
                                   Map<String, Object> sharedContext )
    {

        super( entryName, fieldsMetadata, formatter, sharedContext );
    }

    @Override
    protected boolean needToProcessAutomaticStyles()
    {
        return false;
    }

    @Override
    public boolean doStartElement( String uri, String localName, String name, Attributes attributes )
        throws SAXException
    {
        if ( STYLE_ELT.equals( localName ) )
        {
            String styleName = attributes.getValue( "style:name" );
            int level = styleGen.getHeaderStyleNameLevel( styleName );
            if ( level > 0 )
            {
                existingStyles.add( level );
            }
        }
        return super.doStartElement( uri, localName, name, attributes );
    }

    @Override
    public void doEndElement( String uri, String localName, String name )
        throws SAXException
    {
        if ( STYLES_ELT.equals( localName ) )
        {
            for ( int i = 1; i <= styleGen.getHeaderStylesCount(); i++ )
            {
                if ( !existingStyles.contains( i ) )
                {
                    generateHeaderStyle( i );
                }
            }
            IDocumentFormatter formatter = super.getFormatter();
            if ( formatter != null )
            {
                IBufferedRegion region = getCurrentElement();
                region.append( formatter.getFunctionDirective( true, true, ODTContextHelper.STYLES_GENERATOR_KEY,
                                                               IODTStylesGenerator.generateAllStyles,
                                                               ODTContextHelper.DEFAULT_STYLE_KEY ) );
            }
        }
        super.doEndElement( uri, localName, name );
    }

    protected void generateHeaderStyle( int level )
    {
        IBufferedRegion region = getCurrentElement();
        region.append( styleGen.generateHeaderStyle( level ) );
    }

    @Override
    protected String getTableRowName()
    {
        return "table:table-row";
    }

    @Override
    protected String getTableCellName()
    {
        return "table:table-cell";
    }

    @Override
    protected ODTBufferedDocument createDocument()
    {
        return new ODTBufferedDocument();
    }

}
