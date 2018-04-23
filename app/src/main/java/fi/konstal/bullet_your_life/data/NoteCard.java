package fi.konstal.bullet_your_life.data;

/**
 * Created by e4klehti on 21.4.2018.
 */

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import java.io.Serializable;
import java.util.Arrays;
import java.util.concurrent.CopyOnWriteArrayList;

import fi.konstal.bullet_your_life.data.converter.CardItemConverter;
import fi.konstal.bullet_your_life.task.CardItem;


/**
 * Created by konka on 14.3.2018.
 */


@Entity(tableName = "NoteCard", indices = {@Index(value = {"id", "title"})})
public class NoteCard implements Card, Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "title")
    private String title;

    @TypeConverters(CardItemConverter.class)
    @ColumnInfo(name = "cardItems")
    private CopyOnWriteArrayList<CardItem> cardItems;

    @Ignore
    public NoteCard(String title, CardItem... cardItems) {
        this.title = title;

        this.cardItems = new CopyOnWriteArrayList<>();
        this.cardItems.addAll(Arrays.asList(cardItems));
    }

    @Ignore
    public NoteCard(String title) {
        this.title = title;
        this.cardItems = new CopyOnWriteArrayList<>();
    }

    // Empty constructor for database
    public NoteCard() {
        this.cardItems = new CopyOnWriteArrayList<>();
    }

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


    public CopyOnWriteArrayList<CardItem> getCardItems() {
        return cardItems;
    }

    public void setCardItems(CopyOnWriteArrayList<CardItem> cardItems) {
        this.cardItems = cardItems;
    }

    public void addCardItems(CardItem... cardItems) {
        this.cardItems.addAll(Arrays.asList(cardItems));
    }


    public NoteCard replicate() {
        CardItem[] newArray = new CardItem[cardItems.size()];

        for (int i = 0; i < cardItems.size(); i++) {
            newArray[i] = cardItems.get(i).replicate();
        }

        NoteCard newCard = new NoteCard(title, newArray);
        newCard.setId(this.id);

        return newCard;
    }
}

