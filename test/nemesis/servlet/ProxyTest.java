package nemesis.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import nemesis.annotation.Handle;
import nemesis.annotation.Resource;
import nemesis.form.Form;
import nemesis.form.FormElementParser;
import nemesis.form.FormElementValidator;
import nemesis.form.FormValidator;
import nemesis.form.ParseException;
import nemesis.form.ValidationException;
import nemesis.servlet.Proxy.InvalidGatewayException;
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
        proxy.handleMethod("get");
        proxy.handleMethod("delete");
        proxy.handleMethod("options");
        proxy.handleMethod("post");
        proxy.handleMethod("put");

        assertThat(SpyGateway.getCalled, is(true));
        assertThat(SpyGateway.putCalled, is(true));
        assertThat(SpyGateway.deleteCalled, is(true));
        assertThat(SpyGateway.optionsCalled, is(true));
        assertThat(SpyGateway.postCalled, is(true));
    }

    @Test(expected = IllegalStateException.class)
    public void testGatewayMethodGetsForm() {
        Proxy proxy = new Proxy(GatewayNeedingAForm.class);
        proxy.handleMethod("get", new FakeForm());
    }

    @Test
    public void testProxyGetFormClass() throws InstantiationException, IllegalAccessException {
        Proxy proxy = new Proxy(GatewayNeedingAForm.class);
        Form form = proxy.getForm("get", null);
        assertTrue(form instanceof FakeForm);
    }

    @Test(expected = RuntimeException.class)
    public void testCheckedExceptionsAreThrownUp() {
        Proxy proxy = new Proxy(GatewayThrowingIOException.class);
        proxy.handleMethod("get");
    }

    @Test
    public void testReturnValue() {
        Proxy proxy = new Proxy(GatewayWithReturn.class);
        assertThat((String) proxy.handleMethod("get"), is("hello"));
    }

    @Resource("aResource")
    public static class GatewayThrowingIOException {

        @Handle(method = "GET")
        public void handleGet() throws IOException {
            throw new IOException();
        }

    }

    @Resource("aResource")
    public static class GatewayNeedingAForm {

        @Handle(method = "GET")
        public void handleGet(FakeForm o) {
            throw new IllegalStateException("It worked");
        }

        @Handle(method = "PUT")
        public void handlePut(FakeForm o, String s) {
            throw new IllegalStateException("It worked");
        }

    }

    @Resource("anotherResource")
    public static class GatewayWithReturn {

        @Handle(method = "GET")
        public String handleGet() {
            return "hello";
        }

    }

    public static class FakeForm implements Form {

        @Override
        public void map(HttpServletRequest request) {

        }

        @Override
        public List<ValidationException> getValidationExceptions() {
            return new ArrayList<>();
        }

        @Override
        public Map<String, FormElementValidator> getElementValidators() {
            return new HashMap<>();
        }

        @Override
        public List<FormValidator> getFormValidators() {
            return new ArrayList<>();
        }

        @Override
        public List<ParseException> getParseExceptions() {
            return new ArrayList<>();
        }

        @Override
        public Map<String, FormElementParser> getParsers() {
            return new HashMap<>();
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
