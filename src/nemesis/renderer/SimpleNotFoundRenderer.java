package nemesis.renderer;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nemesis.init.Init;

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
            writer.println("<ul>");
            for(String url : Init.resources.keySet()){
                
                writer.println("<li>" + url + "<li>");
            }
            writer.println("</ul>");            
            writer.println("</body>");
            writer.println("</html>");

        } catch (IOException ex) {

        }
        return true;
    }

}
