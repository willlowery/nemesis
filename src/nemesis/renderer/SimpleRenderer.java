package nemesis.renderer;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author william.lowery@rocky.edu
 */
public class SimpleRenderer implements Renderer {

    @Override
    public boolean render(HttpServletRequest req, HttpServletResponse resp, Object toRender) {
        resp.setContentType("text/html");
        try {
            PrintWriter writer = resp.getWriter();
            writer.println(toRender);
        } catch (IOException ex) {

        }
        return true;
    }

}
