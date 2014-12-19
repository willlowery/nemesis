
package nemesis.renderer;

import java.io.InputStream;

/**
 *
 * @author william.lowery@rocky.edu
 */
public class StreamResponse {
    int contentLength;
    String contentType;
    InputStream stream;

    public int getContentLength() {
        return contentLength;
    }

    public void setContentLength(int contentLength) {
        this.contentLength = contentLength;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public InputStream getStream() {
        return stream;
    }

    public void setStream(InputStream stream) {
        this.stream = stream;
    }
    
}
