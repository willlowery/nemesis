package servlet;

import annotation.gateway.Handle;
import annotation.gateway.Proxy;
import form.Form;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import renderer.Renderer;
import renderer.SimpleExceptionRenderer;
import renderer.SimpleNotFoundRenderer;

/**
 *
 * @author william.lowery@rocky.edu
 */
public class Servlet extends HttpServlet {

    private static final ConcurrentHashMap<String, Proxy> resources = new ConcurrentHashMap<>();
    private static final ConcurrentSkipListSet< Class<?>> renderers = new ConcurrentSkipListSet<>();
    private static Class<?> exceptionRenderer = SimpleExceptionRenderer.class;
    private static Class<?> resourceNotFoundRenderer = SimpleNotFoundRenderer.class;

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

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Proxy proxy = resources.get(req.getRequestURI());
        try {
            if (proxy == null) {
                Renderer renderer = (Renderer) resourceNotFoundRenderer.newInstance();
                renderer.render(req, resp, null);

            } else {
                Class<? extends Form> formClass = proxy.getFormClass(Handle.Method.GET);
                if (formClass == null) {
                    render(req, resp, proxy.handleMethod(Handle.Method.GET));
                } else {
                    Form form = formClass.newInstance();
                    form.map(req);
                    render(req, resp, proxy.handleMethod(Handle.Method.GET, form));
                }
            }
        } catch (InstantiationException | IllegalAccessException | RuntimeException ex) {
            try {
                Renderer renderer = (Renderer) exceptionRenderer.newInstance();
                renderer.render(req, resp, ex);
            } catch (InstantiationException | IllegalAccessException ex1) {
            }
        }
    }

    private void render(HttpServletRequest req, HttpServletResponse resp, Object handleGet) throws IllegalAccessException, InstantiationException {
        for (Class<?> rendererClass : renderers) {
            Renderer renderer = (Renderer) rendererClass.newInstance();
            if (renderer.render(req, resp, handleGet)) {
                break;
            }
        }
    }
}
