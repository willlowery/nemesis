package nemesis.servlet;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Set;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nemesis.annotation.Handle;
import nemesis.annotation.OutputRenderer;
import nemesis.annotation.Resource;
import nemesis.form.Form;
import nemesis.gateway.Proxy;
import nemesis.renderer.Renderer;
import nemesis.renderer.SimpleExceptionRenderer;
import nemesis.renderer.SimpleNotFoundRenderer;
import org.reflections.Reflections;

/**
 *
 * @author william.lowery@rocky.edu
 */
public class Servlet extends HttpServlet {

    HashMap<String, Proxy> resources = new HashMap<>();
    Renderer exceptionRenderer;
    Renderer resourceNotFoundRenderer;
    Renderer directory;

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (resources.get(getPath(req)) == null) {
            if (!directory.render(req, resp, null)) {
                resourceNotFoundRenderer.render(req, resp, null);
            }
        } else {
            handleMethod(req, resp, req.getMethod().toLowerCase());
        }
    }

    private void handleMethod(HttpServletRequest req, HttpServletResponse resp, String handle) {
        Proxy proxy = resources.get(getPath(req));
        try {
            Class<? extends Form> formClass = proxy.getFormClass(handle);
            Method method = proxy.getMethod(handle);
            if(method.isAnnotationPresent(Handle.class)) {
                Handle handler = method.getAnnotation(Handle.class);
                Renderer renderer = handler.as().newInstance();
                if (formClass == null) {
                    renderer.render(req, resp, proxy.handleMethod(handle));
                } else {
                    Form form = formClass.newInstance();
                    form.map(req);
                    renderer.render(req, resp, proxy.handleMethod(handle, form));
                }
            }
        } catch (InstantiationException | IllegalAccessException | RuntimeException ex) {
            exceptionRenderer.render(req, resp, ex);
        }
    }

    private String getPath(HttpServletRequest req) {
        return req.getRequestURI().substring(req.getContextPath().length());
    }

    @Override
    public void init() throws ServletException {
        findResources();
    }

    private void findResources() {
        Reflections ref = new Reflections("");

        Set<Class<?>> gateways = ref.getTypesAnnotatedWith(Resource.class);
        for (Class<?> cls : gateways) {
            Proxy proxy = new Proxy(cls);
            resources.put(proxy.getResource(), proxy);
        }

        resourceNotFoundRenderer = new SimpleNotFoundRenderer(resources);
        exceptionRenderer = new SimpleExceptionRenderer();
        directory = new SimpleNotFoundRenderer(resources);

        Set<Class<?>> rendererClasses = ref.getTypesAnnotatedWith(OutputRenderer.class);
        for (Class<?> rendererClass : rendererClasses) {
            if (Renderer.class.isAssignableFrom(rendererClass)) {
                OutputRenderer output = rendererClass.getAnnotation(OutputRenderer.class);
                try {
                    switch (output.value()) {
                        case EXCEPTION:
                            exceptionRenderer = (Renderer) rendererClass.newInstance();
                            break;
                        case NON_FOUND:
                            resourceNotFoundRenderer = (Renderer) rendererClass.newInstance();
                            break;
                        case FILE:
                            directory = (Renderer) rendererClass.newInstance();
                            break;
                    }
                } catch (InstantiationException | IllegalAccessException e) {
                    
                }
            }
        }
    }
}
