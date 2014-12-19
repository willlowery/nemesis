
package nemesis.form;

/**
 *
 * @author william.lowery@rocky.edu
 */
public interface FormElementValidator<Type> {
    public void validate(String fieldName, Type t);
}
