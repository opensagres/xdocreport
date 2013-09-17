package fr.opensagres.xdocreport.template.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import fr.opensagres.xdocreport.template.formatter.NullImageBehaviour;

@Target( value = { ElementType.METHOD } )
@Retention( value = RetentionPolicy.RUNTIME )
public @interface ImageMetadata
{
    /**
     * Returns the image name if field must use mapping name with image and empty otherwise.
     * 
     * @return the image name if field must use mapping name with image and empty otherwise.
     */
    public String name() default "";

    /**
     * Returns the null image behaviour {@link NullImageBehaviour}.
     * 
     * @return the null image behaviour {@link NullImageBehaviour}.
     */
    public NullImageBehaviour behaviour() default NullImageBehaviour.ThrowsError;

}
