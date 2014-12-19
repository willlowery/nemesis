package renderer;

import java.io.IOException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import util.StreamUtil;

/**
 *
 * @author william.lowery@rocky.edu
 */
public class StreamRenderer implements Renderer {

    @Override
    public boolean render(HttpServletRequest req, HttpServletResponse resp, Object toRender) {
        if (toRender instanceof StreamResponse) {
            StreamResponse response = (StreamResponse) toRender;
            resp.setContentType(response.contentType);
            resp.setContentLength(response.contentLength);
            try (ServletOutputStream out = resp.getOutputStream()) {
                StreamUtil.copy(response.stream, out);
                return true;
            } catch (IOException ex) {
            }
        }
        return false;
    }

}
