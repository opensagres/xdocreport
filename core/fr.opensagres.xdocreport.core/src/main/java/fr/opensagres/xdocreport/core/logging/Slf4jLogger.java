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

package fr.opensagres.xdocreport.core.logging;

import java.util.logging.Level;
import java.util.logging.LogRecord;

/**
 * <p>
 * java.util.logging.Logger implementation delegating to SLF4J.
 * </p>
 * <p>
 * Inspired from org.apache.cxf.common.logging.LogUtils
 * </p>
 * 
 * <pre>
 * FINEST  -&gt; TRACE
 * FINER   -&gt; DEBUG
 * FINE    -&gt; DEBUG
 * CONFIG  -&gt; DEBUG
 * INFO    -&gt; INFO
 * WARN ING -&gt; WARN
 * SEVER   -&gt; ERROR
 * </pre>
 * 
 * @author pascalleclercq
 */
public class Slf4jLogger
    extends AbstractDelegatingLogger
{

    private final org.slf4j.Logger logger;

    public Slf4jLogger( String name )
    {
        super( name );
        logger = org.slf4j.LoggerFactory.getLogger( name );
    }

    @Override
    public Level getLevel()
    {
        Level level;
        // Verify from the wider (trace) to the narrower (error)
        if ( logger.isTraceEnabled() )
        {
            level = Level.FINER; // FINEST
        }
        else if ( logger.isDebugEnabled() )
        {
            // map to the lowest between FINER, FINE and CONFIG
            level = Level.FINER;
        }
        else if ( logger.isInfoEnabled() )
        {
            level = Level.INFO;
        }
        else if ( logger.isWarnEnabled() )
        {
            level = Level.WARNING;
        }
        else if ( logger.isErrorEnabled() )
        {
            level = Level.SEVERE;
        }
        else
        {
            level = Level.OFF;
        }
        return level;
    }

    @Override
    protected void internalLogFormatted( String msg, LogRecord record )
    {

        Level level = record.getLevel();
        Throwable t = record.getThrown();

        /*
         * As we can not use a "switch ... case" block but only a "if ... else if ..." block, the order of the
         * comparisons is important. We first try log level FINE then INFO, WARN, FINER, etc
         */
        if ( Level.FINE.equals( level ) )
        {
            logger.debug( msg, t );
        }
        else if ( Level.INFO.equals( level ) )
        {
            logger.info( msg, t );
        }
        else if ( Level.WARNING.equals( level ) )
        {
            logger.warn( msg, t );
        }
        else if ( Level.FINER.equals( level ) )
        {
            logger.trace( msg, t );
        }
        else if ( Level.FINEST.equals( level ) )
        {
            logger.trace( msg, t );
        }
        else if ( Level.ALL.equals( level ) )
        {
            // should never occur, all is used to configure java.util.logging
            // but not accessible by the API Logger.xxx() API
            logger.error( msg, t );
        }
        else if ( Level.SEVERE.equals( level ) )
        {
            logger.error( msg, t );
        }
        else if ( Level.CONFIG.equals( level ) )
        {
            logger.debug( msg, t );
        }
        else if ( Level.OFF.equals( level ) )
        {
            // don't log
        }
    }
}
