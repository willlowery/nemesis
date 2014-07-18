package servlet;

import annotation.gateway.Handle;
import annotation.gateway.Proxy;
import form.Form;
import init.Init;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import renderer.Renderer;

/**
 *
 * @author william.lowery@rocky.edu
 */
public class Servlet extends HttpServlet {

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        handleMethod(req, resp, Handle.Method.DELETE);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!Init.directory.render(req, resp, null)) {
            handleMethod(req, resp, Handle.Method.GET);
        }
    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        handleMethod(req, resp, Handle.Method.OPTIONS);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        handleMethod(req, resp, Handle.Method.POST);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        handleMethod(req, resp, Handle.Method.PUT);
    }

    private void handleMethod(HttpServletRequest req, HttpServletResponse resp, Handle.Method handle) {
        Proxy proxy = Init.resources.get(Init.BASE_URI + req.getRequestURI());
        try {
            if (proxy == null) {
                Renderer renderer = (Renderer) Init.resourceNotFoundRenderer.newInstance();
                renderer.render(req, resp, null);
            } else {
                Class<? extends Form> formClass = proxy.getFormClass(handle);
                if (formClass == null) {
                    render(req, resp, proxy.handleMethod(handle));
                } else {
                    Form form = formClass.newInstance();
                    form.map(req);
                    render(req, resp, proxy.handleMethod(handle, form));
                }
            }
        } catch (InstantiationException | IllegalAccessException | RuntimeException ex) {
            try {
                Renderer renderer = (Renderer) Init.exceptionRenderer.newInstance();
                renderer.render(req, resp, ex);
            } catch (InstantiationException | IllegalAccessException ex1) {
            }
        }
    }

    private void render(HttpServletRequest req, HttpServletResponse resp, Object handleGet) throws IllegalAccessException, InstantiationException {
        for (Class<?> rendererClass : Init.renderers) {
            Renderer renderer = (Renderer) rendererClass.newInstance();
            if (renderer.render(req, resp, handleGet)) {
                break;
            }
        }
    }
}
