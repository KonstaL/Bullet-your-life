package fi.konstal.bullet_your_life.data;

import java.util.List;

import fi.konstal.bullet_your_life.task.CardItem;

/**
 * Created by e4klehti on 21.4.2018.
 */

public interface Card {
    static final int CARD_TYPE_DAYCARD = 1;
    static final int CARD_TYPE_NOTECARD = 2;

    int getId();
    void setId(int id);

    List<CardItem> getCardItems();
    void setCardItems(List<CardItem> cardItems);
    void addCardItems(CardItem... items);
}
