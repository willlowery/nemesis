package nemesis.form.parser;

import java.util.Locale;
import nemesis.form.ParseException;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author william.lowery@rocky.edu
 */
public class IntegerParserTest {

    @Test
    public void testParse() throws ParseException {
        IntegerParser parser = new IntegerParser();
        assertThat(parser.parse("1", Locale.FRENCH), is(1));
        assertThat(parser.parse("100", Locale.FRENCH), is(100));
    }

    @Test(expected = ParseException.class)
    public void testParseFails() throws ParseException {
        IntegerParser parser = new IntegerParser();
        parser.parse("1.1", Locale.FRENCH);

    }

}
