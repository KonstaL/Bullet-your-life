package fi.konstal.bullet_your_life.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import fi.konstal.bullet_your_life.task.CardItem;
import fi.konstal.bullet_your_life.util.Helper;


/**
 * Created by konka on 14.3.2018.
 */


@Entity(tableName = "DayCard", inheritSuperIndices = true, indices = {@Index(value = {"dateString"})})
public class DayCard extends NoteCard implements Serializable {


    @TypeConverters(DateConverter.class)
    private Date date;

    @ColumnInfo(name = "dateString")
    private String dateString;


    @Ignore
    public DayCard(String title, Date date, CardItem... cardItems) {
        super(title, cardItems);
        this.date = date;
        SimpleDateFormat formater = new SimpleDateFormat("dd.MM.yyyy");
        this.dateString = formater.format(date);
    }

    @Ignore
    public DayCard(String title, String dateString, CardItem... cardItems) {
        super(title, cardItems);
        this.dateString = dateString;
    }

    @Ignore
    public DayCard(Context context, Date date) {
        super();
        this.date = date;
        this.dateString = Helper.dateToString(date);
        setTitle(Helper.weekdayString(context, date));
    }

    // Empty constructor for database actions
    public DayCard() {
        super();
    }


    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }


    public String getDateString() {
        return dateString;
    }

    public void setDateString(String dateString) {
        this.dateString = dateString;
    }

    @Override
    public DayCard replicate() {

        CardItem[] newArray = new CardItem[getCardItems().size()];

        for (int i = 0; i < getCardItems().size(); i++) {
            newArray[i] = getCardItems().get(i).replicate();
        }

        DayCard newCard = new DayCard(getTitle(), date, newArray);
        newCard.setId(getId());

        return newCard;
    }
}
