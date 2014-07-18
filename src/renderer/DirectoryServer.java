
package renderer;

import init.Init;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import util.StreamUtil;

/**
 *
 * @author william.lowery@rocky.edu
 */
public class DirectoryServer implements Renderer {

    String baseDirectory;    
    String webRoot= "";

    public DirectoryServer(String baseDirectory, String webRoot) {
        this.baseDirectory = baseDirectory;
        this.webRoot = webRoot;
    }    
    
    @Override
    public boolean render(HttpServletRequest req, HttpServletResponse resp, Object toRender) {
        try {
            String path = getPath(req);                        
            resp.setContentType(URLConnection.guessContentTypeFromName(path));
            
            if(path.endsWith(".js")){
                resp.setContentType("text/javascript");
            }else if(path.endsWith(".css")){
                resp.setContentType("text/css");
            }else if(path.endsWith("/")){
                path += "index.html";
            }
            FileInputStream in = new FileInputStream(path);
            try (ServletOutputStream out = resp.getOutputStream()) {
                StreamUtil.copy(in, out);
                out.flush();
            }
        } catch (IOException ex) {
            return false;
        }        
        return true;
    }
    
    String getPath(HttpServletRequest req){
        Logger.getLogger(webRoot).log(Level.ALL, req.getRequestURI());
        int index = Init.BASE_URI.isEmpty() ? 0 : Init.BASE_URI.length()-1;
        index += webRoot.isEmpty()? 0 : webRoot.length()-1;
        String requestURI = req.getRequestURI().substring(index);

        return baseDirectory + requestURI;
    }

    public String getBaseDirectory() {
        return baseDirectory;
    }
    
    public String getWebRoot(){
        return webRoot;
    }   
    
}
