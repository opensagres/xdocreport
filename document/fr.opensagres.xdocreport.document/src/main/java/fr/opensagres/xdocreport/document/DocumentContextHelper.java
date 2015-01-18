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
package fr.opensagres.xdocreport.document;

import java.util.Map;

import fr.opensagres.xdocreport.document.images.IImageHandler;
import fr.opensagres.xdocreport.document.images.IImageRegistry;
import fr.opensagres.xdocreport.document.preprocessor.sax.BufferedElement;
import fr.opensagres.xdocreport.document.registry.TextStylingRegistry;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.TemplateContextHelper;

/**
 * Helper to register and retrieves default Java model for the {@link IContext}.
 */
public class DocumentContextHelper
{

    public static final String ELEMENTS_KEY = "___Elements";

    private static final String IMAGE_HANDLER_KEY = "___imageHandler";

    /**
     * Register the given elements cache in the given context.
     * 
     * @param context
     * @param templateEngine
     */
    public static void putElementsCache( IContext context, Map<String, BufferedElement> elementsCache )
    {
        context.put( ELEMENTS_KEY, elementsCache );
    }

    /**
     * Retrieves the elements cache from the given context.
     * 
     * @param context
     * @return
     */
    public static Map<String, BufferedElement> getElementsCache( IContext context )
    {
        return (Map<String, BufferedElement>) context.get( ELEMENTS_KEY );
    }

    /**
     * Returns the {@link BufferedElement} from the given context with the given element id.
     * 
     * @param context
     * @param elementId
     * @return
     */
    public static BufferedElement getElementById( IContext context, String elementId )
    {
        Map<String, BufferedElement> elements = getElementsCache( context );
        if ( elements != null )
        {
            return elements.get( elementId );
        }
        return null;
    }

    /**
     * Register the given text styling registry in the given context.
     * 
     * @param context
     * @param registry
     */
    public static void putTextStylingRegistry( IContext context, TextStylingRegistry registry )
    {
        context.put( TemplateContextHelper.TEXT_STYLING_REGISTRY_KEY, registry );

    }

    public static void putImageRegistry( IContext context, IImageRegistry imageRegistry )
    {
        context.put( TemplateContextHelper.IMAGE_REGISTRY_KEY, imageRegistry );

    }

    public static IImageRegistry getImageRegistry( IContext context )
    {
        return (IImageRegistry) context.get( TemplateContextHelper.IMAGE_REGISTRY_KEY );
    }

    public static void putImageHandler( IContext context, IImageHandler imageHandler )
    {
        context.put( IMAGE_HANDLER_KEY, imageHandler );

    }

    public static IImageHandler getImageHandler( IContext context )
    {
        return (IImageHandler) context.get( IMAGE_HANDLER_KEY );
    }
}
