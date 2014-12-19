package annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 *
 * @author william.lowery@rocky.edu
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Element {
    String value();
}
