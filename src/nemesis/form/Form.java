package nemesis.form;

import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import nemesis.annotation.Element;

/**
 *
 * @author william.lowery@rocky.edu
 */
@Element("Form")
public interface Form {

    public void map(HttpServletRequest request);

    @Element("validation-errors")
    public List<ValidationException> getValidationExceptions();
    
    Map<String, FormElementValidator> getElementValidators();

    List<FormValidator> getFormValidators();

    @Element("parse-errors")
    List<ParseException> getParseExceptions();

    Map<String, FormElementParser> getParsers();
}
