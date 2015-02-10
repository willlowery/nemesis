
package nemesis.form;

import nemesis.annotation.Element;

/**
 *
 * @author william.lowery@rocky.edu
 */
@Element("ValidationException")
public class ValidationException extends RuntimeException{
    private String fieldName;

    public ValidationException() {
    }
    
    public ValidationException(String fieldName) {
        this.fieldName = fieldName;
    }

    public ValidationException(String fieldName, String message) {
        super(message);
        this.fieldName = fieldName;
    }
    
    @Element("field")
    public String getFieldName(){
        return fieldName;
    }        
    
    @Element("message")
    public String getMessage(){
        return super.getMessage();
    }
    
}
