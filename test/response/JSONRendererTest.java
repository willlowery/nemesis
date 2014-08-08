package annotation.response;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import static org.hamcrest.CoreMatchers.is;
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
        renderer.enterElement("response", null);
        renderer.exitObject("response");
        assertThat(stream.toString(), is("{}"));
    }

    @Test
    public void testEnterSingle_ReturnsSingle() throws NoSuchMethodException {
        renderer.enterElement("response", null);

        renderer.enterElement("field", "value", Class.class.getMethod("str"));
        renderer.exitMethod("field");

        renderer.exitObject("response");
        assertThat(stream.toString(), is("{\"field\": \"value\"}"));
    }

    @Test
    public void testMultiFields() throws NoSuchMethodException {
        renderer.enterElement("response", null);

        renderer.enterElement("field", "value", Class.class.getMethod("str"));
        renderer.exitMethod("field");

        renderer.enterElement("fieldA", "value", Class.class.getMethod("str"));
        renderer.exitMethod("fieldA");

        renderer.exitObject("response");
        assertThat(stream.toString(), is("{\"field\": \"value\", \"fieldA\": \"value\"}"));
    }

    @Test
    public void testNestedObjects() throws NoSuchMethodException {
        renderer.enterElement("response", null);

        renderer.enterElement("A", renderer, Class.class.getMethod("a"));

        renderer.enterElement("InnerObject", renderer);

        renderer.enterElement("Inner", "hello", Class.class.getMethod("str"));
        renderer.exitMethod("Inner");

        renderer.exitObject("InnerObject");

        renderer.enterElement("A", renderer, Class.class.getMethod("a"));

        renderer.enterElement("InnerObject", renderer);

        renderer.enterElement("I", "h", Class.class.getMethod("str"));
        renderer.exitMethod("I");

        renderer.exitObject("InnerObject");

        renderer.exitMethod("A");

        renderer.exitObject("response");
        assertThat(stream.toString(), is("{\"A\": {\"Inner\": \"hello\"}, \"A\": {\"I\": \"h\"}}"));
    }

    @Test
    public void testList() throws NoSuchMethodException {
        renderer.enterElement("response", null);

        renderer.enterList("List", Class.class.getMethod("list"));
        renderer.enterElement("List", "A", Class.class.getMethod("list"));
        renderer.enterElement("List", "A", Class.class.getMethod("list"));
        renderer.exitList("List");

        renderer.exitObject("response");
        assertThat(stream.toString(), is("{\"List\": [\"A\", \"A\"]}"));
    }

    @Test
    public void testListoObject() throws NoSuchMethodException {
        renderer.enterElement("response", null);

        renderer.enterList("List", Class.class.getMethod("list"));

        renderer.enterElement("OO", renderer);
        renderer.enterElement("inner", "A", Class.class.getMethod("str"));
        renderer.exitObject("OO");

        renderer.enterElement("OO", renderer);
        renderer.enterElement("inner", "A", Class.class.getMethod("str"));
        renderer.exitObject("OO");

        renderer.exitList("List");
        
        renderer.enterElement("apple", "A",  Class.class.getMethod("str"));
        renderer.exitMethod("apple");

        renderer.exitObject("response");
        assertThat(stream.toString(), is("{\"List\": [{\"inner\": \"A\"}, {\"inner\": \"A\"}], \"apple\": \"A\"}"));
    }

    @Test
    public void testFormatDispatch_ReturnsFormattedObject() {
        renderer.enterElement("response", null);
        renderer.exitObject("response");
        assertThat(stream.toString(), is("{}"));
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
        @Listing(String.class)
        public List list() {
            return Arrays.asList("a", "b", "c");
        }

    }
}
