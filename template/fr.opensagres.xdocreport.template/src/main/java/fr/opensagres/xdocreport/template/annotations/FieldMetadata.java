/**
 * 
 */
package fr.opensagres.xdocreport.template.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation should be attached to getter method which is called from Velocity or Freemarker
 * Otherwise it will have no effect
 * @author rcusnir
 *
 */
@Target(value={ElementType.METHOD})
@Retention(value = RetentionPolicy.RUNTIME)
public @interface FieldMetadata {
	public String syntaxKind();
	public boolean syntaxWithDirective() default false;
	public String description() default "";
}
