package annotation.gateway;

import annotation.gateway.Proxy.InvalidGatewayException;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author william.lowery@rocky.edu
 */
public class ProxyTest {

    @Test
    public void testCreation() {
        Proxy proxy = new Proxy(FakeGateway.class);
        assertThat(proxy.getInstance(), instanceOf(FakeGateway.class));
    }

    @Test
    public void testResourceRetrieval() {
        Proxy proxy = new Proxy(FakeGateway.class);
        String resource = proxy.getResource();
        assertThat(resource, is("nowhere"));
    }

    @Test(expected = InvalidGatewayException.class)
    public void testGatewayWithNoDefaultConstructorFails() {
        Proxy proxy = new Proxy(InvalidConstructor.class);
        proxy.getInstance();
    }

    @Test
    public void methodsAreCalled() {
        Proxy proxy = new Proxy(SpyGateway.class);
        proxy.handleMethod(Handle.Method.GET);
        proxy.handleMethod(Handle.Method.DELETE);
        proxy.handleMethod(Handle.Method.OPTIONS);
        proxy.handleMethod(Handle.Method.POST);
        proxy.handleMethod(Handle.Method.PUT);
        

        assertThat(SpyGateway.getCalled, is(true));
        assertThat(SpyGateway.putCalled, is(true));
        assertThat(SpyGateway.deleteCalled, is(true));
        assertThat(SpyGateway.optionsCalled, is(true));
        assertThat(SpyGateway.postCalled, is(true));
    }

    @Test(expected = IllegalStateException.class)
    public void testGatewayMethodGetsForm() {
        Proxy proxy = new Proxy(GatewayNeedingAForm.class);
        proxy.handleMethod(Handle.Method.GET, new FakeForm());
    }
    
    @Test(expected = RuntimeException.class)
    public void testCheckedExceptionsAreThrownUp() {
        Proxy proxy = new Proxy(GatewayThrowingIOException.class);
        proxy.handleMethod(Handle.Method.GET);
    }

    @Test
    public void testReturnValue() {
        Proxy proxy = new Proxy(GatewayWithReturn.class);
        assertThat((String) proxy.handleMethod(Handle.Method.GET), is("hello"));
    }

    @Resource("aResource")
    public static class GatewayThrowingIOException {

        @Handle(method = Handle.Method.GET)
        public void handleGet() throws IOException {
            throw new IOException();
        }

    }

    @Resource("aResource")
    public static class GatewayNeedingAForm {

        @Handle(method = Handle.Method.GET)
        public void handleGet(@Form FakeForm o) {
            throw new IllegalStateException("It worked");
        }
        
        @Handle(method = Handle.Method.PUT)
        public void handlePut(@Form FakeForm o,String s) {
            throw new IllegalStateException("It worked");
        }
        

    }

    @Resource("anotherResource")
    public static class GatewayWithReturn {

        @Handle(method = Handle.Method.GET)
        public String handleGet() {
            return "hello";
        }

    }

    public static class FakeForm implements form.Form {

        @Override
        public void map(HttpServletRequest request) {
            
        }

    }

    public static class SpyGateway extends FakeGateway {

        static public boolean getCalled = false;
        static public boolean putCalled = false;
        static public boolean deleteCalled = false;
        static public boolean postCalled = false;
        static public boolean optionsCalled = false;

        @Override
        public void handleGet() {
            getCalled = true;
        }

        @Override
        public void handlePut() {
            putCalled = true;
        }

        @Override
        public void handleDelete() {
            deleteCalled = true;
        }

        @Override
        public void handleOptions() {
            optionsCalled = true;
        }

        @Override
        public void handlePost() {
            postCalled = true;
        }

    }

    public static class InvalidConstructor {

        public InvalidConstructor(String a, int b) {
        }

    }

}
