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
package fr.opensagres.xdocreport.document.web;

import jakarta.servlet.http.HttpServletRequest;

import fr.opensagres.xdocreport.converter.IURIResolver;

public class WEBURIResolver
    implements IURIResolver, XDocProcessServletConstants
{

    private final String baseURL;

    public WEBURIResolver( String reportId, HttpServletRequest request )
    {
        baseURL = createBaseURL( reportId, request );
    }

    private static String createBaseURL( String reportId, HttpServletRequest request )
    {
        StringBuilder baseURL = new StringBuilder();
        baseURL.append( request.getContextPath() );
        baseURL.append( request.getServletPath() );

        // reportId=report ID
        baseURL.append( "?" );
        baseURL.append( REPORT_ID_HTTP_PARAM );
        baseURL.append( "=" );
        baseURL.append( reportId );

        // dispatch=download
        baseURL.append( "&" );
        baseURL.append( DISPATCH_HTTP_PARAM );
        baseURL.append( "=" );
        baseURL.append( DOWNLOAD_DISPATCH );

        return baseURL.toString();
    }

    public String resolve( String uri )
    {
        StringBuilder url = new StringBuilder( baseURL );
        url.append( "&" );
        url.append( ENTRY_NAME_HTTP_PARAM );
        url.append( "=" );
        url.append( uri );
        return url.toString();
    }

}
