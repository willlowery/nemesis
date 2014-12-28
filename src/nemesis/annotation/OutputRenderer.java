package nemesis.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface OutputRenderer {

    public enum TYPE{
        EXCEPTION,
        NON_FOUND,
        FILE
    }
    
    TYPE value();
    
}
