package nemesis.response;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.LinkedList;
import nemesis.annotation.Element;

/**
 *
 * @author william.lowery@rocky.edu
 */
public class JSONRenderer implements Renderer {

    PrintWriter stream;
    LinkedList<JSON> context = new LinkedList<>();

    public JSONRenderer(OutputStream stream) {
        this.stream = new PrintWriter(stream);
    }

    @Override
    public void enterMethod(String elementName, Object returned, Method method) {

        context.peek().enterMethod(elementName, returned, method);
    }

    @Override
    public void enterObject(String elementName, Object returned) {
        if (!context.isEmpty()) {
            context.peek().enterObject(elementName, returned);
        }
        context.push(new ObjectContext(stream, elementName));

    }

    @Override
    public void exitMethod(String name) {

    }

    @Override
    public void exitObject(String name) {
        context.peek().exitObject(name);
        context.pop();
        stream.flush();
    }

    @Override
    public void enterList(String name, Method method) {
        
        stream.print(", ");
        
        context.peek().enterList(name, method);
        
        stream.print(quote(name) + ": ");
        context.push(new List(stream));
        context.peek().enterList(name, method);
    }

    @Override
    public void exitList(String name) {
        context.pop().exitList(name);
    }

    public static class JSON implements Renderer {

        PrintWriter writer;
        boolean first = true;

        public JSON(PrintWriter writer) {
            this.writer = writer;
        }

        @Override
        public void enterMethod(String name, Object returned, Method method) {
        }

        @Override
        public void enterObject(String name, Object returned) {
        }

        @Override
        public void enterList(String name, Method method) {
        }

        @Override
        public void exitList(String name) {
        }

        @Override
        public void exitMethod(String name) {
        }

        @Override
        public void exitObject(String name) {
        }

    }

    public static class ObjectContext extends JSON {

        public ObjectContext(PrintWriter writer, String name) {
            super(writer);
            writer.print("{" +quote("@object")+": " +quote(name));
            
        }

        @Override
        public void enterList(String name, Method method) {
            first = false;
        }
        

        @Override
        public void enterMethod(String elementName, Object returned, Method method) {
            writer.print(", ");
            if (method.getReturnType().isAnnotationPresent(Element.class)) {
                writer.print(quote(elementName) + ": ");
            } else {
                writer.print(quote(elementName) + ": " + quote(returned));
            }
            first = false;
        }

        @Override
        public void enterObject(String elementName, Object returned) {

        }

        @Override
        public void exitObject(String name) {
            writer.print("}");
            writer.flush();
        }
    }

    public static class List extends JSON {

        public List(PrintWriter writer) {
            super(writer);
        }

        @Override
        public void enterMethod(String name, Object returned, Method method) {
            if (!first) {
                writer.print(", ");
            }
            writer.print(quote(returned));
            first = false;
        }

        @Override
        public void enterList(String name, Method method) {
            writer.print("[");
        }

        @Override
        public void exitList(String name) {
            writer.print("]");
            writer.flush();
        }

        @Override
        public void enterObject(String name, Object returned) {
            if (first) {
                first = false;
            } else {
                writer.print(", ");
            }
        }
    }

    public static String quote(Object toQuote) {
        return toQuote == null ? "" : "\"" + toQuote + "\"";
    }
}
