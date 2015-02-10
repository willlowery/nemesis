
package nemesis.form.parser;

import java.util.Locale;
import nemesis.annotation.Description;
import nemesis.form.FormElementParser;
import nemesis.form.ParseException;

/**
 *
 * @author william.lowery@rocky.edu
 */
@Description("String Value")
public class StringParser implements FormElementParser<String>{

    @Override
    public String parse(String toParse, Locale locale)  throws ParseException{
        return toParse;
    }    
}
