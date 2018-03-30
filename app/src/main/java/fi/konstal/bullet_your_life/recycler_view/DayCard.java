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
import java.util.Date;
import java.util.List;

import fi.konstal.bullet_your_life.data.DataConverter;
import fi.konstal.bullet_your_life.task.CardItem;
import fi.konstal.bullet_your_life.task.CardTask;


/**
 * Created by konka on 14.3.2018.
 */


@Entity(tableName = "DayCard")
public class DayCard implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @Ignore
    private static int idCounter;

    @ColumnInfo(name = "title")
    private String title;

    @Ignore
    Date date;

    @ColumnInfo(name = "dateString")
    String dateString;

    @TypeConverters(DataConverter.class)
    @ColumnInfo(name = "cardItems")
    private List<CardItem> cardItems;

    public DayCard(String title, Date date, CardItem... cardItems) {
        this.title = title;
        this.date = date;
        this.cardItems = new ArrayList<>();
        this.cardItems.addAll(Arrays.asList(cardItems));
        id = idCounter++;

        SimpleDateFormat formater = new SimpleDateFormat("dd.MM.yyyy");
        this.dateString = formater.format(date);
    }

    public DayCard(String title, String dateString, CardTask... cardItems) {
        this.title = title;
        this.dateString = dateString;
        this.cardItems = new ArrayList<>();
        this.cardItems.addAll(Arrays.asList(cardItems));
        id = idCounter++;
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

    public List<CardItem> getCardItems() {
        return cardItems;
    }

    public void setCardItems(List<CardItem> cardItems) {
        this.cardItems = cardItems;
    }
    public void addCardItems(CardItem... cardItems) {
        this.cardItems.addAll(Arrays.asList(cardItems));
    }

    public String getDateString() {
        return dateString;
    }

    public void setDateString(String dateString) {
        this.dateString = dateString;
    }
}
