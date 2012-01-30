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

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import org.apache.log4j.Appender;
import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Priority;
import org.apache.log4j.spi.LoggingEvent;

/**
 * java.util.logging.Logger implementation delegating to Log4j.
 * <p>
 * Inspired from org.apache.cxf.common.logging.LogUtils (@author gnodet)
 * </p>
 * 
 * @author pascalleclercq
 */
public class Log4jLogger
    extends AbstractDelegatingLogger
{
    private static final Map<Level, org.apache.log4j.Level> TO_LOG4J = new HashMap<Level, org.apache.log4j.Level>();

    private static final org.apache.log4j.Level TRACE;

    private final org.apache.log4j.Logger log;

    static
    {
        // older versions of log4j don't have TRACE, use debug
        org.apache.log4j.Level t = org.apache.log4j.Level.DEBUG;
        try
        {
            Field f = org.apache.log4j.Level.class.getField( "TRACE" );
            t = (org.apache.log4j.Level) f.get( null );
        }
        catch ( Throwable ex )
        {
            // ignore, assume old version of log4j
        }
        TRACE = t;

        TO_LOG4J.put( Level.ALL, org.apache.log4j.Level.ALL );
        TO_LOG4J.put( Level.SEVERE, org.apache.log4j.Level.ERROR );
        TO_LOG4J.put( Level.WARNING, org.apache.log4j.Level.WARN );
        TO_LOG4J.put( Level.INFO, org.apache.log4j.Level.INFO );
        TO_LOG4J.put( Level.CONFIG, org.apache.log4j.Level.DEBUG );
        TO_LOG4J.put( Level.FINE, org.apache.log4j.Level.DEBUG );
        TO_LOG4J.put( Level.FINER, TRACE );
        TO_LOG4J.put( Level.FINEST, TRACE );
        TO_LOG4J.put( Level.OFF, org.apache.log4j.Level.OFF );
    }

    public Log4jLogger( String name )
    {
        super( name );
        log = org.apache.log4j.LogManager.getLogger( name );
    }

    public Level getLevel()
    {
        org.apache.log4j.Level l = log.getEffectiveLevel();
        if ( l != null )
        {
            return fromL4J( l );
        }
        return null;
    }

    public void setLevel( Level newLevel )
        throws SecurityException
    {
        log.setLevel( TO_LOG4J.get( newLevel ) );
    }

    public synchronized void addHandler( Handler handler )
        throws SecurityException
    {
        log.addAppender( new HandlerWrapper( handler ) );
    }

    public synchronized void removeHandler( Handler handler )
        throws SecurityException
    {
        log.removeAppender( "HandlerWrapper-" + handler.hashCode() );
    }

    public synchronized Handler[] getHandlers()
    {
        List<Handler> ret = new ArrayList<Handler>();
        Enumeration en = log.getAllAppenders();
        while ( en.hasMoreElements() )
        {
            Appender ap = (Appender) en.nextElement();
            if ( ap instanceof HandlerWrapper )
            {
                ret.add( ( (HandlerWrapper) ap ).getHandler() );
            }
        }
        return ret.toArray( new Handler[ret.size()] );
    }

    protected void internalLogFormatted( String msg, LogRecord record )
    {
        log.log( AbstractDelegatingLogger.class.getName(), TO_LOG4J.get( record.getLevel() ), msg, record.getThrown() );
    }

    private Level fromL4J( org.apache.log4j.Level l )
    {
        Level l2 = null;
        switch ( l.toInt() )
        {
            case org.apache.log4j.Level.ALL_INT:
                l2 = Level.ALL;
                break;
            case org.apache.log4j.Level.FATAL_INT:
                l2 = Level.SEVERE;
                break;
            case org.apache.log4j.Level.ERROR_INT:
                l2 = Level.SEVERE;
                break;
            case org.apache.log4j.Level.WARN_INT:
                l2 = Level.WARNING;
                break;
            case org.apache.log4j.Level.INFO_INT:
                l2 = Level.INFO;
                break;
            case org.apache.log4j.Level.DEBUG_INT:
                l2 = Level.FINE;
                break;
            case org.apache.log4j.Level.OFF_INT:
                l2 = Level.OFF;
                break;
            default:
                if ( l.toInt() == TRACE.toInt() )
                {
                    l2 = Level.FINEST;
                }
                else
                {
                    l2 = null;
                }
        }
        return l2;
    }

    private class HandlerWrapper
        extends AppenderSkeleton
    {
        Handler handler;

        public HandlerWrapper( Handler h )
        {
            handler = h;
            name = "HandlerWrapper-" + h.hashCode();
        }

        public Handler getHandler()
        {
            return handler;
        }

        @Override
        protected void append( LoggingEvent event )
        {
            LogRecord lr = new LogRecord( fromL4J( event.getLevel() ), event.getMessage().toString() );
            lr.setLoggerName( event.getLoggerName() );
            if ( event.getThrowableInformation() != null )
            {
                lr.setThrown( event.getThrowableInformation().getThrowable() );
            }
            String rbname = getResourceBundleName();
            if ( rbname != null )
            {
                lr.setResourceBundleName( rbname );
                lr.setResourceBundle( getResourceBundle() );
            }
            getFullInfoForLogUtils( lr, event.fqnOfCategoryClass );
            handler.publish( lr );
        }

        public void close()
        {
            handler.close();
            closed = true;
        }

        public boolean requiresLayout()
        {
            return false;
        }

        @Override
        public Priority getThreshold()
        {
            return TO_LOG4J.get( handler.getLevel() );
        }

        @Override
        public boolean isAsSevereAsThreshold( Priority priority )
        {
            Priority p = getThreshold();
            return ( p == null ) || priority.isGreaterOrEqual( p );
        }
    }

    private static void getFullInfoForLogUtils( LogRecord lr, String cname )
    {
        StackTraceElement el[] = Thread.currentThread().getStackTrace();
        for ( int x = el.length - 2; x >= 0; x-- )
        {
            if ( LogUtils.class.getName().equals( el[x].getClassName() ) || cname.equals( el[x].getClassName() ) )
            {
                lr.setSourceClassName( el[x + 1].getClassName() );
                lr.setSourceMethodName( el[x + 1].getMethodName() );
                return;
            }
        }
    }
}
