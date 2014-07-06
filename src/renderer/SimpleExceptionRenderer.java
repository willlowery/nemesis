
package renderer;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author william.lowery@rocky.edu
 */
public class SimpleExceptionRenderer implements Renderer{

    @Override
    public boolean render(HttpServletRequest req, HttpServletResponse resp, Object toRender) {
        if(toRender instanceof Exception){            
            try {
                resp.setContentType("text/html");
                ((Exception) toRender).printStackTrace(resp.getWriter());
                return true;
            } catch (IOException ex) {
            }
        }
        return false;
    }
    
}
