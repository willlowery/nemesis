package nemesis.response;

import java.util.Arrays;
import java.util.List;
import nemesis.annotation.Element;
import nemesis.form.ParseException;
import nemesis.form.StandardForm;
import nemesis.form.ValidationException;
import nemesis.response.SimpleRenderer.EnterEvent;
import nemesis.response.SimpleRenderer.ExitEvent;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author william.lowery@rocky.edu
 */
public class ResponseWalkerTest {

    @Test()
    public void testSimple() {
        SimpleRenderer renderer = new SimpleRenderer();
        ResponseWalker walker = new ResponseWalker(renderer);
        walker.walk(new SimpleResponse());
    }

    @Test
    public void testInterfaceRoot() {
        SimpleRenderer renderer = new SimpleRenderer();
        ResponseWalker walker = new ResponseWalker(renderer);
        walker.walk(new I() {
        });
        List<SimpleRenderer.Event> history = renderer.getHistory();
        assertThat(history.get(0), instanceOf(EnterEvent.class));
        assertThat(history.get(1), instanceOf(ExitEvent.class));
    }

    @Test
    public void testSingleElementResponse() {
        SimpleRenderer renderer = new SimpleRenderer();
        ResponseWalker walker = new ResponseWalker(renderer);
        walker.walk(new SingleElement());
        List<SimpleRenderer.Event> history = renderer.getHistory();
        assertThat(history.get(0), instanceOf(EnterEvent.class));

        assertThat(history.get(1), instanceOf(EnterEvent.class));
        assertThat(history.get(2), instanceOf(ExitEvent.class));

        assertThat(history.get(3), instanceOf(ExitEvent.class));
    }

    @Test
    public void testSingleCollectionResponse() {
        SimpleRenderer renderer = new SimpleRenderer();
        ResponseWalker walker = new ResponseWalker(renderer);
        walker.walk(new SingleCollection());
        List<SimpleRenderer.Event> history = renderer.getHistory();
        assertThat(history.get(0), instanceOf(EnterEvent.class));
        assertThat(history.get(1), instanceOf(EnterEvent.class));

        assertThat(history.get(2), instanceOf(EnterEvent.class));
        assertThat(history.get(3), instanceOf(ExitEvent.class));

        assertThat(history.get(4), instanceOf(EnterEvent.class));
        assertThat(history.get(5), instanceOf(ExitEvent.class));

        assertThat(history.get(6), instanceOf(EnterEvent.class));
        assertThat(history.get(7), instanceOf(ExitEvent.class));

        assertThat(history.get(8), instanceOf(ExitEvent.class));
        assertThat(history.get(9), instanceOf(ExitEvent.class));
    }

    @Test
    public void testSingleArrayResponse() {
        SimpleRenderer renderer = new SimpleRenderer();
        ResponseWalker walker = new ResponseWalker(renderer);
        walker.walk(new SingleArray());
        List<SimpleRenderer.Event> history = renderer.getHistory();
        assertThat(history.get(0), instanceOf(EnterEvent.class));
        assertThat(history.get(1), instanceOf(EnterEvent.class));

        assertThat(history.get(2), instanceOf(EnterEvent.class));
        assertThat(history.get(3), instanceOf(ExitEvent.class));

        assertThat(history.get(4), instanceOf(EnterEvent.class));
        assertThat(history.get(5), instanceOf(ExitEvent.class));

        assertThat(history.get(6), instanceOf(EnterEvent.class));
        assertThat(history.get(7), instanceOf(ExitEvent.class));

        assertThat(history.get(8), instanceOf(ExitEvent.class));
        assertThat(history.get(9), instanceOf(ExitEvent.class));
    }

    @Test
    public void testSingleCollectionOfResponses() {
        SimpleRenderer renderer = new SimpleRenderer();
        ResponseWalker walker = new ResponseWalker(renderer);
        walker.walk(new SingleCollectionOfResponses());
        List<SimpleRenderer.Event> history = renderer.getHistory();
        assertThat(history.get(0), instanceOf(EnterEvent.class));
        assertThat(history.get(1), instanceOf(EnterEvent.class));

        assertThat(history.get(2), instanceOf(EnterEvent.class));
        assertThat(history.get(3), instanceOf(EnterEvent.class));
        assertThat(history.get(4), instanceOf(ExitEvent.class));
        assertThat(history.get(5), instanceOf(ExitEvent.class));

        assertThat(history.get(6), instanceOf(EnterEvent.class));
        assertThat(history.get(7), instanceOf(EnterEvent.class));
        assertThat(history.get(8), instanceOf(ExitEvent.class));
        assertThat(history.get(9), instanceOf(ExitEvent.class));

        assertThat(history.get(10), instanceOf(EnterEvent.class));
        assertThat(history.get(11), instanceOf(EnterEvent.class));
        assertThat(history.get(12), instanceOf(ExitEvent.class));
        assertThat(history.get(13), instanceOf(ExitEvent.class));

        assertThat(history.get(14), instanceOf(ExitEvent.class));
        assertThat(history.get(15), instanceOf(ExitEvent.class));
    }

    @Test
    public void testNestedResponse() {
        SimpleRenderer renderer = new SimpleRenderer();
        ResponseWalker walker = new ResponseWalker(renderer);
        walker.walk(new NestedResponse());
        List<SimpleRenderer.Event> history = renderer.getHistory();

        assertThat(history.get(0), instanceOf(EnterEvent.class));
        assertThat(history.get(1), instanceOf(EnterEvent.class));

        assertThat(history.get(2), instanceOf(EnterEvent.class));
        assertThat(history.get(3), instanceOf(EnterEvent.class));

        assertThat(history.get(4), instanceOf(ExitEvent.class));
        assertThat(history.get(5), instanceOf(ExitEvent.class));

        assertThat(history.get(6), instanceOf(ExitEvent.class));
        assertThat(history.get(7), instanceOf(ExitEvent.class));
    }
    
    public static class SimpleResponse extends StandardForm{
    }        

    @Element("SingleElement")
    public static class SingleElement {

        @Element("Element")
        public String getElement() {
            return "value";
        }
    }

    @Element("Nested")
    public static class NestedResponse {

        @Element("inner")
        public SingleElement getElement() {
            return new SingleElement();
        }
    }

    @Element("SingleCollection")
    public static class SingleCollection {

        @Element("Element")
        public List<String> getElement() {
            return Arrays.asList("value", "value", "value");
        }
    }

    @Element("SingleArray")
    public static class SingleArray {

        @Element("Element")
        public String[] getElement() {
            return new String[]{"value", "value", "value"};
        }
    }

    @Element("SingleCollectionOfResponses")
    public static class SingleCollectionOfResponses {

        @Element("Element")
        public List<SingleElement> getElement() {
            return Arrays.asList(new SingleElement(), new SingleElement(), new SingleElement());
        }
    }

    @Element("thing")
    public static interface I {
    }

}
