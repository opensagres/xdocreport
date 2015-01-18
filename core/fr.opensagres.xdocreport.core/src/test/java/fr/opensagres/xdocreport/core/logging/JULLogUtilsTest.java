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
package fr.opensagres.xdocreport.core.logging;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class JULLogUtilsTest
{
    private static Class backupLoggerClass;

    @BeforeClass
    public static void initialize()
    {
        backupLoggerClass = LogUtils.getLoggerClass();
        LogUtils.setLoggerClass( null );

    }

    @AfterClass
    public static void finish()
    {
        LogUtils.setLoggerClass( backupLoggerClass );
    }

    @Test
    public void testGetLogger()
    {
        Logger logger = LogUtils.getLogger( JULLogUtilsTest.class );
        assertNotNull( logger );
        System.out.println( logger );
    }

    @Test
    public void testHandleMessage()
        throws Exception
    {
        Logger log = LogUtils.getLogger( JULLogUtilsTest.class );
        Handler handler = new ConsoleHandler();
        log.addHandler( handler );
        // handler called *before* localization of message
        LogRecord record = new LogRecord( Level.WARNING, "FOOBAR_MSG" );
        record.setResourceBundle( log.getResourceBundle() );

        handler.publish( record );

        log.log( Level.WARNING, "FOOBAR_MSG" );

        log.removeHandler( handler );
    }

    @Test
    public void testLogNoParamsOrThrowable()
    {
        Logger log = LogUtils.getLogger( JULLogUtilsTest.class );
        Handler handler = new ConsoleHandler();
        log.addHandler( handler );
        // handler called *after* localization of message
        LogRecord record = new LogRecord( Level.SEVERE, "subbed in {0} only" );

        handler.publish( record );

        LogUtils.log( log, Level.SEVERE, "SUB1_MSG" );

        log.removeHandler( handler );
    }

    @Test
    public void testLogNoParamsWithThrowable()
    {
        Logger log = LogUtils.getLogger( JULLogUtilsTest.class );
        Handler handler = new ConsoleHandler();
        Exception ex = new Exception( "x" );
        LogRecord record = new LogRecord( Level.SEVERE, "subbed in {0} only" );
        record.setThrown( ex );

        handler.publish( record );

        synchronized ( log )
        {
            log.addHandler( handler );
            // handler called *after* localization of message
            LogUtils.log( log, Level.SEVERE, "SUB1_MSG", ex );

            log.removeHandler( handler );
        }
    }

    @Test
    public void testLogParamSubstitutionWithThrowable()
        throws Exception
    {
        Logger log = LogUtils.getLogger( JULLogUtilsTest.class );
        Handler handler = new ConsoleHandler();
        Exception ex = new Exception();
        LogRecord record = new LogRecord( Level.SEVERE, "subbed in 1 only" );
        record.setThrown( ex );

        handler.publish( record );

        synchronized ( log )
        {
            log.addHandler( handler );
            LogUtils.log( log, Level.SEVERE, "SUB1_MSG", ex, 1 );

            log.removeHandler( handler );
        }
    }

    @Test
    public void testLogParamsSubstitutionWithThrowable()
        throws Exception
    {
        Logger log = LogUtils.getLogger( JULLogUtilsTest.class );
        Handler handler = new ConsoleHandler();
        Exception ex = new Exception();
        LogRecord record = new LogRecord( Level.SEVERE, "subbed in 4 & 3" );
        record.setThrown( ex );

        handler.publish( record );

        synchronized ( log )
        {
            log.addHandler( handler );
            LogUtils.log( log, Level.SEVERE, "SUB2_MSG", ex, new Object[] { 3, 4 } );

            log.removeHandler( handler );
        }
    }

    @Test
    public void testClassMethodNames()
        throws Exception
    {
        Logger log = LogUtils.getLogger( JULLogUtilsTest.class );
        TestLogHandler handler = new TestLogHandler();
        log.addHandler( handler );

        // logger called directly
        log.warning( "hello" );

        String cname = handler.cname;
        String mname = handler.mname;

        // logger called through LogUtils
        LogUtils.log( log, Level.WARNING, "FOOBAR_MSG" );

        assertEquals( cname, handler.cname );
        assertEquals( mname, handler.mname );
    }

    private static final class TestLogHandler
        extends Handler
    {
        String cname;

        String mname;

        public void close()
            throws SecurityException
        {
        }

        public void flush()
        {
        }

        public void publish( LogRecord record )
        {
            cname = record.getSourceClassName();
            mname = record.getSourceMethodName();
        }
    }

}
