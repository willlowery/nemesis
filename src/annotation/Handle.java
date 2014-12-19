
package annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import renderer.Renderer;
import renderer.SimpleRenderer;

/**
 *
 * @author william.lowery@rocky.edu
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Handle {

    String method();      
    Class<? extends Renderer> as() default SimpleRenderer.class;
}
