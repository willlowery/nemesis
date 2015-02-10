
package nemesis.form.validator;

import nemesis.annotation.Description;
import nemesis.form.FormElementValidator;
import nemesis.form.ValidationException;
/*
 *
 * @author william.lowery@rocky.edu
 */
@Description("Value Required")
public class NotNullValidator implements FormElementValidator<Object>{

    @Override
    public void validate(String fieldName, Object t) {
        if(t == null){
            throw new ValidationException(fieldName, "The field was null");
        }
    }
    
}
