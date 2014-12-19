
package nemesis.form;

/**
 *
 * @author william.lowery@rocky.edu
 */
public interface FormValidator<T extends Form> {

    public void validate(T form);
    
}
