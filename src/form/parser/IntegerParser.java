
package form.parser;

import form.FormElementParser;
import form.ParseException;
import java.util.Locale;

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
