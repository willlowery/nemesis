
package nemesis.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author william.lowery@rocky.edu
 */
public class StreamUtilTest {
    
    @Test
    public void testCopy() throws IOException {
        String buffer = "apple";
        for(int i =0; i < 8; i++){
            buffer += buffer;
        }
        ByteArrayInputStream in = new ByteArrayInputStream(buffer.getBytes());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        StreamUtil.copy(in, out);        
        assertThat(buffer, is(out.toString()));
    }
    
}
