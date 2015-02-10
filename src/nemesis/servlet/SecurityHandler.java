package nemesis.servlet;

import java.lang.reflect.Method;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface SecurityHandler {

    boolean isAccessible(HttpServletRequest req, HttpServletResponse response, Method method);
}
