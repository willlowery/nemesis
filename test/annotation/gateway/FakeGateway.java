
package annotation.gateway;

/**
 *
 * @author william.lowery@rocky.edu
 */
@Resource(value = "nowhere")
public class FakeGateway {

    @Handle(method = Handle.Method.GET)
    public void handleGet() {
    }

    @Handle(method = Handle.Method.DELETE)
    public void handleDelete() {
    }

    @Handle(method = Handle.Method.PUT)
    public void handlePut() {
    }

    @Handle(method = Handle.Method.POST)
    public void handlePost() {
    }

    @Handle(method = Handle.Method.OPTIONS)
    public void handleOptions() {
    }
    
}
