package nemesis.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface ConfigureClass {

    public enum TYPE{
        EXCEPTION,
        NON_FOUND,
        FILE,
        SECURITY
    }
    
    TYPE value();
    
}
