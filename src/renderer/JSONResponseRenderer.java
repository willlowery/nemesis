package renderer;

import response.JSONRenderer;
import response.ResponseWalker;
import java.io.IOException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author william.lowery@rocky.edu
 */
public class JSONResponseRenderer implements Renderer {

    @Override
    public boolean render(HttpServletRequest req, HttpServletResponse resp, Object toRender) {
        resp.setContentType("application/json");
        try (ServletOutputStream out = resp.getOutputStream()) {
            ResponseWalker walker = new ResponseWalker(new JSONRenderer(out));
            walker.walk(toRender);
        } catch (IOException ex) {

        }
        return true;
    }
}
