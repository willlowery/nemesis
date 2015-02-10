package nemesis.response;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.reflect.Method;

public class XMLRenderer implements Renderer {

    PrintWriter stream;

    public XMLRenderer(OutputStream stream) {
        this.stream = new PrintWriter(stream);
    }

    @Override
    public void enterMethod(String name, Object returned, Method method) {
        if (returned == null)
            return;
        stream.printf("<%s>%s", name, handleEscapeChars(returned.toString()));
    }

    @Override
    public void enterObject(String name, Object returned) {
        stream.printf("<%s>", name);
    }

    @Override
    public void enterList(String name, Method method) {
        stream.printf("<%s>", name);
    }

    @Override
    public void exitList(String name) {
        stream.printf("</%s>", name);

    }

    @Override
    public void exitMethod(String name) {
        stream.printf("</%s>", name);

    }

    @Override
    public void exitObject(String name) {
        stream.printf("</%s>", name);
        stream.flush();
    }

    private String handleEscapeChars(String value) {
        return value.replace("&", "&amp;")
                .replace("'", "&apos;")
                .replace("\"", "&quot;")
                .replace("<", "&lt;")
                .replace(">", "&gt;");
    }

}
