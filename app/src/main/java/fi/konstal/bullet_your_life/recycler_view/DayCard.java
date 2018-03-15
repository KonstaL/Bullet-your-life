package fi.konstal.bullet_your_life.recycler_view;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * Created by konka on 14.3.2018.
 */

public class DayCard {
    String title;
    Date date;
    String content;
    String dateString;

    public DayCard(String title, String content) {
        this.title = title;
        this.content = content;
        this.date = Calendar.getInstance().getTime();


        SimpleDateFormat formater = new SimpleDateFormat("dd.MM.yyyy");
        this.dateString = formater.format(date);
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDateString() {
        return dateString;
    }

    public void setDateString(String dateString) {
        this.dateString = dateString;
    }
}
