
package form.validator;

import form.ValidationException;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author william.lowery@rocky.edu
 */
public class NotNullValidatorTest {
    
    NotNullValidator validator;
    
    @Before
    public void setup(){
        validator = new NotNullValidator();
    }
    
    @Test(expected = ValidationException.class)
    public void testNull_ThrowsException(){
        validator.validate("", null);
    }
    
    @Test
    public void TestNonNull_FallsThrough(){
        validator.validate("", new Object());
    }
}
