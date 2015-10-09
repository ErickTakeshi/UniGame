
package annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


@Retention(RetentionPolicy.RUNTIME)
public @interface ServerAnnotation {
	int porta();
	String ip() default "";
	
}
