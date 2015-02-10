package nemesis.form.parser;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Locale;
import nemesis.annotation.Description;
import nemesis.form.FormElementParser;
import nemesis.form.ParseException;

@Description("Date Value ISO LocalDate")
public class LocalDateParser implements FormElementParser<LocalDate> {

    @Override
    public LocalDate parse(String toParse, Locale locale) throws ParseException {
        try {
            
            return LocalDate.parse(toParse);
        } catch (DateTimeParseException e) {
            throw new ParseException(e);
        }
    }

}
