
package init;

import annotation.gateway.Proxy;
import annotation.gateway.Resource;
import java.util.Set;
import org.reflections.Reflections;
import servlet.Servlet;

/**
 *
 * @author william.lowery@rocky.edu
 */
public class GatewayLocator {
    
    public static void buildResources(String location){
        Reflections ref = new Reflections(location);
        Set<Class<?>> resources = ref.getTypesAnnotatedWith(Resource.class);  
        for(Class<?> cls : resources){
            Proxy proxy = new Proxy(cls);
            Servlet.addResource(proxy);
        }
    }
    
}
