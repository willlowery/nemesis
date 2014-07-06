package renderer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author william.lowery@rocky.edu
 */
public interface Renderer {

    public boolean render(HttpServletRequest req, HttpServletResponse resp, Object toRender);
}
