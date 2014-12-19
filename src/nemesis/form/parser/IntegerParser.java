
package nemesis.form.parser;

import java.util.Locale;
import nemesis.form.FormElementParser;
import nemesis.form.ParseException;

/**
 *
 * @author william.lowery@rocky.edu
 */
public class IntegerParser implements FormElementParser<Integer>{

    @Override
    public Integer parse(String toParse, Locale locale) throws ParseException{
        try{
            return Integer.valueOf(toParse);
        }catch(NumberFormatException e){
            throw new ParseException();
        }
        
    }
    
}
