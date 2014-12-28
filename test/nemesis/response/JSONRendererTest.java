package nemesis.response;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.List;
import nemesis.annotation.Element;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author william.lowery@rocky.edu
 */
public class JSONRendererTest {

    ByteArrayOutputStream stream = new ByteArrayOutputStream();
    JSONRenderer renderer;

    @Before
    public void setup() {
        renderer = new JSONRenderer(stream);
    }

    @Test
    public void testEmpty_ReturnsEmptyString() {
        assertThat(stream.toString(), is(""));
    }

    @Test
    public void testEnterExit_ReturnsEmptyObject() {
        renderer.enterObject("response", null);
        renderer.exitObject("response");
        assertThat(stream.toString(), is("{\"@object\": \"response\"}"));
    }

    @Test
    public void testEnterSingle_ReturnsSingle() throws NoSuchMethodException {
        renderer.enterObject("response", null);

        renderer.enterMethod("field", "value", Class.class.getMethod("str"));
        renderer.exitMethod("field");

        renderer.exitObject("response");
        assertThat(stream.toString(), is("{\"@object\": \"response\", \"field\": \"value\"}"));
    }

    @Test
    public void testMultiFields() throws NoSuchMethodException {
        renderer.enterObject("response", null);

        renderer.enterMethod("field", "value", Class.class.getMethod("str"));
        renderer.exitMethod("field");

        renderer.enterMethod("fieldA", "value", Class.class.getMethod("str"));
        renderer.exitMethod("fieldA");

        renderer.exitObject("response");
        assertThat(stream.toString(), is("{\"@object\": \"response\", \"field\": \"value\", \"fieldA\": \"value\"}"));
    }

    @Test
    public void testNestedObjects() throws NoSuchMethodException {
        renderer.enterObject("response", null);

        renderer.enterMethod("A", renderer, Class.class.getMethod("a"));

        renderer.enterObject("InnerObject", renderer);

        renderer.enterMethod("Inner", "hello", Class.class.getMethod("str"));
        renderer.exitMethod("Inner");

        renderer.exitObject("InnerObject");

        renderer.enterMethod("A", renderer, Class.class.getMethod("a"));

        renderer.enterObject("InnerObject", renderer);

        renderer.enterMethod("I", "h", Class.class.getMethod("str"));
        renderer.exitMethod("I");

        renderer.exitObject("InnerObject");

        renderer.exitMethod("A");

        renderer.exitObject("response");
        assertThat(stream.toString(), is("{\"@object\": \"response\", \"A\": {\"@object\": \"InnerObject\", \"Inner\": \"hello\"}, \"A\": {\"@object\": \"InnerObject\", \"I\": \"h\"}}"));
    }

    @Test
    public void testList() throws NoSuchMethodException {
        renderer.enterObject("response", null);

        renderer.enterList("List", Class.class.getMethod("list"));
        renderer.enterMethod("List", "A", Class.class.getMethod("list"));
        renderer.enterMethod("List", "A", Class.class.getMethod("list"));
        renderer.exitList("List");

        renderer.exitObject("response");
        assertThat(stream.toString(), is("{\"@object\": \"response\", \"List\": [\"A\", \"A\"]}"));
    }

    @Test
    public void testListoObject() throws NoSuchMethodException {
        renderer.enterObject("response", null);

        renderer.enterList("List", Class.class.getMethod("list"));

        renderer.enterObject("OO", renderer);
        renderer.enterMethod("inner", "A", Class.class.getMethod("str"));
        renderer.exitObject("OO");

        renderer.enterObject("OO", renderer);
        renderer.enterMethod("inner", "A", Class.class.getMethod("str"));
        renderer.exitObject("OO");

        renderer.exitList("List");
        
        renderer.enterMethod("apple", "A",  Class.class.getMethod("str"));
        renderer.exitMethod("apple");

        renderer.exitObject("response");
        assertThat(stream.toString(), is("{\"@object\": \"response\", \"List\": [{\"@object\": \"OO\", \"inner\": \"A\"}, {\"@object\": \"OO\", \"inner\": \"A\"}], \"apple\": \"A\"}"));
    }

    @Test
    public void testFormatDispatch_ReturnsFormattedObject() {
        renderer.enterObject("response", null);
        renderer.exitObject("response");
        assertThat(stream.toString(), is("{\"@object\": \"response\"}"));
    }
    
    @Test
    public void testEscapeCharactersAreEscaped() throws NoSuchMethodException{
        renderer.enterObject("response", null);

        renderer.enterMethod("field", "\"\n\r\t ", Class.class.getMethod("str"));
        renderer.exitMethod("field");

        renderer.exitObject("response");
        assertThat(stream.toString(), is("{\"@object\": \"response\", \"field\": \"\\\"\\\n\\\r\\\t \"}"));
    }
    
    @Element("a")
    public class Class {

        @Element("aaa")
        public Class a() {
            return new Class();
        }

        @Element("str")
        public String str() {
            return "";
        }

        @Element("list")
        public List list() {
            return Arrays.asList("a", "b", "c");
        }

    }
}
