package org.helios.netty.ajax.handlergroups;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>Title: URIHandler</p>
 * <p>Description: </p> 
 * <p><code>org.helios.netty.ajax.handlergroups.URIHandler</code></p>
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface URIHandler {
	/**
	 * Returns the simple URIs that the annotated handler will handle
	 * @return the simple URIs that the annotated handler will handle
	 */
	public String[] uri();
}
