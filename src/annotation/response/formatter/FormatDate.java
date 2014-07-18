
package annotation.response.formatter;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 *
 * @author william.lowery@rocky.edu
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface FormatDate{
    public String value();
}
