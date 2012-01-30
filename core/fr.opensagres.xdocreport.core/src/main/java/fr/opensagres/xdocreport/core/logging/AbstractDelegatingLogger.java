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

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.logging.Filter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 * java.util.logging.Logger implementation delegating to another framework.
 * <p>
 * Inspired from org.apache.cxf.common.logging.LogUtils (@author gnodet)
 * </p>
 * 
 * @author pascalleclercq
 */
public abstract class AbstractDelegatingLogger
    extends Logger
{

    protected AbstractDelegatingLogger( String name )
    {
        super( name, null );

    }

    public void log( LogRecord record )
    {
        if ( isLoggable( record.getLevel() ) )
        {
            doLog( record );
        }
    }

    public void log( Level level, String msg )
    {
        if ( isLoggable( level ) )
        {
            LogRecord lr = new LogRecord( level, msg );
            doLog( lr );
        }
    }

    public void log( Level level, String msg, Object param1 )
    {
        if ( isLoggable( level ) )
        {
            LogRecord lr = new LogRecord( level, msg );
            Object params[] = { param1 };
            lr.setParameters( params );
            doLog( lr );
        }
    }

    public void log( Level level, String msg, Object params[] )
    {
        if ( isLoggable( level ) )
        {
            LogRecord lr = new LogRecord( level, msg );
            lr.setParameters( params );
            doLog( lr );
        }
    }

    public void log( Level level, String msg, Throwable thrown )
    {
        if ( isLoggable( level ) )
        {
            LogRecord lr = new LogRecord( level, msg );
            lr.setThrown( thrown );
            doLog( lr );
        }
    }

    public void logp( Level level, String sourceClass, String sourceMethod, String msg )
    {
        if ( isLoggable( level ) )
        {
            LogRecord lr = new LogRecord( level, msg );
            lr.setSourceClassName( sourceClass );
            lr.setSourceMethodName( sourceMethod );
            doLog( lr );
        }
    }

    public void logp( Level level, String sourceClass, String sourceMethod, String msg, Object param1 )
    {
        if ( isLoggable( level ) )
        {
            LogRecord lr = new LogRecord( level, msg );
            lr.setSourceClassName( sourceClass );
            lr.setSourceMethodName( sourceMethod );
            Object params[] = { param1 };
            lr.setParameters( params );
            doLog( lr );
        }
    }

    public void logp( Level level, String sourceClass, String sourceMethod, String msg, Object params[] )
    {
        if ( isLoggable( level ) )
        {
            LogRecord lr = new LogRecord( level, msg );
            lr.setSourceClassName( sourceClass );
            lr.setSourceMethodName( sourceMethod );
            lr.setParameters( params );
            doLog( lr );
        }
    }

    public void logp( Level level, String sourceClass, String sourceMethod, String msg, Throwable thrown )
    {
        if ( isLoggable( level ) )
        {
            LogRecord lr = new LogRecord( level, msg );
            lr.setSourceClassName( sourceClass );
            lr.setSourceMethodName( sourceMethod );
            lr.setThrown( thrown );
            doLog( lr );
        }
    }

    public void logrb( Level level, String sourceClass, String sourceMethod, String bundleName, String msg )
    {
        if ( isLoggable( level ) )
        {
            LogRecord lr = new LogRecord( level, msg );
            lr.setSourceClassName( sourceClass );
            lr.setSourceMethodName( sourceMethod );
            doLog( lr, bundleName );
        }
    }

    public void logrb( Level level, String sourceClass, String sourceMethod, String bundleName, String msg,
                       Object param1 )
    {
        if ( isLoggable( level ) )
        {
            LogRecord lr = new LogRecord( level, msg );
            lr.setSourceClassName( sourceClass );
            lr.setSourceMethodName( sourceMethod );
            Object params[] = { param1 };
            lr.setParameters( params );
            doLog( lr, bundleName );
        }
    }

    public void logrb( Level level, String sourceClass, String sourceMethod, String bundleName, String msg,
                       Object params[] )
    {
        if ( isLoggable( level ) )
        {
            LogRecord lr = new LogRecord( level, msg );
            lr.setSourceClassName( sourceClass );
            lr.setSourceMethodName( sourceMethod );
            lr.setParameters( params );
            doLog( lr, bundleName );
        }
    }

    public void logrb( Level level, String sourceClass, String sourceMethod, String bundleName, String msg,
                       Throwable thrown )
    {
        if ( isLoggable( level ) )
        {
            LogRecord lr = new LogRecord( level, msg );
            lr.setSourceClassName( sourceClass );
            lr.setSourceMethodName( sourceMethod );
            lr.setThrown( thrown );
            doLog( lr, bundleName );
        }
    }

    public void entering( String sourceClass, String sourceMethod )
    {
        if ( isLoggable( Level.FINER ) )
        {
            logp( Level.FINER, sourceClass, sourceMethod, "ENTRY" );
        }
    }

    public void entering( String sourceClass, String sourceMethod, Object param1 )
    {
        if ( isLoggable( Level.FINER ) )
        {
            Object params[] = { param1 };
            logp( Level.FINER, sourceClass, sourceMethod, "ENTRY {0}", params );
        }
    }

    public void entering( String sourceClass, String sourceMethod, Object params[] )
    {
        if ( isLoggable( Level.FINER ) )
        {
            String msg = "ENTRY";
            if ( params == null )
            {
                logp( Level.FINER, sourceClass, sourceMethod, msg );
                return;
            }
            StringBuilder builder = new StringBuilder( msg );
            for ( int i = 0; i < params.length; i++ )
            {
                builder.append( " {" );
                builder.append( Integer.toString( i ) );
                builder.append( "}" );
            }
            logp( Level.FINER, sourceClass, sourceMethod, builder.toString(), params );
        }
    }

    public void exiting( String sourceClass, String sourceMethod )
    {
        if ( isLoggable( Level.FINER ) )
        {
            logp( Level.FINER, sourceClass, sourceMethod, "RETURN" );
        }
    }

    public void exiting( String sourceClass, String sourceMethod, Object result )
    {
        if ( isLoggable( Level.FINER ) )
        {
            Object params[] = { result };
            logp( Level.FINER, sourceClass, sourceMethod, "RETURN {0}", params );
        }
    }

    public void throwing( String sourceClass, String sourceMethod, Throwable thrown )
    {
        if ( isLoggable( Level.FINER ) )
        {
            LogRecord lr = new LogRecord( Level.FINER, "THROW" );
            lr.setSourceClassName( sourceClass );
            lr.setSourceMethodName( sourceMethod );
            lr.setThrown( thrown );
            doLog( lr );
        }
    }

    public void severe( String msg )
    {
        if ( isLoggable( Level.SEVERE ) )
        {
            LogRecord lr = new LogRecord( Level.SEVERE, msg );
            doLog( lr );
        }
    }

    public void warning( String msg )
    {
        if ( isLoggable( Level.WARNING ) )
        {
            LogRecord lr = new LogRecord( Level.WARNING, msg );
            doLog( lr );
        }
    }

    public void info( String msg )
    {
        if ( isLoggable( Level.INFO ) )
        {
            LogRecord lr = new LogRecord( Level.INFO, msg );
            doLog( lr );
        }
    }

    public void config( String msg )
    {
        if ( isLoggable( Level.CONFIG ) )
        {
            LogRecord lr = new LogRecord( Level.CONFIG, msg );
            doLog( lr );
        }
    }

    public void fine( String msg )
    {
        if ( isLoggable( Level.FINE ) )
        {
            LogRecord lr = new LogRecord( Level.FINE, msg );
            doLog( lr );
        }
    }

    public void finer( String msg )
    {
        if ( isLoggable( Level.FINER ) )
        {
            LogRecord lr = new LogRecord( Level.FINER, msg );
            doLog( lr );
        }
    }

    public void finest( String msg )
    {
        if ( isLoggable( Level.FINEST ) )
        {
            LogRecord lr = new LogRecord( Level.FINEST, msg );
            doLog( lr );
        }
    }

    public void setLevel( Level newLevel )
        throws SecurityException
    {
        throw new UnsupportedOperationException();
    }

    public abstract Level getLevel();

    public boolean isLoggable( Level level )
    {
        Level l = getLevel();
        return level.intValue() >= l.intValue() && l != Level.OFF;
    }

    public synchronized void addHandler( Handler handler )
        throws SecurityException
    {
        throw new UnsupportedOperationException();
    }

    public synchronized void removeHandler( Handler handler )
        throws SecurityException
    {
        throw new UnsupportedOperationException();
    }

    public synchronized Handler[] getHandlers()
    {
        throw new UnsupportedOperationException();
    }

    public synchronized void setUseParentHandlers( boolean useParentHandlers )
    {
        throw new UnsupportedOperationException();
    }

    public synchronized boolean getUseParentHandlers()
    {
        throw new UnsupportedOperationException();
    }

    public Logger getParent()
    {
        return null;
    }

    public void setParent( Logger parent )
    {
        throw new UnsupportedOperationException();
    }

    protected void doLog( LogRecord lr )
    {
        lr.setLoggerName( getName() );
        String rbname = getResourceBundleName();
        if ( rbname != null )
        {
            lr.setResourceBundleName( rbname );
            lr.setResourceBundle( getResourceBundle() );
        }
        internalLog( lr );
    }

    protected void doLog( LogRecord lr, String rbname )
    {
        lr.setLoggerName( getName() );
        if ( rbname != null )
        {
            lr.setResourceBundleName( rbname );
            lr.setResourceBundle( loadResourceBundle( rbname ) );
        }
        internalLog( lr );
    }

    protected void internalLog( LogRecord record )
    {
        Filter filter = getFilter();
        if ( filter != null && !filter.isLoggable( record ) )
        {
            return;
        }
        String msg = formatMessage( record );
        internalLogFormatted( msg, record );
    }

    protected abstract void internalLogFormatted( String msg, LogRecord record );

    protected String formatMessage( LogRecord record )
    {
        String format = record.getMessage();
        ResourceBundle catalog = record.getResourceBundle();
        if ( catalog != null )
        {
            try
            {
                format = catalog.getString( record.getMessage() );
            }
            catch ( MissingResourceException ex )
            {
                format = record.getMessage();
            }
        }
        try
        {
            Object parameters[] = record.getParameters();
            if ( parameters == null || parameters.length == 0 )
            {
                return format;
            }
            if ( format.indexOf( "{0" ) >= 0 || format.indexOf( "{1" ) >= 0 || format.indexOf( "{2" ) >= 0
                || format.indexOf( "{3" ) >= 0 )
            {
                return java.text.MessageFormat.format( format, parameters );
            }
            return format;
        }
        catch ( Exception ex )
        {
            return format;
        }
    }

    /**
     * Load the specified resource bundle
     * 
     * @param resourceBundleName the name of the resource bundle to load, cannot be null
     * @return the loaded resource bundle.
     * @throws java.util.MissingResourceException If the specified resource bundle can not be loaded.
     */
    static ResourceBundle loadResourceBundle( String resourceBundleName )
    {
        // try context class loader to load the resource
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        if ( null != cl )
        {
            try
            {
                return ResourceBundle.getBundle( resourceBundleName, Locale.getDefault(), cl );
            }
            catch ( MissingResourceException e )
            {
                // Failed to load using context classloader, ignore
            }
        }
        // try system class loader to load the resource
        cl = ClassLoader.getSystemClassLoader();
        if ( null != cl )
        {
            try
            {
                return ResourceBundle.getBundle( resourceBundleName, Locale.getDefault(), cl );
            }
            catch ( MissingResourceException e )
            {
                // Failed to load using system classloader, ignore
            }
        }
        return null;
    }

}