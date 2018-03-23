package fi.konstal.bullet_your_life.recycler_view;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import fi.konstal.bullet_your_life.task.Task;


/**
 * Created by konka on 14.3.2018.
 */

public class DayCard implements Serializable {
    String title;
    Date date;
    Task[] tasks;
    String dateString;

    public DayCard(String title, Date date, Task... tasks) {
        this.title = title;
        this.tasks = tasks;
        this.date = date;


        SimpleDateFormat formater = new SimpleDateFormat("dd.MM.yyyy");
        this.dateString = formater.format(date);
    }


    public DayCard(String title, String dateString, Task... tasks) {
        this.title = title;
        this.tasks = tasks;
        this.dateString = dateString;

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

    public Task[] getTasks() {
        return tasks;
    }

    public void setTasks(Task[] tasks) {
        this.tasks = tasks;
    }

    public String getDateString() {
        return dateString;
    }

    public void setDateString(String dateString) {
        this.dateString = dateString;
    }
}
