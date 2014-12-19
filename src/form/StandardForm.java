package form;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author william.lowery@rocky.edu
 */
public class StandardForm implements Form {

    Map<String, FormElementParser> parsers;
    Map<String, FormElementValidator> elementValidators;
    Map<String, List<Object>> params;
    List<ValidationException> validationExceptions;
    List<FormValidator> formValidators;

    public StandardForm() {
        params = new HashMap<>();
        parsers = new HashMap<>();
        elementValidators = new HashMap<>();
        validationExceptions = new ArrayList<>();
        formValidators = new ArrayList<>();
    }

    protected void define(String field, FormElementParser parser) {
        parsers.put(field, parser);
    }

    protected void define(String field, FormElementParser parser, FormElementValidator validator) {
        define(field, parser);
        elementValidators.put(field, validator);
    }

    protected void define(FormValidator validator) {
        formValidators.add(validator);
    }

    @Override
    public void map(HttpServletRequest request) {
        fillForm(request);
        validateElements();
        validateForm();
    }

    void fillForm(HttpServletRequest request) {
        Map<String, String[]> parameterMap = request.getParameterMap();
        for (String key : parsers.keySet()) {
            List<Object> list = new ArrayList<>();
            String[] requestParams = parameterMap.get(key);
            if (requestParams == null) {
                continue;
            }
            for (String param : requestParams) {
                try {
                    list.add(parsers.get(key).parse(param, request.getLocale()));
                } catch (ParseException ex) {

                }
            }
            params.put(key, list);
        }
    }

    void validateElements() {
        for (String key : elementValidators.keySet()) {
            FormElementValidator validator = elementValidators.get(key);
            try {
                validator.validate(key, get(key));
            } catch (ValidationException ex) {
                validationExceptions.add(ex);
            }
        }
    }

    void validateForm() {
        for (FormValidator validator : formValidators) {
            try {
                validator.validate(this);
            } catch (ValidationException ex) {
                validationExceptions.add(ex);
            }
        }
    }

    protected <DesiredType> DesiredType get(String field) {
        List<Object> entry = params.get(field);
        if (entry == null) {
            return null;
        } else {
            return (DesiredType) entry.get(0);
        }

    }

    protected <DesiredType> DesiredType getValues(String field) {
        if (params.get(field) == null) {
            return (DesiredType) Collections.EMPTY_LIST;
        }
        return (DesiredType) params.get(field);
    }

    @Override
    public List<ValidationException> getValidationExceptions() {
        return validationExceptions;
    }

}
