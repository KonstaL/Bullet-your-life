package fi.konstal.bullet_your_life.recycler_view;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import fi.konstal.bullet_your_life.data.DataConverter;
import fi.konstal.bullet_your_life.task.Task;


/**
 * Created by konka on 14.3.2018.
 */


@Entity(tableName = "DayCard")
public class DayCard implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "title")
    private String title;

    @Ignore
    Date date;

    @ColumnInfo(name = "dateString")
    String dateString;

    @TypeConverters(DataConverter.class)
    @ColumnInfo(name = "tasks")
    private List<Task> tasks;

    public DayCard(String title, Date date, Task... tasks) {

        this.title = title;
        this.date = date;
        this.tasks = new ArrayList<>();
        this.tasks.addAll(Arrays.asList(tasks));



        SimpleDateFormat formater = new SimpleDateFormat("dd.MM.yyyy");
        this.dateString = formater.format(date);
    }


    public DayCard(String title, String dateString, Task... tasks) {
        this.title = title;
        this.dateString = dateString;
        this.tasks = new ArrayList<>();
        this.tasks.addAll(Arrays.asList(tasks));
    }

    // Empty constructor for database actions
    public DayCard() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }
    public void addTasks(Task... tasks) {
        this.tasks.addAll(Arrays.asList(tasks));
    }

    public String getDateString() {
        return dateString;
    }

    public void setDateString(String dateString) {
        this.dateString = dateString;
    }
}
