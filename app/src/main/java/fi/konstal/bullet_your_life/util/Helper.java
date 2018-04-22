package fi.konstal.bullet_your_life.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

import fi.konstal.bullet_your_life.R;
import fi.konstal.bullet_your_life.data.CardRepository;
import fi.konstal.bullet_your_life.data.DayCard;
import fi.konstal.bullet_your_life.task.CardItem;


/**
 * Created by e4klehti on 22.3.2018.
 */

public class Helper  {
    public static final String TAG = "Helper";
    public static final int SCALE_BY_HEIGHT = 200;
    public static final int SCALE_BY_WIDTH = 300;

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

    public static String dateToString(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        return dateFormat.format(date);
    }

    public static String currentDateString() {
        Date date = new Date();
        return dateToString(date);
    }


    public static Bitmap getResizedBitmap(Bitmap image, int scaleBy, int newSize) {
        int width = image.getWidth();
        int height = image.getHeight();
        double scale;

        if(scaleBy == SCALE_BY_HEIGHT) {
            scale = ((double)newSize) / ((double)height);
        } else if (scaleBy == SCALE_BY_WIDTH){
            scale = newSize/width;
        } else {
            throw new RuntimeException("You cannot use custom scaleBy parameters!");
        }

        int newWidth =(int)(width*scale);
        int newHeight = (int)(height*scale);

        return Bitmap.createScaledBitmap(image, newWidth, newHeight, true);
    }

    public static void seedCardData(Context context, CardRepository cardRepository) {
        DayCard[] dayCards = new DayCard[7];
        Date date = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(date);

        // add 7 days of cards and examples
        for(int i = 0; i < 7; i++) {
            date = c.getTime();

            CardItem item1 = new CardItem(context.getString(R.string.example_task), R.drawable.ic_task_12dp);
            CardItem item2 = new CardItem(context.getString(R.string.example_event), R.drawable.ic_hollow_circle_16dp);

            dayCards[i] = (
                new DayCard(
                    Helper.weekdayString(context, date),
                    date,
                    item1,
                    item2
                )
            );

            //move to the next day
            c.add(Calendar.DATE, 1);
        }

        cardRepository.insertDayCards(dayCards);
    }
}
