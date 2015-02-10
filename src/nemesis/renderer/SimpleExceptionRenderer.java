
package nemesis.renderer;

import java.io.IOException;
import java.io.PrintWriter;
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
                
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.setContentType("text/html");
                ((Exception) toRender).printStackTrace();               
                
                ((Exception) toRender).printStackTrace(resp.getWriter());                
                return true;
            } catch (IOException ex) {
            }
        }
        return false;
    }
    
    protected void printException(PrintWriter writer, Throwable ex){
        if(ex == null){
            return;
        }
        writer.println(ex.getMessage());
        writer.println();
        ex.printStackTrace(writer);
        printException(writer, ex.getCause());
    }
    
}
