package form;

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

    public StandardFormTest() {
        define("field", new StringParser());
        define("int-field", new IntegerParser());
    }

    @Test
    public void testGetOnEmptyRequest_ReturnsNull() {
        HashMap<String, String[]> parameters = new HashMap<>();
        map(new FakeRequest(parameters));
        assertNull( get("field") );
    }

    @Test
    public void testGet_ReturnsValue() {
        HashMap<String, String[]> parameters = new HashMap<>();
        addEntry(parameters, "field", "value");
        addEntry(parameters, "int-field", "1");
        
        map(new FakeRequest(parameters));
        
        
        assertThat((String) get("field"), is("value"));
        assertThat((Integer) get("int-field"), is(1));
    }
    
    @Test
    public void testGetValues_ReturnsEmptyList(){
        map(new FakeRequest(new HashMap<String, String[]>()));        
        assertThat((List) getValues("field"), is(Collections.EMPTY_LIST));
    }
    
    @Test
    public void testGetValues_ReturnsValues(){
        HashMap<String, String[]> parameters = new HashMap<>();
        addEntry(parameters, "field", "value", "value 2");
        addEntry(parameters, "int-field", "1", "2");
        
        map(new FakeRequest(parameters));        
        
        assertThat((List) getValues("field"), is(Arrays.asList("value", "value 2")));
        assertThat((List) getValues("int-field"), is(Arrays.asList(1,2)));
    }
    
    


    private void addEntry(Map<String, String[]> map, String key, String... values) {
        map.put(key, values);
    }

    public static class FakeRequest extends DummyRequest {

        Map<String, String[]> parameters;

        public FakeRequest(Map<String, String[]> parameters) {
            this.parameters = parameters;
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

}
