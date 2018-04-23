package fi.konstal.bullet_your_life.data;

/**
 * Created by e4klehti on 2.4.2018.
 */

import android.arch.persistence.room.TypeConverter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

class DateConverter {
    static DateFormat df = new SimpleDateFormat("dd.MM.yyyy");

    @TypeConverter
    public static Date toDate(String value) {
        if (value != null) {
            try {
                return df.parse(value);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @TypeConverter
    public static String toTimestamp(Date date) {
        if(date != null) {
            return df.format(date);
        }
        return null;
    }
}