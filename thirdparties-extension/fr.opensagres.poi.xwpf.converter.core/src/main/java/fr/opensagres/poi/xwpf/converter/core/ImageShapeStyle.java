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
package fr.opensagres.poi.xwpf.converter.core;

import java.util.HashMap;
import java.util.Map;

/**
 * This ImageShapeStyle is read style of Picture Shape. Used for VML Picture Shape.
 * <p>
 * It parses the CSS like content of the <code>style</code> attribute of a <code>v:shape</code>, ex :
 *
 * <pre>
 * position:absolute;margin-left:-71.65pt;margin-top:-69.65pt;width:595.5pt;height:403.5pt;z-index:-1
 * </pre>
 *
 * Beside the size of the shape, the position information is read too, so that a floating (absolutely positioned) shape
 * can be drawn outside of the text flow instead of being inserted inline (which pushes the text of the document down).
 */
public class ImageShapeStyle {
    private static final String STYLE_DELIMITER = ";";
    private static final String PROPERTY_DELIMITER = ":";

    private static final String WIDTH_TOKEN = "width";
    private static final String HEIGHT_TOKEN = "height";
    private static final String POSITION_TOKEN = "position";
    private static final String MARGIN_LEFT_TOKEN = "margin-left";
    private static final String MARGIN_TOP_TOKEN = "margin-top";
    private static final String POSITION_HORIZONTAL_TOKEN = "mso-position-horizontal";
    private static final String POSITION_VERTICAL_TOKEN = "mso-position-vertical";
    private static final String POSITION_HORIZONTAL_RELATIVE_TOKEN = "mso-position-horizontal-relative";
    private static final String POSITION_VERTICAL_RELATIVE_TOKEN = "mso-position-vertical-relative";

    private static final String ABSOLUTE_POSITION = "absolute";

    private static final float DEFAULT_SIZE = 10;

    private final float height;
    private final float width;
    private final boolean canUse;
    private final boolean absolutePosition;
    private final float marginLeft;
    private final float marginTop;
    private final String positionHorizontal;
    private final String positionVertical;
    private final String positionHorizontalRelative;
    private final String positionVerticalRelative;

    private ImageShapeStyle(float height, float width, boolean canUse, boolean absolutePosition, float marginLeft,
                            float marginTop, String positionHorizontal, String positionVertical,
                            String positionHorizontalRelative, String positionVerticalRelative) {
        this.height = height;
        this.width = width;
        this.canUse = canUse;
        this.absolutePosition = absolutePosition;
        this.marginLeft = marginLeft;
        this.marginTop = marginTop;
        this.positionHorizontal = positionHorizontal;
        this.positionVertical = positionVertical;
        this.positionHorizontalRelative = positionHorizontalRelative;
        this.positionVerticalRelative = positionVerticalRelative;
    }

    public float getHeight() {
        return height;
    }

    public float getWidth() {
        return width;
    }

    public boolean isCanUse() {
        return canUse;
    }

    /**
     * Returns true if the shape is a floating shape (<code>position:absolute</code>) which must be drawn outside of the
     * text flow, and false if the shape is an inline shape which takes place in the text flow.
     */
    public boolean isAbsolutePosition() {
        return absolutePosition;
    }

    /**
     * Returns the horizontal offset in points of the shape from its anchor (see
     * {@link #getPositionHorizontalRelative()}). A negative value means that the shape is drawn at the left of its
     * anchor.
     */
    public float getMarginLeft() {
        return marginLeft;
    }

    /**
     * Returns the vertical offset in points of the shape from its anchor (see {@link #getPositionVerticalRelative()}).
     * A negative value means that the shape is drawn above its anchor.
     */
    public float getMarginTop() {
        return marginTop;
    }

    /**
     * Returns the horizontal alignment of the shape (ex : <code>center</code>) or null when the shape is positioned
     * with {@link #getMarginLeft()}.
     */
    public String getPositionHorizontal() {
        return positionHorizontal;
    }

    /**
     * Returns the vertical alignment of the shape (ex : <code>center</code>) or null when the shape is positioned with
     * {@link #getMarginTop()}.
     */
    public String getPositionVertical() {
        return positionVertical;
    }

    /**
     * Returns what {@link #getMarginLeft()} is relative to (ex : <code>page</code>, <code>margin</code>,
     * <code>text</code>). Word defaults to <code>text</code> (ie the anchor of the shape) when the attribute is
     * missing.
     */
    public String getPositionHorizontalRelative() {
        return positionHorizontalRelative;
    }

    /**
     * Returns what {@link #getMarginTop()} is relative to (ex : <code>page</code>, <code>margin</code>,
     * <code>text</code>). Word defaults to <code>text</code> (ie the anchor of the shape) when the attribute is
     * missing.
     */
    public String getPositionVerticalRelative() {
        return positionVerticalRelative;
    }

    public static ImageShapeStyle parse(String style) {
        Map<String, String> properties = parseProperties(style);

        Float width = getLength(properties, WIDTH_TOKEN);
        Float height = getLength(properties, HEIGHT_TOKEN);
        Float marginLeft = getLength(properties, MARGIN_LEFT_TOKEN);
        Float marginTop = getLength(properties, MARGIN_TOP_TOKEN);

        boolean absolutePosition = ABSOLUTE_POSITION.equals(properties.get(POSITION_TOKEN));

        return new ImageShapeStyle(height != null ? height : DEFAULT_SIZE, width != null ? width : DEFAULT_SIZE,
                                   width != null && height != null, absolutePosition,
                                   marginLeft != null ? marginLeft : 0, marginTop != null ? marginTop : 0,
                                   properties.get(POSITION_HORIZONTAL_TOKEN), properties.get(POSITION_VERTICAL_TOKEN),
                                   properties.get(POSITION_HORIZONTAL_RELATIVE_TOKEN),
                                   properties.get(POSITION_VERTICAL_RELATIVE_TOKEN));
    }

    private static Map<String, String> parseProperties(String style) {
        Map<String, String> properties = new HashMap<String, String>();
        if (style == null) {
            return properties;
        }
        for (String token : style.split(STYLE_DELIMITER)) {
            int index = token.indexOf(PROPERTY_DELIMITER);
            if (index == -1) {
                continue;
            }
            String name = token.substring(0, index).trim().toLowerCase();
            String value = token.substring(index + PROPERTY_DELIMITER.length()).trim();
            if (name.length() > 0) {
                properties.put(name, value);
            }
        }
        return properties;
    }

    private static Float getLength(Map<String, String> properties, String name) {
        return parseLength(properties.get(name));
    }

    /**
     * Converts a VML length (ex : <code>595.5pt</code>, <code>-1.5in</code>) to points, or returns null when the value
     * is missing or cannot be read.
     */
    static Float parseLength(String value) {
        if (value == null) {
            return null;
        }
        value = value.trim().toLowerCase();
        if (value.length() == 0) {
            return null;
        }

        // Number of points of one unit, see VML "Length units". A value without unit is expressed in pixels.
        float unit = 72f / 96f;
        if (value.endsWith("pt")) {
            unit = 1f;
            value = value.substring(0, value.length() - 2);
        } else if (value.endsWith("in")) {
            unit = 72f;
            value = value.substring(0, value.length() - 2);
        } else if (value.endsWith("cm")) {
            unit = 72f / 2.54f;
            value = value.substring(0, value.length() - 2);
        } else if (value.endsWith("mm")) {
            unit = 72f / 25.4f;
            value = value.substring(0, value.length() - 2);
        } else if (value.endsWith("pc")) {
            unit = 12f;
            value = value.substring(0, value.length() - 2);
        } else if (value.endsWith("pi")) {
            unit = 12f;
            value = value.substring(0, value.length() - 2);
        } else if (value.endsWith("px")) {
            // VML pixels are 1/96 inch.
            unit = 72f / 96f;
            value = value.substring(0, value.length() - 2);
        } else if (value.endsWith("em") || value.endsWith("ex")) {
            // Relative units cannot be resolved here.
            return null;
        }

        try {
            return Float.valueOf(Float.parseFloat(value.trim()) * unit);
        } catch (NumberFormatException nfEx) {
            return null;
        }
    }
}
