package nemesis.form;

import nemesis.annotation.Element;

/**
 *
 * @author william.lowery@rocky.edu
 */
@Element("ParseException")
public class ParseException extends Exception{

    public ParseException() {
    }

    public ParseException(RuntimeException e) {
        super(e);
    }
    
    @Element("message")
    public String getMsg(){
        return getMessage();
    }
    
}
