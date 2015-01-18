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
/**
 * 
 */
package fr.opensagres.xdocreport.template.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import fr.opensagres.xdocreport.template.formatter.NullImageBehaviour;

/**
 * This annotation should be attached to getter method which is called from Velocity or Freemarker Otherwise it will
 * have no effect
 * 
 * @author rcusnir
 */
@Target( value = { ElementType.METHOD } )
@Retention( value = RetentionPolicy.RUNTIME )
public @interface FieldMetadata
{

    /**
     * Returns the syntax kind to use for the field and empty otherwise.
     * 
     * @return the syntax kind to use for the field and empty otherwise.
     */
    public String syntaxKind() default "";

    /**
     * Returns true if field which used syntax kind can have template syntax and false otherwise.
     * 
     * @return true if field which used syntax kind can have template syntax and false otherwise.
     */
    public boolean syntaxWithDirective() default false;

    /**
     * Returns the description of the field.
     * 
     * @return the description of the field.
     */
    public String description() default "";

    /**
     * Returns the image name if field must use mapping name with image and empty otherwise.
     * 
     * @return the image name if field must use mapping name with image and empty otherwise.
     */
    //public String imageName() default "";

    public ImageMetadata[] images() default {};
    
    /**
     * Returns the null image behaviour {@link NullImageBehaviour}.
     * 
     * @return the null image behaviour {@link NullImageBehaviour}.
     */
    //public NullImageBehaviour imageBehaviour() default NullImageBehaviour.ThrowsError;
}
