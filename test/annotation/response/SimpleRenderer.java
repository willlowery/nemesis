
package annotation.response;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author william.lowery@rocky.edu
 */
public class SimpleRenderer implements Renderer {
    List<Event> history = new ArrayList<>();

    public List<Event> getHistory() {
        return history;
    }

    @Override
    public void enterElement(String elementName, Object returned, Method method) {
        history.add(new EnterEvent(elementName, returned, method));
    }  

    @Override
    public void enterElement(String elementName, Object returned) {
         history.add(new EnterEvent(elementName, returned, null));
    }

    @Override
    public void exitElement(String name) {
        history.add(new ExitEvent(name, null));
    }

    public static class Event {

        String element;
        Method method;

        public Event(String element, Method method) {
            this.element = element;
            this.method = method;
        }
    }

    public static class ExitEvent extends Event {

        public ExitEvent(String element, Method method) {
            super(element, method);
        }
    }

    public static class EnterEvent extends Event {

        Object returned;

        public EnterEvent(String element, Object returned, Method method) {
            super(element, method);
            this.returned = returned;
        }
    }
    
}
