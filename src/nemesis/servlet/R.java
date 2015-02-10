package nemesis.servlet;

import java.util.ArrayList;
import java.util.List;
import nemesis.annotation.Element;

@Element("Resource")
public class R {

    String resource;
    String description;
    List<M> methods;

    public R() {
        methods = new ArrayList<>();
    }

    @Element("name")
    public String getResource() {
        return resource;
    }

    @Element("methods")
    public List<M> getMethods() {
        return methods;
    }

    @Element("Method")
    public static class M {

        String method;
        List<F> fields;
        List<String> formValidators;

        public M() {
            fields = new ArrayList<>();
            formValidators = new ArrayList<>();
        }

        @Element("name")
        public String getMethod() {
            return method;
        }

        @Element("fields")
        public List<F> getFields() {
            return fields;
        }

    }

    @Element("Field")
    public static class F {

        String name;
        String type;
        String validators;

        

        @Element("name")
        public String getName() {
            return name;
        }

        @Element("type")
        public String getType() {
            return type;
        }

        @Element("validators")
        public String getValidators() {
            return validators;
        }

    }

}
