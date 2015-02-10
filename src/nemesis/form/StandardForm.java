package nemesis.form;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import p.URLEncodedParser;

/**
 *
 * @author william.lowery@rocky.edu
 */
public class StandardForm implements Form {

    public static final int MAX_LENGTH = Integer.MAX_VALUE;
    Map<String, FormElementParser> parsers;
    Map<String, FormElementValidator> elementValidators;
    Map<String, List<Object>> params;
    List<ValidationException> validationExceptions;
    List<ParseException> parseExceptions;
    List<FormValidator> formValidators;

    public StandardForm() {
        params = new HashMap<>();
        parsers = new HashMap<>();
        elementValidators = new HashMap<>();
        validationExceptions = new ArrayList<>();
        formValidators = new ArrayList<>();
        parseExceptions = new ArrayList<>();
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

        Map<String, List<String>> paramMap = new HashMap<>();

        request.getParameterMap().forEach((k, v) -> {
            paramMap.put((String) k, Arrays.asList((String[]) v));
        });
        URLEncodedParser parser = new URLEncodedParser();
        try {
            parser.parse(request.getInputStream(), MAX_LENGTH).forEach((k, v) -> {
                paramMap.put(k, v);
            });
        } catch (IOException ex) {
        }

        parsers.keySet().forEach((key) -> {
            List<Object> list = new ArrayList<>();
            List<String> requestParams = paramMap.get(key);
            if (requestParams != null) {
                requestParams.forEach((s) -> {
                    try {
                        list.add(parsers.get(key).parse(s, request.getLocale()));
                    } catch (ParseException ex) {
                        parseExceptions.add(ex);
                    }
                });
                params.put(key, list);
            }
        });
    }

    void validateElements() {
        elementValidators.keySet().forEach((key) -> {
            try {
                elementValidators.get(key).validate(key, get(key));
            } catch (ValidationException ex) {
                validationExceptions.add(ex);
            }
        });
    }

    void validateForm() {
        formValidators.forEach((validator) -> {
            try {
                validator.validate(this);
            } catch (ValidationException ex) {
                validationExceptions.add(ex);
            }
        });
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

    public List<ValidationException> getValidationExceptions() {
        return validationExceptions;
    }

    public List<ParseException> getParseExceptions() {
        return parseExceptions;
    }

    public Map<String, FormElementValidator> getElementValidators() {
        return elementValidators;
    }

    public Map<String, FormElementParser> getParsers() {
        return parsers;
    }

    public List<FormValidator> getFormValidators() {
        return formValidators;
    }

    public boolean isValid() {
        return parseExceptions.isEmpty() && validationExceptions.isEmpty();
    }

}
