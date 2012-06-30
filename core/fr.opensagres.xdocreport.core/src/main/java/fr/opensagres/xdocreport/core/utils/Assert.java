package fr.opensagres.xdocreport.core.utils;


/**
 * @author pascalleclercq
 * Inpired from org.springframework.util.Assert
 */
public abstract class Assert {

	/**
	 * Assert that an object is not <code>null</code> .
	 * <pre class="code">Assert.notNull(clazz, "The class must not be null");</pre>
	 * @param object the object to check
	 * @param message the exception message to use if the assertion fails
	 * @throws IllegalArgumentException if the object is <code>null</code>
	 */
	public static void notNull(Object object, String message) {
		if (object == null) {
			throw new IllegalArgumentException(message);
		}
	}
}
