package ch.supsi.webapp.web.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public interface DateConverter {

    default String convertDateFromLocalDateTimeToString(LocalDateTime inputDate, String format) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(format);
        return inputDate.format(dateTimeFormatter);
    }

    default String convertDateToPersonalisedFormat(String inputDate, String initialFormat, String finalFormat) {
        try {
            SimpleDateFormat inputDateFormat = new SimpleDateFormat(initialFormat);
            Date date = inputDateFormat.parse(inputDate);

            SimpleDateFormat outputDateFormat = new SimpleDateFormat(finalFormat);
            return outputDateFormat.format(date);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

}
