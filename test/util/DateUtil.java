
package util;

import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author william.lowery@rocky.edu
 */
public class DateUtil {
    
    public static Date getDate(int year, int month, int days) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month, days);
        return cal.getTime();
    }
    
}
