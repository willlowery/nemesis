package annotation.response.formatter;

import java.util.Date;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import org.junit.Test;
import util.DateUtil;

/**
 *
 * @author william.lowery@rocky.edu
 */
public class SimpleDateFormatterTest {

    SimpleDateFormatter formatter = new SimpleDateFormatter();

    @Test
    public void testFormatNull_ReturnsNull() throws NoSuchMethodException {
        Object format = formatter.format(null, null);
        assertNull(format);
    }

    @Test
    public void testFormatNonDate_ReturnsNonDate() throws NoSuchMethodException {
        Object toFormat = "a";
        Object formatted = formatter.format(toFormat, null);
        assertThat(formatted, is(toFormat));
    }

    @Test
    public void testFormatDate_ReturnsFormattedDate() throws NoSuchMethodException {
        Date toFormat = DateUtil.getDate(2014, 0, 1);
        Object formatted = formatter.format(toFormat, getAnnotationFrom("a"));
        assertThat(formatted, is((Object) "2014"));
    }

    @Test
    public void testInvalidDateFormat_ReturnsToFormat() throws NoSuchMethodException {
        Date toFormat = DateUtil.getDate(2014, 0, 1);
        Object formatted = formatter.format(toFormat, getAnnotationFrom("b"));
        assertThat(formatted, is((Object) toFormat));
    }
    
    private FormatDate getAnnotationFrom(String methodName) throws NoSuchMethodException, SecurityException {
        return SimpleDateFormatterTest.class.getMethod(methodName).getAnnotation(FormatDate.class);
    }

    

    @FormatDate("YYYY")
    public Date a() {
        return null;
    }

    @FormatDate("sadergs9b339o")
    public Date b() {
        return null;
    }
}
