package fi.konstal.bullet_your_life.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.TypeConverters;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import fi.konstal.bullet_your_life.data.converter.DateConverter;
import fi.konstal.bullet_your_life.task.CardItem;
import fi.konstal.bullet_your_life.util.Helper;


/**
 * Holds all DayCard related date together
 *
 * @author Konsta Lehtinen
 * @version 1.0
 * @since 1.0
 */
@Entity(tableName = "DayCard", inheritSuperIndices = true, indices = {@Index(value = {"dateString"})})
public class DayCard extends NoteCard implements Serializable {

    @TypeConverters(DateConverter.class)
    private Date date;

    @ColumnInfo(name = "dateString")
    private String dateString;

    /**
     * The constructor
     *
     * @param title     Card title
     * @param date      Card Date
     * @param cardItems Card Items
     */
    @Ignore
    public DayCard(String title, Date date, CardItem... cardItems) {
        super(title, cardItems);
        this.date = date;
        SimpleDateFormat formater = new SimpleDateFormat("dd.MM.yyyy");
        this.dateString = formater.format(date);
    }

    /**
     * The constructor
     *
     * @param date the date by which to make the whole card
     */
    @Ignore
    public DayCard(Date date) {
        super();
        this.date = date;
        this.dateString = Helper.dateToString(date);
        setTitle(Helper.weekdayString(date));
    }

    /**
     * Empty constructor for DateBase related action
     */
    public DayCard() {
        super();
    }


    /**
     * Gets the cards date
     *
     * @return the date
     */
    public Date getDate() {
        return date;
    }

    /**
     * Sets the current date
     *
     * @param date the date
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * Gets the date as a string
     *
     * @return
     */
    public String getDateString() {
        return dateString;
    }

    /**
     * Sets the dateString
     *
     * @param dateString the dateString
     */
    public void setDateString(String dateString) {
        this.dateString = dateString;
    }

    /**
     * {@inheritDoc}
     */
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
