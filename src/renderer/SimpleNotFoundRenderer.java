package renderer;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author william.lowery@rocky.edu
 */
public class SimpleNotFoundRenderer implements Renderer {

    @Override
    public boolean render(HttpServletRequest req, HttpServletResponse resp, Object toRender) {
        try {
            resp.setContentType("text/html");
            PrintWriter writer = resp.getWriter();
            writer.println("<html>");
            writer.println("<body>");
            writer.println("<h3>");
            writer.print(req.getRequestURI());
            writer.print(" had no mapping");            
            writer.println("<h3>");
            writer.println("</body>");
            writer.println("</html>");

        } catch (IOException ex) {

        }
        return true;
    }

}
