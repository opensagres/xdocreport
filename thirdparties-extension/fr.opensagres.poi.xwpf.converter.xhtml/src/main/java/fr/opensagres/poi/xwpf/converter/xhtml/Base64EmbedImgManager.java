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
package fr.opensagres.poi.xwpf.converter.xhtml;

import fr.opensagres.poi.xwpf.converter.core.ImageManager;
import fr.opensagres.xdocreport.core.utils.Base64Utility;

import java.io.File;
import java.io.IOException;

/**
 * Created by zzt on 17/4/11.
 */
public class Base64EmbedImgManager extends ImageManager {
    private static final String EMBED_IMG_SRC_PREFIX = XHTMLConstants.DATA_ATTR + ";base64,";

    private byte[] picture;

    public Base64EmbedImgManager() {
        super(new File(""), "");
    }

    @Override
    public void extract(String imagePath, byte[] imageData) throws IOException {
        this.picture = imageData;
    }

    @Override
    public String resolve(String uri) {
        StringBuilder sb = new StringBuilder(picture.length + EMBED_IMG_SRC_PREFIX.length())
                .append(EMBED_IMG_SRC_PREFIX)
                .append(Base64Utility.encode(picture));
        return sb.toString();
    }
}
