package com.lowagie.text.awt;

import java.awt.Paint;
import java.awt.PaintContext;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.ColorModel;

public class Color implements Paint {
	
	int value;
	private float frgbvalue[] = null;
	private float fvalue[] = null;
	private float falpha = 0.0f;
	transient private PaintContext theContext;

	
	/**
     * The color white.  In the default sRGB space.
     */
    public final static Color white     = new Color(255, 255, 255);

    /**
     * The color white.  In the default sRGB space.
     * @since 1.4
     */
    public final static Color WHITE = white;

    /**
     * The color light gray.  In the default sRGB space.
     */
    public final static Color lightGray = new Color(192, 192, 192);

    /**
     * The color light gray.  In the default sRGB space.
     * @since 1.4
     */
    public final static Color LIGHT_GRAY = lightGray;

    /**
     * The color gray.  In the default sRGB space.
     */
    public final static Color gray      = new Color(128, 128, 128);

    /**
     * The color gray.  In the default sRGB space.
     * @since 1.4
     */
    public final static Color GRAY = gray;

    /**
     * The color dark gray.  In the default sRGB space.
     */
    public final static Color darkGray  = new Color(64, 64, 64);

    /**
     * The color dark gray.  In the default sRGB space.
     * @since 1.4
     */
    public final static Color DARK_GRAY = darkGray;

    /**
     * The color black.  In the default sRGB space.
     */
    public final static Color black     = new Color(0, 0, 0);
    
    /**
     * The color black.  In the default sRGB space.
     * @since 1.4
     */
    public final static Color BLACK = black;
    
    /**
     * The color red.  In the default sRGB space.
     */
    public final static Color red       = new Color(255, 0, 0);

    /**
     * The color red.  In the default sRGB space.
     * @since 1.4
     */
    public final static Color RED = red;

    /**
     * The color pink.  In the default sRGB space.
     */
    public final static Color pink      = new Color(255, 175, 175);

    /**
     * The color pink.  In the default sRGB space.
     * @since 1.4
     */
    public final static Color PINK = pink;

    /**
     * The color orange.  In the default sRGB space.
     */
    public final static Color orange    = new Color(255, 200, 0);

    /**
     * The color orange.  In the default sRGB space.
     * @since 1.4
     */
    public final static Color ORANGE = orange;

    /**
     * The color yellow.  In the default sRGB space.
     */
    public final static Color yellow    = new Color(255, 255, 0);

    /**
     * The color yellow.  In the default sRGB space.
     * @since 1.4
     */
    public final static Color YELLOW = yellow;

    /**
     * The color green.  In the default sRGB space.
     */
    public final static Color green     = new Color(0, 255, 0);

    /**
     * The color green.  In the default sRGB space.
     * @since 1.4
     */
    public final static Color GREEN = green;

    /**
     * The color magenta.  In the default sRGB space.
     */
    public final static Color magenta   = new Color(255, 0, 255);

    /**
     * The color magenta.  In the default sRGB space.
     * @since 1.4
     */
    public final static Color MAGENTA = magenta;

    /**
     * The color cyan.  In the default sRGB space.
     */
    public final static Color cyan  = new Color(0, 255, 255);

    /**
     * The color cyan.  In the default sRGB space.
     * @since 1.4
     */
    public final static Color CYAN = cyan;

    /**
     * The color blue.  In the default sRGB space.
     */
    public final static Color blue  = new Color(0, 0, 255);

    /**
     * The color blue.  In the default sRGB space.
     * @since 1.4
     */
    public final static Color BLUE = blue;

	private static final double FACTOR = 0.7;


	
	public Color(int r, int g, int b) {
		this(r, g, b, 255);
	}
	
	public Color(int r, int g, int b, int a) {
        value = ((a & 0xFF) << 24) |
                ((r & 0xFF) << 16) |
                ((g & 0xFF) << 8)  |
                ((b & 0xFF) << 0);
	testColorValueRange(r,g,b,a);
    }
	
	private static void testColorValueRange(int r, int g, int b, int a) {
        boolean rangeError = false;
	String badComponentString = "";
	
	if ( a < 0 || a > 255) {
	    rangeError = true;
	    badComponentString = badComponentString + " Alpha";
	}
        if ( r < 0 || r > 255) {
	    rangeError = true;
	    badComponentString = badComponentString + " Red";
	}
	if ( g < 0 || g > 255) {
	    rangeError = true;
	    badComponentString = badComponentString + " Green";
	}
	if ( b < 0 || b > 255) {
	    rangeError = true;
	    badComponentString = badComponentString + " Blue";
	}
	if ( rangeError == true ) {
	throw new IllegalArgumentException("Color parameter outside of expected range:"
					   + badComponentString);
	}
    }	
	
	public int getRed() {
		return (getRGB() >> 16) & 0xFF;
	    }


	    public int getGreen() {
		return (getRGB() >> 8) & 0xFF;
	    }


	    public int getBlue() {
		return (getRGB() >> 0) & 0xFF;
	    }	
	
	    public int getRGB() {
	    	return value;
	        }

	    public Color(float r, float g, float b) {
	        this( (int) (r*255+0.5), (int) (g*255+0.5), (int) (b*255+0.5));
	        testColorValueRange(r,g,b,1.0f);
	        frgbvalue = new float[3];
	        frgbvalue[0] = r;
	        frgbvalue[1] = g;
	        frgbvalue[2] = b;
	        falpha = 1.0f;
	        fvalue = frgbvalue;
	    }
	    
	    private static void testColorValueRange(float r, float g, float b, float a) {
	        boolean rangeError = false;
		String badComponentString = "";
		if ( a < 0.0 || a > 1.0) {
		    rangeError = true;
		    badComponentString = badComponentString + " Alpha";
		}
		if ( r < 0.0 || r > 1.0) {
		    rangeError = true;
		    badComponentString = badComponentString + " Red";
		}
		if ( g < 0.0 || g > 1.0) {
		    rangeError = true;
		    badComponentString = badComponentString + " Green";
		}
		if ( b < 0.0 || b > 1.0) {
		    rangeError = true;
		    badComponentString = badComponentString + " Blue";
		}
		if ( rangeError == true ) {
		throw new IllegalArgumentException("Color parameter outside of expected range:"
						   + badComponentString);
		}
	    }	    
	    
	    public Color darker() {
	    	return new Color(Math.max((int)(getRed()  *FACTOR), 0), 
	    			 Math.max((int)(getGreen()*FACTOR), 0),
	    			 Math.max((int)(getBlue() *FACTOR), 0));
	        }
	    
	    public int getAlpha() {
	        return (getRGB() >> 24) & 0xff;
	    }
	    
	    public int getTransparency() {
	        int alpha = getAlpha();
	        if (alpha == 0xff) {
	            return Transparency.OPAQUE;
	        }
	        else if (alpha == 0) {
	            return Transparency.BITMASK;
	        }
	        else {
	            return Transparency.TRANSLUCENT;
	        }
	    }
	    
	    public synchronized PaintContext createContext(ColorModel cm, Rectangle r,
				   Rectangle2D r2d,
				   AffineTransform xform,
                                        RenderingHints hints) {
			PaintContext pc = theContext;
			if (pc == null || ((ColorPaintContext)pc).color != getRGB()) {
			pc = new ColorPaintContext(getRGB(), cm);
			theContext = pc;
		}
		return pc;
	    }	    
	    
	    /**
	     * Converts a <code>String</code> to an integer and returns the 
	     * specified opaque <code>Color</code>. This method handles string
	     * formats that are used to represent octal and hexidecimal numbers.
	     * @param      nm a <code>String</code> that represents 
	     *                            an opaque color as a 24-bit integer
	     * @return     the new <code>Color</code> object.
	     * @see        java.lang.Integer#decode
	     * @exception  NumberFormatException  if the specified string cannot
	     *                      be interpreted as a decimal, 
	     *                      octal, or hexidecimal integer.
	     * @since      JDK1.1
	     */
	    public static Color decode(String nm) throws NumberFormatException {
	    Integer intval = Integer.decode(nm);
	    int i = intval.intValue();
	    return new Color((i >> 16) & 0xFF, (i >> 8) & 0xFF, i & 0xFF);
	    }	    
}
