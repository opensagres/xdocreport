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
package fr.opensagres.xdocreport.document.pptx;

import static fr.opensagres.xdocreport.document.pptx.PPTXConstants.A_NS;
import static fr.opensagres.xdocreport.document.pptx.PPTXConstants.PPR_ELT;
import static fr.opensagres.xdocreport.document.pptx.PPTXConstants.P_ELT;
import static fr.opensagres.xdocreport.document.pptx.PPTXConstants.P_NS;
import static fr.opensagres.xdocreport.document.pptx.PPTXConstants.R_ELT;
import static fr.opensagres.xdocreport.document.pptx.PPTXConstants.TXBODY_ELT;
import static fr.opensagres.xdocreport.document.pptx.PPTXConstants.T_ELT;

public class PPTXUtils
{

    public static boolean isAP( String uri, String localName, String name )
    {
        return ( A_NS.equals( uri ) && P_ELT.equals( localName ) );
    }

    public static boolean isAR( String uri, String localName, String name )
    {
        return ( A_NS.equals( uri ) && R_ELT.equals( localName ) );
    }

    public static boolean isAT( String uri, String localName, String name )
    {
        return ( A_NS.equals( uri ) && T_ELT.equals( localName ) );
    }

    public static boolean isAPPr( String uri, String localName, String name )
    {
        return ( A_NS.equals( uri ) && PPR_ELT.equals( localName ) );
    }

    public static boolean isPTxBody( String uri, String localName, String name )
    {
        return ( P_NS.equals( uri ) && TXBODY_ELT.equals( localName ) );
    }
    
    public static boolean isATxBody( String uri, String localName, String name )
    {
        return ( A_NS.equals( uri ) && TXBODY_ELT.equals( localName ) );
    }

}
