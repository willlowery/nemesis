package nemesis.init;

import java.util.HashMap;
import java.util.Set;
import nemesis.annotation.Resource;
import nemesis.gateway.Proxy;
import nemesis.renderer.DirectoryServer;
import nemesis.renderer.Renderer;
import nemesis.renderer.SimpleExceptionRenderer;
import nemesis.renderer.SimpleNotFoundRenderer;
import org.reflections.Reflections;

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
