
package renderer;

/**
 *
 * @author william.lowery@rocky.edu
 */
public class Renderers {
    
    public static final Class<? extends Renderer> JSON = JSONResponseRenderer.class;
    public static final Class<? extends Renderer> STREAM = StreamRenderer.class;
    
}
