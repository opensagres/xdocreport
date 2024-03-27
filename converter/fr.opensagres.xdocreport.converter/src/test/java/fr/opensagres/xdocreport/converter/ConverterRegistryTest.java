/*
 * The MIT License
 *
 * Copyright 2015 benoit.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package fr.opensagres.xdocreport.converter;

import fr.opensagres.xdocreport.converter.discovery.IConverterDiscovery;
import fr.opensagres.xdocreport.converter.internal.AbstractConverterNoEntriesSupport;
import java.io.InputStream;
import java.io.OutputStream;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author benoit
 */
public class ConverterRegistryTest {

    private static final ConverterRegistry reg = ConverterRegistry.getRegistry();

    private static void register(String from, String to, String via, String with) {
        ConverterRegistry.getRegistry().registerInstance(new Disco(from, to, via, with));
    }
    
    @Before
    public void setup() {
        register("from_1", "to_1", "via_1", "with_1");
        IConverter con = reg.getConverter("from_1", "to_1", "via_1", "with_1");
        Assert.assertNotNull(con);
        Assert.assertTrue(con instanceof IConverter);
    }

    @After
    public void tearDown() {
        reg.dispose();
    }

    @Test
    public void testDispose() {
        Assert.assertFalse(reg.getFroms().isEmpty());
        reg.dispose();
        IConverter con = reg.getConverter("from_1", "to_1", "via_1", "with_1");
        Assert.assertNull(con);
        Assert.assertFalse(con instanceof IConverter);
        Assert.assertTrue(reg.getFroms().isEmpty());

    }

    @Test
    public void allowedNullTest() {
        IConverter con = reg.getConverter("from_1", "to_1", null, "with_1");
        Assert.assertNotNull(con);
        Assert.assertTrue(con instanceof IConverter);
        con = reg.getConverter("from_1", "to_1", "via_1", null);
        Assert.assertNotNull(con);
        Assert.assertTrue(con instanceof IConverter);
        con = reg.getConverter("from_1", "to_1", null, null);
        Assert.assertNotNull(con);
        Assert.assertTrue(con instanceof IConverter);
    }

    @Test
    public void disallowedNullTest() {
        IConverter con = reg.getConverter(null, "to_1", "via_1", "with_1");
        Assert.assertNull(con);
        Assert.assertFalse(con instanceof IConverter);
        con = reg.getConverter("from_1", null, "via_1", "with_1");
        Assert.assertNull(con);
        Assert.assertFalse(con instanceof IConverter);
        con = reg.getConverter(null, null, "via_1", "with_1");
        Assert.assertNull(con);
        Assert.assertFalse(con instanceof IConverter);
    }

    @Test
    public void basicFailTest() {
        IConverter con = reg.getConverter("from_1", "to_1", "via_1", "nonexistent");
        Assert.assertNull(con);
        Assert.assertFalse(con instanceof IConverter);
        con = reg.getConverter("from_1", "to_1", "nonexistent", "with_1");
        Assert.assertNull(con);
        Assert.assertFalse(con instanceof IConverter);
        con = reg.getConverter("from_1", "nonexistent", "via_1", "with_1");
        Assert.assertNull(con);
        Assert.assertFalse(con instanceof IConverter);
        con = reg.getConverter("nonexistent", "to_1", "via_1", "with_1");
        Assert.assertNull(con);
        Assert.assertFalse(con instanceof IConverter);
    }

    @Test
    public void basicTooDissimilarTest() {
        IConverter con = reg.getConverter("from_1", "to_1", "f", null);
        Assert.assertNull(con);
        Assert.assertFalse(con instanceof IConverter);
    }

    private static class Disco implements IConverterDiscovery {

        private final String from, to, via, with;

        public Disco(String from, String to, String via, String with) {
            this.from = from;
            this.to = to;
            this.via = via;
            this.with = with;
        }

        public String getFrom() {
            return from;
        }

        public String getTo() {
            return to;
        }

        public String getVia() {
            return via;
        }

        public String getWith() {
            return with;
        }

        public IConverter getConverter() {
            return new AbstractConverterNoEntriesSupport() {
                public void convert(InputStream in, OutputStream out, Options options) throws XDocConverterException {
                    throw new UnsupportedOperationException("Not supported yet.");
                }

                public MimeMapping getMimeMapping() {
                    throw new UnsupportedOperationException("Not supported yet.");
                }
            };
        }

        public String getId() {
            throw new UnsupportedOperationException("Not supported.");
        }

        public String getDescription() {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }
}
