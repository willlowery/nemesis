package annotation.gateway;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author william.lowery@rocky.edu
 */
public class Proxy {

    Class<?> gateway;
    Map<Handle.Method, Method> methods;

    public Proxy(Class<?> gateway) {
        this.gateway = gateway;
        methods = new HashMap<>();
        findMethods(gateway);
    }   
    
    public Method getMethod(Handle.Method method){
        return methods.get(method);
    }

    public Class<? extends form.Form> getFormClass(Handle.Method method) {
        if (methods.containsKey(method)) {
            Method get = methods.get(method);
            Annotation[][] paramAnnos = get.getParameterAnnotations();
            if (paramAnnos.length >= 1 && paramAnnos[0].length >= 1 && (paramAnnos[0][0] instanceof Form)) {
                return  (Class<? extends form.Form>) get.getParameters()[0].getType();
            }
        }
        return null;
    }

    public String getResource() {        
        return getResource(gateway);
    }
    
    private String getResource(Class<?> gateway){
        if(gateway == null){
            return "";
        }
        
        if(gateway.isAnnotationPresent(Resource.class)){
            return gateway.getAnnotation(Resource.class).value();
        }else{
            return getResource(gateway.getSuperclass());
        }        
    }

    public Object handleMethod(Handle.Method method) {
        if (methods.containsKey(method)) {
            Method get = methods.get(method);
            try {
                return get.invoke(getInstance());
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                if (ex.getCause() instanceof RuntimeException) {
                    throw (RuntimeException) ex.getCause();
                } else {
                    throw new RuntimeException(ex.getCause());
                }
            }
        }
        return null;
    }

    public Object handleMethod(Handle.Method method, Object o) {
        if (methods.containsKey(method)) {
            Method get = methods.get(method);
            try {
                return get.invoke(getInstance(), o);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                if (ex.getCause() instanceof RuntimeException) {
                    throw (RuntimeException) ex.getCause();
                } else {
                    throw new RuntimeException(ex.getCause());
                }
            }
        }
        return  null;
    }

    Object getInstance() {
        try {
            return gateway.newInstance();
        } catch (InstantiationException | IllegalAccessException ex) {
            throw new InvalidGatewayException("Gateways must have a default constructor.");
        }
    }

    private void findMethods(Class<?> gateway) {
        if (gateway == null) {
            return;
        }
        Method[] allMethods = gateway.getDeclaredMethods();
        for (Method m : allMethods) {
            if (m.isAnnotationPresent(Handle.class)) {
                Handle handle = m.getAnnotation(Handle.class);
                methods.put(handle.method(), m);
            }
        }
        findMethods(gateway.getSuperclass());
    }

    public static class InvalidGatewayException extends RuntimeException {

        private InvalidGatewayException(String string) {
            super(string);
        }
    }
}
