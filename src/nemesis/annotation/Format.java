package nemesis.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import nemesis.response.Formatter;

@Retention(RetentionPolicy.RUNTIME)
public @interface Format {

    String format() default ""; 
    Class<? extends Formatter> as();
    
}
