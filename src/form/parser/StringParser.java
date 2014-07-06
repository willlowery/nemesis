
package form.parser;

import form.FormElementParser;
import form.ParseException;
import java.util.Locale;

/**
 *
 * @author william.lowery@rocky.edu
 */
public class StringParser implements FormElementParser<String>{

    @Override
    public String parse(String toParse, Locale locale)  throws ParseException{
        return toParse;
    }    
}
