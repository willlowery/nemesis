package nemesis.servlet;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import nemesis.annotation.Handle;
import nemesis.annotation.Resource;
import nemesis.form.Form;

/**
 *
 * @author william.lowery@rocky.edu
 */
public class Proxy {

    Class<?> gateway;
    Map<String, Method> methods;

    public Proxy(Class<?> gateway) {
        this.gateway = gateway;
        methods = new HashMap<>();
        findMethods(gateway);
    }   

    public Map<String, Method> getMethods() {
        return methods;
    }
    
    public Method getMethod(String method){
        return methods.get(method);
    }

    public Form getForm(String method, HttpServletRequest req) throws InstantiationException, IllegalAccessException {
        if (methods.containsKey(method)) {
            Method handler = methods.get(method);
            Class<?>[] types = handler.getParameterTypes();
            for(Class<?> type : types){
                if(Form.class.isAssignableFrom(type)){
                   Form form = (Form) type.newInstance();
                   form.map(req);
                   return form;
                }
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

    public Object handleMethod(String method, Object... o) {
        if (methods.containsKey(method)) {
            Method get = methods.get(method);
            try {
                return get.invoke(getInstance(), createParamArray(get, o));
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                if (ex.getCause() instanceof RuntimeException) {
                    throw (RuntimeException) ex.getCause();
                } else {
                    final RuntimeException runtimeException = new RuntimeException(ex.getMessage(),ex);
                    runtimeException.setStackTrace(ex.getStackTrace());
                    throw runtimeException;
                }
            }
        }
        return null;
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
                methods.put(handle.method().toLowerCase(), m);
            }
        }
        findMethods(gateway.getSuperclass());
    }

    public static class InvalidGatewayException extends RuntimeException {

        private InvalidGatewayException(String string) {
            super(string);
        }
    }
    
    public static Object[] createParamArray(Method m, Object... in){
        HashMap<String, Object> map = new HashMap<>();
        Object[] values = new Object[m.getParameterCount()];
        for (Object o : in) {
            if(o != null)
                map.put(o.getClass().getName(), o);
        }
        int idx = 0;
        for(Type t: m.getParameterTypes()){
            values[idx++] = map.get(t.getTypeName());
        }
        return values;
    }
    
    public static class T{
        public void thing(String a, Integer b){
            System.out.println(a + " " + b);
        }
    }
}
