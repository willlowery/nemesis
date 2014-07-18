package renderer;

import form.DummyRequest;
import form.StandardFormTest;
import init.Init;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author william.lowery@rocky.edu
 */
public class DirectoryServerTest {

    @Test
    public void testGetFilePath() {
        
        DirectoryServer server = new DirectoryServer("/home","");
        assertThat(server.getPath(new FakeRequest("/a.html")), is("/home/a.html"));
        assertThat(server.getPath(new FakeRequest("/level/a.html")), is("/home/level/a.html"));
        Init.BASE_URI = "/sample/";
        assertThat(server.getPath(new FakeRequest("/sample/a.html")), is("/home/a.html"));
        assertThat(server.getPath(new FakeRequest("/sample/level/a.html")), is("/home/level/a.html"));
    }

    public static class FakeRequest extends DummyRequest {

        private String uri;

        public FakeRequest(String uri) {
            this.uri = uri;
        }

        @Override
        public String getRequestURI() {
            return uri;
        }
    }

}
