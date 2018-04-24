package fi.konstal.bullet_your_life.data.converter;


import android.arch.persistence.room.TypeConverter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Converter that converts Date to equivalent String and back for ROOM usage
 *
 * @author Konsta Lehtinen
 * @version 1.0
 * @since 1.0
 */
public class DateConverter {
    static DateFormat df = new SimpleDateFormat("dd.MM.yyyy");

    /**
     * Convert String to equivalent Date
     *
     * @param value The String value
     * @return the equivalent Date Value
     */
    @TypeConverter
    public static Date toDate(String value) {
        if (value == null) return null;

        try {
            return df.parse(value);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * Converts Date to a String
     *
     * @param date the date to be converted
     * @return the equivalent String value
     */
    @TypeConverter
    public static String toTimestamp(Date date) {
        if (date != null) {
            return df.format(date);
        }
        return null;
    }
}