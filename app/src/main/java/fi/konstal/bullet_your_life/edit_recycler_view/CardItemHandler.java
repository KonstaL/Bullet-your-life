package fi.konstal.bullet_your_life.edit_recycler_view;

import fi.konstal.bullet_your_life.task.CardItem;

/**
 * This interface helps manage card item data
 *
 * @author Konsta Lehtinen
 * @author KonstaL
 * @version 1.0
 * @since 1.0
 */
public interface CardItemHandler {
    /**
     * Deletes the given card
     *
     * @param cardItem the card item to be deleted
     */
    void deleteItem(CardItem cardItem);

    /**
     * Delete item by index
     *
     * @param i the index
     */
    void deleteItem(int i);

    /**
     * Add an card item
     *
     * @param cardItem the card item
     */
    void addItem(CardItem cardItem);

    /**
     * Update the given CardItem
     *
     * @param cardItem the card item were gonna update
     */
    void updateItem(CardItem cardItem);
}
