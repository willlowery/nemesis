package nemesis.renderer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLConnection;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nemesis.util.StreamUtil;

/**
 *
 * @author william.lowery@rocky.edu
 */
public class DirectoryServer implements Renderer {

    String baseDirectory;

    public DirectoryServer(String baseDirectory) {
        this.baseDirectory = baseDirectory;
    }

    @Override
    public boolean render(HttpServletRequest req, HttpServletResponse resp, Object toRender) {
        String path = getPath(req);
        File file = new File(path);

        if (file.exists() && file.isFile() ) {
            resp.setContentType(URLConnection.guessContentTypeFromName(path));
            if (path.endsWith(".js")) {
                resp.setContentType("text/javascript");
            } else if (path.endsWith(".css")) {
                resp.setContentType("text/css");
            }
        } else {
            if (path.endsWith("/")) {
                path += "index.html";
            } else {
                path += "/index.html";
            }
            resp.setContentType("text/html");
        }
        
        try {
            try (FileInputStream in = new FileInputStream(path)) {
                try (ServletOutputStream out = resp.getOutputStream()) {
                    StreamUtil.copy(in, out);
                    out.flush();
                    return true;
                }
            }
        } catch (IOException ex) {
        }

        return false;
    }

    String getPath(HttpServletRequest req) {
        return baseDirectory + req.getRequestURI().substring(req.getContextPath().length());
    }
}
