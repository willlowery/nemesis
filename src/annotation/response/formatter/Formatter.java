
package annotation.response.formatter;

import java.lang.annotation.Annotation;

/**
 *
 * @author william.lowery@rocky.edu
 */
public interface Formatter {
    public Object format(Object toFormat, Annotation anno);
}
