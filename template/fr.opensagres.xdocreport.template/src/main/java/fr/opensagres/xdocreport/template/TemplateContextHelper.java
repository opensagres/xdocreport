/**
 * Copyright (C) 2011 Angelo Zerr <angelo.zerr@gmail.com> and Pascal Leclercq <pascal.leclercq@gmail.com>
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
package fr.opensagres.xdocreport.template;

/**
 * Helper to register and retrieves default Java model for the {@link IContext}.
 */
public class TemplateContextHelper
{

    public static final String CONTEXT_KEY = "___context";

    public final static String TEMPLATE_ENGINE_KEY = "___TemplateEngine";

    public static final String TEXT_STYLING_REGISTRY_KEY = "___TextStylingRegistry";
    
    public static final String TRANSFORM_METHOD = "transform";
    
    public static final String IMAGE_REGISTRY_KEY = "___ImageRegistry";

    /**
     * Register the given context in the given context. With that it's possible to retrieves the context in template
     * engine directive.
     * 
     * @param context
     */
    public static void putContext( IContext context )
    {
        context.put( CONTEXT_KEY, context );

    }

    /**
     * Register the given template engine in the given context.
     * 
     * @param context
     * @param templateEngine
     */
    public static void putTemplateEngine( IContext context, ITemplateEngine templateEngine )
    {
        context.put( TEMPLATE_ENGINE_KEY, templateEngine );
    }

    /**
     * Retrieves the template engine from the given context.
     * 
     * @param context
     * @return
     */
    public static ITemplateEngine getTemplateEngine( IContext context )
    {
        return (ITemplateEngine) context.get( TEMPLATE_ENGINE_KEY );
    }
    
}
