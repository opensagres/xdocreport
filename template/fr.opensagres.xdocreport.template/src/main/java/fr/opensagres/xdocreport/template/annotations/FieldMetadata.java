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
