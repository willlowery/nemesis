package annotation.response;

import java.lang.reflect.Method;

/**
 *
 * @author william.lowery@rocky.edu
 */
public interface Renderer {

    public void enterElement(String elementName, Object returned, Method method);

    public void enterElement(String elementName, Object returned);

    public void exitMethod(String name);

    public void exitObject(String name);

}
