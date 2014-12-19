
package form.validator;

import form.FormElementValidator;
import form.ValidationException;

/**
 *
 * @author william.lowery@rocky.edu
 */
public class NotNullValidator implements FormElementValidator<Object>{

    @Override
    public void validate(String fieldName, Object t) {
        if(t == null){
            throw new ValidationException(fieldName, "The field was null");
        }
    }
    
}
