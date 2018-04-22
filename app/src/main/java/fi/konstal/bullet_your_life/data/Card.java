package fi.konstal.bullet_your_life.data;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import fi.konstal.bullet_your_life.task.CardItem;

/**
 * Created by e4klehti on 21.4.2018.
 */

public interface Card {
    static final int CARD_TYPE_DAYCARD = 1;
    static final int CARD_TYPE_NOTECARD = 2;

    int getId();
    void setId(int id);

    CopyOnWriteArrayList<CardItem> getCardItems();
    void setCardItems(CopyOnWriteArrayList<CardItem> cardItems);
    void addCardItems(CardItem... items);
}
