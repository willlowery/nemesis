
package nemesis.servlet;

import nemesis.annotation.Handle;
import nemesis.annotation.Resource;

/**
 *
 * @author william.lowery@rocky.edu
 */
@Resource(value = "nowhere")
public class FakeGateway {

    @Handle(method = "GET")
    public void handleGet() {
    }

    @Handle(method = "DELETE")
    public void handleDelete() {
    }

    @Handle(method = "PUT")
    public void handlePut() {
    }

    @Handle(method = "POST")
    public void handlePost() {
    }

    @Handle(method = "OPTIONS")
    public void handleOptions() {
    }
    
}
