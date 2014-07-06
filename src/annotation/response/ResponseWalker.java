package annotation.response;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author william.lowery@rocky.edu
 */
public class ResponseWalker {

    Renderer[] renderers;

    public ResponseWalker(Renderer... renderers) {
        this.renderers = renderers;
    }

    public void walk(Object response) {
        this.walk(response, response.getClass());
    }

    private void walk(Object response, Class<?> toWalk) {
        visit(getElement(toWalk), response);
        List<Method> elementsToWalk = getElementsToWalk(toWalk);
        for (Method method : elementsToWalk) {
            try {
                Object returned = method.invoke(response);
                visit(getElementName(method), returned, method);
                if (isA(method.getReturnType(), Collection.class)) {
                    if (method.getAnnotation(Listing.class).value().isAnnotationPresent(Element.class)) {
                        for (Object o : (Collection) returned) {                            
                            walk(o, method.getAnnotation(Listing.class).value());
                        }                            
                    } else {
                        for (Object o : (Collection) returned) {
                            visit(getElementName(method), o, method);
                        }
                    }
                }
                leave(getElementName(method));
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {

            }
        }
        leave(getElement(toWalk));
    }

    private boolean isA(Class<?> a, Class<?> b) {
        return b.isAssignableFrom(a);
    }

    private String getElement(Class<?> toWalk) {
        if (toWalk.isAnnotationPresent(Element.class)) {
            return toWalk.getAnnotation(Element.class).value();
        } else {
            throw new ResponseNotAnnotatedException();
        }
    }

    private String getElementName(Method method) {
        return method.getAnnotation(Element.class).value();
    }

    private List<Method> getElementsToWalk(Class<?> toWalk) {
        if (toWalk == null) {
            return new ArrayList<>();
        }
        List<Method> elementsToWalk = getElementsToWalk(toWalk.getSuperclass());
        for (Method m : toWalk.getDeclaredMethods()) {
            if (m.isAnnotationPresent(Element.class)) {
                elementsToWalk.add(m);
            }
        }

        for (Class c : toWalk.getInterfaces()) {
            for (Method m : c.getDeclaredMethods()) {
                if (m.isAnnotationPresent(Element.class)) {
                    elementsToWalk.add(m);
                }
            }
        }

        return elementsToWalk;
    }

    private void visit(String name, Object returned, Method method) {
        for (Renderer r : renderers) {
            r.enterElement(name, returned, method);
        }
    }

    private void visit(String name, Object returned) {
        for (Renderer r : renderers) {
            r.enterElement(name, returned);
        }
    }

    private void leave(String name) {
        for (Renderer r : renderers) {
            r.exitElement(name);
        }
    }

    public static class ResponseNotAnnotatedException extends RuntimeException {

    }
}
