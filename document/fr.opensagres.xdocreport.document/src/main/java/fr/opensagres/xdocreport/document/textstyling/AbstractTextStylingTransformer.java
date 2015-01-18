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
package fr.opensagres.xdocreport.document.textstyling;

import java.util.logging.Level;
import java.util.logging.Logger;

import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.core.logging.LogUtils;

/**
 * Abstract class for text styling transformer.
 */
public abstract class AbstractTextStylingTransformer
    implements ITextStylingTransformer
{

    /**
     * Logger for this class
     */
    private static final Logger LOGGER = LogUtils.getLogger( AbstractTextStylingTransformer.class.getName() );

    public ITransformResult transform( String content, IDocumentHandler handler )
        throws XDocReportException
    {
        try
        {
            doTransform( content, handler );
            ITransformResult result = handler;
            if ( LOGGER.isLoggable( Level.FINE ) )
            {
                LOGGER.fine( result.toString() );
            }
            return result;
        }
        catch ( Throwable e )
        {
            if ( LOGGER.isLoggable( Level.SEVERE ) )
            {
                LOGGER.severe( e.getMessage() );
            }
            if ( e instanceof XDocReportException )
            {
                throw (XDocReportException) e;
            }
            throw new XDocReportException( e );
        }
    }

    /**
     * Transform the given content (with some syntax like HTML, Mediawiki, etc) to another syntax (docx, odt, etc). The
     * given visitor can be used to process the transformation.
     * 
     * @param content the content to transform.
     * @param documentHandler the document handler used for the transformation.
     * @return the transformed content.
     * @throws Exception
     */
    protected abstract void doTransform( String content, IDocumentHandler documentHandler )
        throws Exception;
}
