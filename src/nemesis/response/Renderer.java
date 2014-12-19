package nemesis.response;

import java.lang.reflect.Method;

/**
 *
 * @author william.lowery@rocky.edu
 */
public interface Renderer {

    public void enterMethod(String name, Object returned, Method method);

    public void enterObject(String name, Object returned);
    
    public void enterList(String name, Method method);
    
    public void exitList(String name);

    public void exitMethod(String name);

    public void exitObject(String name);

}
