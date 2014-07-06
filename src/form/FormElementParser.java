
package form;

import java.util.Locale;

/**
 *
 * @author william.lowery@rocky.edu
 */
public interface FormElementParser<T> {
   
    public T parse(String toParse, Locale locale) throws ParseException;
    
}
