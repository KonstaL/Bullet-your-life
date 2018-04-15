package fi.konstal.bullet_your_life.edit_recycler_view;

import fi.konstal.bullet_your_life.task.CardItem;

/**
 * Created by e4klehti on 15.4.2018.
 */

public interface CardItemHandler {
    void deleteItem(CardItem cardItem);
    void deleteItem(int i);
    void addItem(CardItem cardItem);
    void updateItem(CardItem cardItem);
}
