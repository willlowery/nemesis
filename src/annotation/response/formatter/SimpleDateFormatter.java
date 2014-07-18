package annotation.response.formatter;

import java.lang.annotation.Annotation;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author william.lowery@rocky.edu
 */
public class SimpleDateFormatter implements Formatter {

    @Override
    public Object format(Object toFormat, Annotation anno) {
        if (anno instanceof FormatDate && toFormat instanceof Date) {
            try {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(((FormatDate) anno).value());
                return simpleDateFormat.format(toFormat);
            } catch (IllegalArgumentException ex) {
            }
        }
        return toFormat;
    }
}
