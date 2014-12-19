package nemesis.form;

import java.util.List;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author william.lowery@rocky.edu
 */
public interface Form {

    public void map(HttpServletRequest request);

    public List<ValidationException> getValidationExceptions();
}
