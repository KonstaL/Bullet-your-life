package fi.konstal.bullet_your_life;

import android.content.Context;
import android.util.Log;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by e4klehti on 22.3.2018.
 */

public class Helper  {
    public static String weekdayString(Context context, Date date) {

        Calendar c = Calendar.getInstance();
        c.setTime(date);

        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);

        switch (dayOfWeek) {
            case 1: return context.getString(R.string.sun);
            case 2: return context.getString(R.string.mon);
            case 3: return context.getString(R.string.tue);
            case 4: return context.getString(R.string.wed);
            case 5: return context.getString(R.string.thu);
            case 6: return context.getString(R.string.fri);
            case 7: return context.getString(R.string.sat);
            default: return "Error";
        }
    }
}
