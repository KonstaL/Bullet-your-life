package fi.konstal.bullet_your_life.data;

import java.util.concurrent.CopyOnWriteArrayList;

import fi.konstal.bullet_your_life.task.CardItem;


/**
 * Interface for all Card Objects
 */
public interface Card {
    static final int CARD_TYPE_DAYCARD = 1;
    static final int CARD_TYPE_NOTECARD = 2;

    /**
     * Returns the cards database generated ID
     * @return the id
     */
    int getId();

    /**
     * Sets the cards database generated ID
     * @return the id
     */
    void setId(int id);

    /**
     * Returns all the cards CardItems
     * @return CardItems
     */
    CopyOnWriteArrayList<CardItem> getCardItems();

    /**
     * Sets the Cards cardItems
     *
     * @param cardItems the card items
     */
    void setCardItems(CopyOnWriteArrayList<CardItem> cardItems);

    /**
     * Adds CardItems to the card
     * @param items the added carditems
     */
    void addCardItems(CardItem... items);
}
