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
import nemesis.annotation.ConfigureClass;
import nemesis.annotation.Description;
import nemesis.annotation.Resource;
import nemesis.form.Form;
import nemesis.form.FormElementValidator;
import nemesis.renderer.JSONResponseRenderer;
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
    SecurityHandler securityHandler;

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {

            if (resources.get(getPath(req)) == null) {
                if (!directory.render(req, resp, null)) {
                    resourceNotFoundRenderer.render(req, resp, null);
                }
            } else {
                handleMethod(req, resp, req.getMethod().toLowerCase());
            }
        } catch (ResourceNotFoundException ex) {
            resourceNotFoundRenderer.render(req, resp, null);
        } catch (InstantiationException | IllegalAccessException | RuntimeException ex) {
            exceptionRenderer.render(req, resp, ex);
        }
    }

    private void handleMethod(HttpServletRequest req, HttpServletResponse resp, String handle) throws InstantiationException, IllegalAccessException {
        Proxy proxy = resources.get(getPath(req));
        if (proxy == null) {
            throw new ResourceNotFoundException();
        }
        if (handle.equals("options")) {
            handleOptions(proxy, handle, req, resp);
            return;
        }

        Method method = proxy.getMethod(handle);
        if (method == null) {
            throw new ResourceNotFoundException();
        }

        if (securityHandler.isAccessible(req, resp, method)) {
            Handle handler = method.getAnnotation(Handle.class);
            Renderer renderer = handler.as().newInstance();
            Form form = proxy.getForm(handle, req);
            renderer.render(req, resp, proxy.handleMethod(handle, form, securityHandler));
        }

    }

    private void handleOptions(Proxy proxy, String handle, HttpServletRequest req, HttpServletResponse resp) {
        Class<?> c = proxy.gateway;
        R r = new R();
        if (c.isAnnotationPresent(Description.class)) {
            r.description = c.getAnnotation(Description.class).value();

        }
        r.resource = proxy.getResource();
        proxy.getMethods().forEach((methodName, method) -> {
            R.M m = new R.M();
            m.method = methodName;

            try {
                Form f = proxy.getForm(methodName, req);
                if (f == null) {
                    return;
                }
                f.getFormValidators().forEach((fv) -> {
                    if (fv.getClass().isAnnotationPresent(Description.class)) {
                        m.formValidators.add(fv.getClass().getAnnotation(Description.class).value());
                    }
                });

                f.getParsers().forEach((name, elementPar) -> {
                    R.F field = new R.F();
                    field.name = name;
                    if (elementPar.getClass().isAnnotationPresent(Description.class)) {
                        field.type = elementPar.getClass().getAnnotation(Description.class).value();
                        FormElementValidator fep = f.getElementValidators().get(name);
                        if (fep != null && fep.getClass().isAnnotationPresent(Description.class)) {
                            field.validators = fep.getClass().getAnnotation(Description.class).value();
                        }
                    }
                    m.fields.add(field);

                });
            } catch (InstantiationException | IllegalAccessException ex) {
            }
            r.methods.add(m);
        });
        new JSONResponseRenderer().render(req, resp, r);
    }

    private String getPath(HttpServletRequest req) {
        return req.getRequestURI().substring(req.getContextPath().length());
    }

    public static class ResourceNotFoundException extends RuntimeException {

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
        securityHandler = (HttpServletRequest req, HttpServletResponse resp, Method method) -> true;

        Set<Class<?>> configureClasses = ref.getTypesAnnotatedWith(ConfigureClass.class);
        for (Class<?> configClass : configureClasses) {
            ConfigureClass output = configClass.getAnnotation(ConfigureClass.class);
            try {
                switch (output.value()) {
                    case EXCEPTION:
                        exceptionRenderer = (Renderer) configClass.newInstance();
                        break;
                    case NON_FOUND:
                        resourceNotFoundRenderer = (Renderer) configClass.newInstance();
                        break;
                    case FILE:
                        directory = (Renderer) configClass.newInstance();
                        break;
                    case SECURITY:
                        securityHandler = (SecurityHandler) configClass.newInstance();
                        break;
                }
            } catch (InstantiationException | IllegalAccessException e) {
            }
        }
    }
}
