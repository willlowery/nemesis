package nemesis.response;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Method;
import nemesis.annotation.Attr;
import static org.hamcrest.CoreMatchers.is;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

public class XMLRendererTest {

    XMLRenderer renderer;
    ByteArrayOutputStream stream;

    @Before
    public void setup() {
        stream = new ByteArrayOutputStream();
        renderer = new XMLRenderer(stream);
    }

    @Test
    public void testRootNode() {
        renderer.enterObject("root", null);
        renderer.exitObject("root");

        assertThat(stream.toString(), is("<root></root>"));
    }

    @Test
    public void testRootNodeWithElement() {
        renderer.enterObject("root", null);
        renderer.enterMethod("element", "value", null);
        renderer.exitMethod("element");
        renderer.exitObject("root");

        assertThat(stream.toString(), is("<root><element>value</element></root>"));
    }

    @Test
    public void testRootNodeWithList() {
        renderer.enterObject("root", null);
        renderer.enterList("list", null);
        renderer.enterMethod("element", "value", null);
        renderer.exitMethod("element");
        renderer.exitList("list");
        renderer.exitObject("root");

        assertThat(stream.toString(), is("<root><list><element>value</element></list></root>"));
    }

    @Test
    public void testEscapeCharacters() {

        renderer.enterObject("root", null);
        renderer.enterMethod("element", "\"'<>&", null);
        renderer.exitMethod("element");
        renderer.exitObject("root");

        assertThat(stream.toString(), is("<root><element>&quot;&apos;&lt;&gt;&amp;</element></root>"));
    }
    

}
