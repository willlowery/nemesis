package init;

import annotation.gateway.Proxy;
import annotation.gateway.Resource;
import annotation.response.formatter.Formatter;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    public static String BASE_URI = "";
    public static HashMap<String, Proxy> resources = new HashMap<>();
    public static Map<Class<?>, Class<? extends Formatter>> responseFormatters = new HashMap<>();
    public static List< Class<?>> renderers = new ArrayList<>();
    public static Class<?> exceptionRenderer = SimpleExceptionRenderer.class;
    public static Class<?> resourceNotFoundRenderer = SimpleNotFoundRenderer.class;
    public static DirectoryServer directory;

    public static void addResource(Proxy proxy) {
        resources.put(proxy.getResource(), proxy);
    }

    public static <T extends Renderer> void addRenderer(Class<T> rendererClass) {
        renderers.add(rendererClass);
    }

    public static <T extends Renderer> void setResourceNotFoundRenderer(Class<T> cls) {
        resourceNotFoundRenderer = cls;
    }

    public static <T extends Renderer> void setExceptionRenderer(Class<T> cls) {
        exceptionRenderer = cls;
    }

    public static void buildResources(String location) {
        Reflections ref = new Reflections(location);
        Set<Class<?>> resources = ref.getTypesAnnotatedWith(Resource.class);
        for (Class<?> cls : resources) {
            Proxy proxy = new Proxy(cls);
            addResource(proxy);
        }
    }

    public static void register(Class<?> anno, Class<? extends Formatter> formatter) {
        responseFormatters.put(anno, formatter);
    }

    public static Object formatter(Annotation anno, Object toFormat){
        Class<? extends Formatter> formatterClass = responseFormatters.get(anno.annotationType());
        if(formatterClass != null){
            try {
                return formatterClass.newInstance().format(toFormat, anno);
            } catch (InstantiationException | IllegalAccessException ex) {

            }
        }
        return toFormat;
    }
}
