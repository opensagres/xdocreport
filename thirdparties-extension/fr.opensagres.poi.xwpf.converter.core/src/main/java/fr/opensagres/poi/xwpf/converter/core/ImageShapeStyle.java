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

/**
 * This ImageShapeStyle is read style of Picture Shape. Used for VML Picture Shape.
 */
public class ImageShapeStyle {
    private static final String STYLE_DELIMITER = ";";
    private static final String WIDTH_TOKEN = "width:";
    private static final String HEIGHT_TOKEN = "height:";

    private float height;
    private float width;
    private boolean canUse;

    private ImageShapeStyle(float height, float width, boolean canUse) {
        this.height = height;
        this.width = width;
        this.canUse = canUse;
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

    public static ImageShapeStyle parse(String style) {
        float height = 10;
        float width = 10;
        String[] tokens = style.split(STYLE_DELIMITER);
        int count = 0;
        for (String token : tokens) {
            token = token.trim();
            if (token.startsWith(WIDTH_TOKEN)) {
                token = token.substring(WIDTH_TOKEN.length());
                token = token.substring(0, token.length() - 2);
                try {
                    width = Float.parseFloat(token);
                    count++;
                } catch (NumberFormatException nfEx) {

                }
            } else if (token.startsWith(HEIGHT_TOKEN)) {
                token = token.substring(HEIGHT_TOKEN.length());
                token = token.substring(0, token.length() - 2);

                try {
                    height = Float.parseFloat(token);
                    count++;
                } catch (NumberFormatException nfEx) {

                }
            }
        }
        return new ImageShapeStyle(height, width, count == 2);
    }
}
