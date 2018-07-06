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
package fr.opensagres.xdocreport.document.odt;

import static fr.opensagres.xdocreport.document.odt.ODTConstants.ANNOTATION_ELT;
import static fr.opensagres.xdocreport.document.odt.ODTConstants.ANNOTATION_END_ELT;
import static fr.opensagres.xdocreport.document.odt.ODTConstants.ANNOTATION_NS;
import static fr.opensagres.xdocreport.document.odt.ODTConstants.DRAW_FRAME_ELT;
import static fr.opensagres.xdocreport.document.odt.ODTConstants.DRAW_IMAGE_ELT;
import static fr.opensagres.xdocreport.document.odt.ODTConstants.DRAW_NS;
import static fr.opensagres.xdocreport.document.odt.ODTConstants.MIMETYPE;
import static fr.opensagres.xdocreport.document.odt.ODTConstants.ODT_MIMETYPE;
import static fr.opensagres.xdocreport.document.odt.ODTConstants.OFFICE_AUTOMATIC_STYLES_ELT;
import static fr.opensagres.xdocreport.document.odt.ODTConstants.OFFICE_NS;
import static fr.opensagres.xdocreport.document.odt.ODTConstants.TABLE_ELT;
import static fr.opensagres.xdocreport.document.odt.ODTConstants.TABLE_NS;
import static fr.opensagres.xdocreport.document.odt.ODTConstants.TABLE_ROW_ELT;
import static fr.opensagres.xdocreport.document.odt.ODTConstants.TEXT_A_ELT;
import static fr.opensagres.xdocreport.document.odt.ODTConstants.TEXT_INPUT_ELT;
import static fr.opensagres.xdocreport.document.odt.ODTConstants.TEXT_NS;

import java.io.IOException;

import fr.opensagres.xdocreport.core.io.IOUtils;
import fr.opensagres.xdocreport.core.io.XDocArchive;

/**
 * Utilities for Open Office ODT.
 */
public class ODTUtils
{

    /**
     * Returns true if the given document archive is a ODT and false otherwise.
     *
     * @param documentArchive
     * @return
     */
    public static boolean isODT( XDocArchive documentArchive )
    {
        try
        {
            // test if document archive has 'mimetype' entry.
            if ( !documentArchive.hasEntry( MIMETYPE ) )
            {
                return false;
            }
            // test if 'mimetype' entry contains
            // 'application/vnd.oasis.opendocument.text'
            return ODT_MIMETYPE.equals( IOUtils.toString( documentArchive.getEntryReader( MIMETYPE ) ) );
        }
        catch ( IOException e )
        {
        }
        return false;
    }

    /**
     * Returns true if element is table:table and false otherwise.
     *
     * @param uri
     * @param localName
     * @param name
     * @return
     */
    public static boolean isTable( String uri, String localName, String name )
    {
        return TABLE_NS.equals( uri ) && TABLE_ELT.equals( localName );
    }

    /**
     * Returns true if element is table:table-row and false otherwise.
     *
     * @param uri
     * @param localName
     * @param name
     * @return
     */
    public static boolean isTableRow( String uri, String localName, String name )
    {
        return TABLE_NS.equals( uri ) && TABLE_ROW_ELT.equals( localName );
    }

    /**
     * Returns true if element is text:text-input and false otherwise.
     *
     * @param uri
     * @param localName
     * @param name
     * @return
     */
    public static boolean isTextInput( String uri, String localName, String name )
    {
        return TEXT_NS.equals( uri ) && TEXT_INPUT_ELT.equals( localName );
    }

    /**
     * Returns true if element is draw:frame and false otherwise.
     *
     * @param uri
     * @param localName
     * @param name
     * @return
     */
    public static boolean isDrawFrame( String uri, String localName, String name )
    {
        return DRAW_NS.equals( uri ) && DRAW_FRAME_ELT.equals( localName );
    }

    /**
     * Returns true if element is draw:image and false otherwise.
     *
     * @param uri
     * @param localName
     * @param name
     * @return
     */
    public static boolean isDrawImage( String uri, String localName, String name )
    {
        return DRAW_NS.equals( uri ) && DRAW_IMAGE_ELT.equals( localName );
    }

    /**
     * Returns true if element is text:a and false otherwise.
     *
     * @param uri
     * @param localName
     * @param name
     * @return
     */
    public static boolean isTextA( String uri, String localName, String name )
    {
        return TEXT_NS.equals( uri ) && TEXT_A_ELT.equals( localName );
    }

    /**
     * Returns true if element is text:annotation and false otherwise.
     *
     * @param uri
     * @param localName
     * @param name
     * @return
     */
    public static boolean isAnnotation( final String uri, final String localName, final String name )
    {
        return ANNOTATION_NS.equals( uri ) && ANNOTATION_ELT.equals( localName );
    }

    /**
     * Returns true if element is text:annotation-end and false otherwise.
     *
     * @param uri
     * @param localName
     * @param name
     * @return
     */
    public static boolean isAnnotationEnd( final String uri, final String localName, final String name )
    {
        return ANNOTATION_NS.equals( uri ) && ANNOTATION_END_ELT.equals( localName );
    }

    /**
     * Returns true if element is office:automatic-styles and false otherwise.
     *
     * @param uri
     * @param localName
     * @param name
     * @return
     */
    public static boolean isOfficeAutomaticStyles( String uri, String localName, String name )
    {
        return OFFICE_NS.equals( uri ) && OFFICE_AUTOMATIC_STYLES_ELT.equals( localName );
    }
}
