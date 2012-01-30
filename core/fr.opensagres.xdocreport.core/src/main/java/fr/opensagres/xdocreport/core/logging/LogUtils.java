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

import static fr.opensagres.xdocreport.core.utils.StringUtils.isEmpty;

import java.lang.reflect.Constructor;
import java.text.MessageFormat;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 * A container for static utility methods related to logging.
 * <p>
 * Inspired from org.apache.cxf.common.logging.LogUtils
 * </p>
 * 
 * @author pascalleclercq
 */
@SuppressWarnings( "unchecked" )
public final class LogUtils
{

    private static final Object[] NO_PARAMETERS = new Object[0];

    private static Class<? extends AbstractDelegatingLogger> loggerClass;

    // for testing purpose
    static void setLoggerClass( Class<? extends AbstractDelegatingLogger> loggerClass )
    {
        LogUtils.loggerClass = loggerClass;
    }

    static Class<? extends AbstractDelegatingLogger> getLoggerClass()
    {
        return loggerClass;
    }

    /**
     * Prevents instantiation.
     */
    private LogUtils()
    {
    }

    static
    {

        try
        {
            String cname = null;

            Class.forName( "org.slf4j.impl.StaticLoggerBinder" );
            Class<?> cls = Class.forName( "org.slf4j.LoggerFactory" );
            Class<?> fcls = cls.getMethod( "getILoggerFactory" ).invoke( null ).getClass();
            if ( fcls.getName().contains( "Log4j" ) )
            {
                cname = "fr.opensagres.xdocreport.core.logging.Log4jLogger";
            }
            else if ( fcls.getName().contains( "JCL" ) )
            {
                cls = Class.forName( "org.apache.commons.logging.LogFactory" );
                fcls = cls.getMethod( "getFactory" ).invoke( null ).getClass();
                if ( fcls.getName().contains( "Log4j" ) )
                {
                    cname = "fr.opensagres.xdocreport.core.logging.Log4jLogger";
                }
            }
            else
            {
                cname = "fr.opensagres.xdocreport.core.logging.Slf4jLogger";
            }

            if ( !isEmpty( cname ) )
            {
                try
                {

                    loggerClass =
                        (Class<? extends AbstractDelegatingLogger>) Class.forName( cname.trim(),
                                                                                   true,
                                                                                   Thread.currentThread().getContextClassLoader() );
                }
                catch ( Throwable ex )
                {
                    loggerClass = (Class<? extends AbstractDelegatingLogger>) Class.forName( cname.trim() );
                }
                getLogger( LogUtils.class ).fine( "Using " + loggerClass.getName() + " for logging." );
            }
        }
        catch ( Throwable ex )
        {
            // ignore - if we get here, some issue prevented the logger class
            // from being loaded.
            // maybe a ClassNotFound or NoClassDefFound or similar. Just use
            // j.u.l
            loggerClass = null;
        }
    }

    /**
     * Get a Logger with the associated default resource bundle for the class.
     * 
     * @param cls the Class to contain the Logger
     * @return an appropriate Logger
     */
    public static Logger getLogger( Class<?> cls )
    {
        return createLogger( cls.getName() );
    }

    /**
     * Create a logger
     */
    protected static Logger createLogger( String loggerName )
    {
        if ( loggerClass != null )
        {

            try
            {

                Constructor<? extends AbstractDelegatingLogger> cns = loggerClass.getConstructor( String.class );
                return cns.newInstance( loggerName );

            }
            catch ( Exception e )
            {
                throw new RuntimeException( e );
            }
        }
        return Logger.getLogger( loggerName, null );

    }

    /**
     * Allows both parameter substitution and a typed Throwable to be logged.
     * 
     * @param logger the Logger the log to
     * @param level the severity level
     * @param message the log message
     * @param throwable the Throwable to log
     * @param parameter the parameter to substitute into message
     */
    public static void log( Logger logger, Level level, String message, Throwable throwable, Object parameter )
    {
        if ( logger.isLoggable( level ) )
        {
            final String formattedMessage = MessageFormat.format( message, parameter );
            doLog( logger, level, formattedMessage, throwable );
        }
    }

    /**
     * Allows both parameter substitution and a typed Throwable to be logged.
     * 
     * @param logger the Logger the log to
     * @param level the severity level
     * @param message the log message
     * @param throwable the Throwable to log
     * @param parameters the parameters to substitute into message
     */
    public static void log( Logger logger, Level level, String message, Throwable throwable, Object... parameters )
    {
        if ( logger.isLoggable( level ) )
        {
            final String formattedMessage = MessageFormat.format( message, parameters );
            doLog( logger, level, formattedMessage, throwable );
        }
    }

    /**
     * Checks log level and logs
     * 
     * @param logger the Logger the log to
     * @param level the severity level
     * @param message the log message
     */
    public static void log( Logger logger, Level level, String message )
    {
        log( logger, level, message, NO_PARAMETERS );
    }

    /**
     * Checks log level and logs
     * 
     * @param logger the Logger the log to
     * @param level the severity level
     * @param message the log message
     * @param throwable the Throwable to log
     */
    public static void log( Logger logger, Level level, String message, Throwable throwable )
    {
        log( logger, level, message, throwable, NO_PARAMETERS );
    }

    /**
     * Checks log level and logs
     * 
     * @param logger the Logger the log to
     * @param level the severity level
     * @param message the log message
     * @param parameter the parameter to substitute into message
     */
    public static void log( Logger logger, Level level, String message, Object parameter )
    {
        log( logger, level, message, new Object[] { parameter } );
    }

    /**
     * Checks log level and logs
     * 
     * @param logger the Logger the log to
     * @param level the severity level
     * @param message the log message
     * @param parameters the parameters to substitute into message
     */
    public static void log( Logger logger, Level level, String message, Object[] parameters )
    {
        if ( logger.isLoggable( level ) )
        {

            doLog( logger, level, message, null );
        }
    }

    private static void doLog( Logger log, Level level, String msg, Throwable t )
    {
        LogRecord record = new LogRecord( level, msg );

        record.setLoggerName( log.getName() );
        record.setResourceBundleName( log.getResourceBundleName() );
        record.setResourceBundle( log.getResourceBundle() );

        if ( t != null )
        {
            record.setThrown( t );
        }

        // try to get the right class name/method name - just trace
        // back the stack till we get out of this class
        StackTraceElement stack[] = ( new Throwable() ).getStackTrace();
        String cname = LogUtils.class.getName();
        for ( int x = 0; x < stack.length; x++ )
        {
            StackTraceElement frame = stack[x];
            if ( !frame.getClassName().equals( cname ) )
            {
                record.setSourceClassName( frame.getClassName() );
                record.setSourceMethodName( frame.getMethodName() );
                break;
            }
        }
        log.log( record );
    }

    public static Logger getLogger( String name )
    {
        return createLogger( name );
    }

}
