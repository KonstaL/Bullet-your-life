package fi.konstal.bullet_your_life.data;

/**
 * Created by e4klehti on 2.4.2018.
 */

import android.arch.persistence.room.TypeConverter;

import java.util.Date;

class DateConverter {

    @TypeConverter
    public static Date toDate(Long timestamp) {
        return timestamp == null ? null : new Date(timestamp);
    }

    @TypeConverter
    public static Long toTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }
}