package nemesis.servlet;

import java.io.IOException;
import java.lang.reflect.Method;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nemesis.annotation.Handle;
import nemesis.form.Form;
import nemesis.gateway.Proxy;
import nemesis.init.Init;
import nemesis.renderer.Renderer;

/**
 *
 * @author william.lowery@rocky.edu
 */
public class Servlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (Init.resources.get(req.getRequestURI()) == null) {
            if (Init.directory != null && !Init.directory.render(req, resp, null)) {
                try {
                    Renderer renderer = (Renderer) Init.resourceNotFoundRenderer.newInstance();
                    renderer.render(req, resp, null);
                } catch (InstantiationException | IllegalAccessException ex) {
                    handleError(req, resp, ex);
                }
            }
        } else {
            handleMethod(req, resp, req.getMethod().toLowerCase());
        }
    }

    private void handleMethod(HttpServletRequest req, HttpServletResponse resp, String handle) {
        Proxy proxy = Init.resources.get(req.getRequestURI());
        try {            
            Class<? extends Form> formClass = proxy.getFormClass(handle);
            Method method = proxy.getMethod(handle);
            if (method.isAnnotationPresent(Handle.class)) {
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
            handleError(req, resp, ex);
        }
    }

    void handleError(HttpServletRequest req, HttpServletResponse resp, final java.lang.Exception ex) {
        try {
            Renderer renderer = (Renderer) Init.exceptionRenderer.newInstance();
            renderer.render(req, resp, ex);
        } catch (InstantiationException | IllegalAccessException ex1) {
        }
    }
}
