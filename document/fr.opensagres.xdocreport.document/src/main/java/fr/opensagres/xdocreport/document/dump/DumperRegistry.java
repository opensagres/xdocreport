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
package fr.opensagres.xdocreport.document.dump;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.core.logging.LogUtils;
import fr.opensagres.xdocreport.core.registry.AbstractRegistry;
import fr.opensagres.xdocreport.document.discovery.IDumperDiscovery;

/**
 * {@link IDumper} registry.
 */
public class DumperRegistry
    extends AbstractRegistry<IDumperDiscovery>
{

    private static final DumperRegistry INSTANCE = new DumperRegistry();

    private static final Logger LOGGER = LogUtils.getLogger( DumperRegistry.class.getName() );

    private Map<String /* kind */, IDumper> dumpers = new HashMap<String, IDumper>();

    public DumperRegistry()
    {

        super( IDumperDiscovery.class );

    }

    /**
     * Returns the dumper registry instance.
     * 
     * @return
     */
    public static DumperRegistry getRegistry()
    {
        return INSTANCE;
    }

    /**
     * Returns the dumper registered with the given kind in the registry.
     * 
     * @param kind the dumper kind.
     * @return
     * @throws XDocReportException thrown when the dumper is not found.
     */
    public IDumper findDumper( String kind )
        throws XDocReportException
    {
        IDumper dumper = getDumper( kind );
        if ( dumper == null )
        {
            String msg = String.format( "Cannot find dumper with kind=%s", dumper );
            LOGGER.severe( msg );
            throw new XDocReportException( msg );
        }
        return dumper;
    }

    /**
     * Returns the dumper registered with the given kind in the registry and null otherwise.
     * 
     * @param options
     * @return
     */
    public IDumper getDumper( String kind )
    {
        initializeIfNeeded();
        return dumpers.get( kind );
    }

    /**
     * Returns teh list of the dumper kind registered in the registry.
     * 
     * @return
     */
    public Set<String> getKinds()
    {
        initializeIfNeeded();
        return dumpers.keySet();
    }

    @Override
    protected void doDispose()
    {
        this.dumpers.clear();
    }

    @Override
    protected boolean registerInstance( IDumperDiscovery discovery )
    {
        dumpers.put( discovery.getKind(), discovery.getDumper() );
        return true;
    }
}
