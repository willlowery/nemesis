package annotation.response;

import init.Init;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
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

    public void walk(Object response, Class<?> toWalk) {
        visit(getElement(toWalk), response);
        List<Method> elementsToWalk = getElementsToWalk(toWalk);
        for (Method method : elementsToWalk) {
            try {
                Object returned = method.invoke(response);
                if (method.getReturnType().isAnnotationPresent(Element.class)) {
                    visit(getElementName(method), returned, method);
                    walk(returned, method.getReturnType());
                    leave(getElementName(method));
                } else if (isA(method.getReturnType(), Collection.class)) {
                    handleCollection(method, returned);
                } else if (method.getReturnType().isArray()) {
                    handleCollection(method, Arrays.asList((Object[]) returned));
                } else {
                    visit(getElementName(method), returned, method);
                    leave(getElementName(method));
                }
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                visit("Error", ex.getLocalizedMessage(), method);
                leave("Error");
            }
        }
        leaveObject(getElement(toWalk));
    }

    private void handleCollection(Method method, Object returned) {
        visitList(getElementName(method), method);
        if (method.getAnnotation(Listing.class).value().isAnnotationPresent(Element.class)) {
            for (Object o : (Collection) returned) {
                walk(o, method.getAnnotation(Listing.class).value());
            }
        } else {
            for (Object o : (Collection) returned) {
                visit(getElementName(method), o, method);
            }
        }
        leaveList(getElementName(method));
    }

    private boolean isA(Class<?> a, Class<?> b) {
        return b.isAssignableFrom(a);
    }

    private String getElement(Class<?> toWalk) {
        Class<?>[] interfaces = toWalk.getInterfaces();
        for(Class<?> inter: interfaces ){
            if (inter.isAnnotationPresent(Element.class)) {
                return inter.getAnnotation(Element.class).value();
            }   
        }
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

    private void visitList(String name, Method method) {
        for (Renderer r : renderers) {
            r.enterList(name, method);
        }
    }

    private void visit(String name, Object returned, Method method) {
        Object value = returned;
        for(Annotation anno: method.getAnnotations()){
            value = Init.formatter(anno, value);
        }
        for (Renderer r : renderers) {
            r.enterElement(name, value, method);
        }
    }

    private void visit(String name, Object returned) {
        for (Renderer r : renderers) {
            r.enterElement(name, returned);
        }
    }

    private void leave(String name) {
        for (Renderer r : renderers) {
            r.exitMethod(name);
        }
    }

    private void leaveObject(String name) {
        for (Renderer r : renderers) {
            r.exitObject(name);
        }
    }

    private void leaveList(String name) {
        for (Renderer r : renderers) {
            r.exitList(name);
        }
    }

    public static class ResponseNotAnnotatedException extends RuntimeException {
    }
}
