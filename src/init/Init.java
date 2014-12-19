package init;

import gateway.Proxy;
import annotation.Resource;
import java.util.HashMap;
import java.util.Set;
import org.reflections.Reflections;
import renderer.DirectoryServer;
import renderer.Renderer;
import renderer.SimpleExceptionRenderer;
import renderer.SimpleNotFoundRenderer;

/**
 *
 * @author william.lowery@rocky.edu
 */
public class Init {

    public static HashMap<String, Proxy> resources = new HashMap<>();
    public static Class<?> exceptionRenderer = SimpleExceptionRenderer.class;
    public static Class<?> resourceNotFoundRenderer = SimpleNotFoundRenderer.class;
    public static DirectoryServer directory;

    public static void addResource(Proxy proxy) {
        resources.put(proxy.getResource(), proxy);
    }

    public static <T extends Renderer> void setResourceNotFoundRenderer(Class<T> cls) {
        resourceNotFoundRenderer = cls;
    }

    public static <T extends Renderer> void setExceptionRenderer(Class<T> cls) {
        exceptionRenderer = cls;
    }

    public static void buildResources(String location) {
        Reflections ref = new Reflections(location);
        Set<Class<?>> gateways = ref.getTypesAnnotatedWith(Resource.class);
        for (Class<?> cls : gateways) {
            Proxy proxy = new Proxy(cls);
            resources.put(proxy.getResource(), proxy);
        }
    }
}
