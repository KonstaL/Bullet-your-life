package fi.konstal.bullet_your_life.data;


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
 * Class that holds all NoteCard Related data
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

    /**
     *
     * @param title
     * @param cardItems
     */
    @Ignore
    public NoteCard(String title, CardItem... cardItems) {
        this.title = title;

        this.cardItems = new CopyOnWriteArrayList<>();
        this.cardItems.addAll(Arrays.asList(cardItems));
    }

    /**
     *
     * @param title
     */
    @Ignore
    public NoteCard(String title) {
        this.title = title;
        this.cardItems = new CopyOnWriteArrayList<>();
    }

    /**
     *   Empty constructor for database
     */
    public NoteCard() {
        this.cardItems = new CopyOnWriteArrayList<>();
    }

    /**
     *
     * @return
     */
    public int getId() {
        return id;
    }

    /**
     *
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     *
     * @return
     */
    public String getTitle() {
        return title;
    }

    /**
     *
     * @param title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     *
     * @return
     */
    public CopyOnWriteArrayList<CardItem> getCardItems() {
        return cardItems;
    }

    /**
     *
     * @param cardItems the card items
     */
    public void setCardItems(CopyOnWriteArrayList<CardItem> cardItems) {
        this.cardItems = cardItems;
    }

    /**
     *
     * @param cardItems
     */
    public void addCardItems(CardItem... cardItems) {
        this.cardItems.addAll(Arrays.asList(cardItems));
    }


    /**
     * Replicates the Card
     *
     * @return replicated Card
     */
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

