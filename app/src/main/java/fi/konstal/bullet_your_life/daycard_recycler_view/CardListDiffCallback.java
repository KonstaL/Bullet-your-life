package fi.konstal.bullet_your_life.daycard_recycler_view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;
import android.util.Log;

import java.io.Serializable;
import java.util.List;

import fi.konstal.bullet_your_life.data.DayCard;

/**
 * A Class that calculates and returns the differences between two CardLists
 *
 * @author Konsta Lehtinen
 * @version 1.0
 * @since 1.0
 */
public class CardListDiffCallback extends DiffUtil.Callback {
    private List<DayCard> mOldList;
    private List<DayCard> mNewList;

    /**
     * The Constuctor
     *
     * @param oldList the old list
     * @param newList the new List
     */
    public CardListDiffCallback(List<DayCard> oldList, List<DayCard> newList) {
        this.mOldList = oldList;
        this.mNewList = newList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getOldListSize() {
        return mOldList != null ? mOldList.size() : 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getNewListSize() {
        return mNewList != null ? mNewList.size() : 0;
    }

    /**
     * {@inheritDoc}
     */
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        Log.i("test", "are items the same: " + Boolean.toString(mNewList.get(newItemPosition).getId() == mOldList.get(oldItemPosition).getId()));
        return mNewList.get(newItemPosition).getId() == mOldList.get(oldItemPosition).getId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        Log.i("test", "are contents the same");
        DayCard oldCard = mOldList.get(oldItemPosition);
        DayCard newCard = mNewList.get(newItemPosition);

        return (
                newCard.getCardItems() == oldCard.getCardItems() &&
                        newCard.getDateString().equals(oldCard.getDateString()) &&
                        newCard.getTitle().equals(oldCard.getTitle())
        );
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        DayCard newCard = mNewList.get(newItemPosition);
        DayCard oldCard = mOldList.get(oldItemPosition);
        Bundle diffBundle = new Bundle();

        if (newCard.getCardItems().size() != oldCard.getCardItems().size()) {
            diffBundle.putSerializable("card_item_list", newCard.getCardItems());
        }
        if (!newCard.getTitle().equals(oldCard.getTitle())) {
            diffBundle.putString("card_title", newCard.getTitle());
        }
        if (!newCard.getDateString().equals(oldCard.getDateString())) {
            diffBundle.putString("card_date_string", newCard.getDateString());
        }
        if (diffBundle.size() == 0) return null;
        return diffBundle;
    }
}