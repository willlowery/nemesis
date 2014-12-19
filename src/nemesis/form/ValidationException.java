
package nemesis.form;

/**
 *
 * @author william.lowery@rocky.edu
 */
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
    
    public String getFieldName(){
        return fieldName;
    }        
    
}
