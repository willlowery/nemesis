
package nemesis.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import nemesis.renderer.Renderer;
import nemesis.renderer.SimpleRenderer;

/**
 *
 * @author william.lowery@rocky.edu
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Handle {

    String method();      
    Class<? extends Renderer> as() default SimpleRenderer.class;
}
