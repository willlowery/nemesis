package form;

import form.validator.NotNullValidator;
import form.parser.IntegerParser;
import form.parser.StringParser;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author william.lowery@rocky.edu
 */
public class StandardFormTest extends StandardForm {

    FakeRequest request;

    public StandardFormTest() {
        request = new FakeRequest();
        define("field", new StringParser());
        define("int-field", new IntegerParser());
    }

    @Test
    public void testGetOnEmptyRequest_ReturnsNull() {
        map(request);
        assertNull(get("field"));
    }

    @Test
    public void testNonMapParamtersAreIgnored() {
        request.addEntry("apple", "gala");
        map(request);
        assertNull(get("apple"));
    }

    @Test
    public void testGet_ReturnsValue() {
        request.addEntry("field", "value");
        request.addEntry("int-field", "1");
        map(request);

        assertThat((String) get("field"), is("value"));
        assertThat((Integer) get("int-field"), is(1));
    }

    @Test
    public void testGetValues_ReturnsEmptyList() {
        map(request);
        assertThat((List) getValues("field"), is(Collections.EMPTY_LIST));
    }

    @Test
    public void testGetValues_ReturnsValues() {
        request.addEntry("field", "value", "value 2");
        request.addEntry("int-field", "1", "2");
        
        map(request);

        assertThat((List) getValues("field"), is(Arrays.asList("value", "value 2")));
        assertThat((List) getValues("int-field"), is(Arrays.asList(1, 2)));
    }

    @Test
    public void testValidators_RegisterExceptions() {
        TestForm form = new TestForm();
        form.map(request);
        assertThat(form.getValidationExceptions().isEmpty(), is(false));
    }

    @Test
    public void testFormLevelValidator_invalidStateGeneratesException() {
        FormWithValidator form = new FormWithValidator();
        request.addEntry("a", "90");
        request.addEntry("b", "100");
        form.map(request);
        assertThat(form.getValidationExceptions().isEmpty(), is(false));
    }

    public static class FakeRequest extends DummyRequest {

        Map<String, String[]> parameters;

        public FakeRequest() {
            this.parameters = new HashMap<>();
        }

        public void addEntry(String key, String... values) {
            parameters.put(key, values);
        }

        @Override
        public Map getParameterMap() {
            return parameters;
        }

        @Override
        public Locale getLocale() {
            return Locale.US;
        }

    }

    public static class TestForm extends StandardForm {

        public TestForm() {
            define("validate", new StringParser(), new NotNullValidator());
        }

    }

    public static class FormWithValidator extends StandardForm {

        public FormWithValidator() {
            define("a", new IntegerParser());
            define("b", new IntegerParser());
            define(new AGreaterThanB());
        }

        public Integer getA() {
            return get("a");
        }

        public Integer getB() {
            return get("b");
        }

        public static class AGreaterThanB implements FormValidator<FormWithValidator> {

            @Override
            public void validate(FormWithValidator form) {
                if (form.getA() <= form.getB()) {
                    throw new ValidationException();
                }
            }

        }
    }

}
