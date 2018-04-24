package fi.konstal.bullet_your_life.edit_recycler_view;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;
import android.util.Log;

import java.util.List;


import fi.konstal.bullet_your_life.task.CardItem;


/**
 * A Class that calculates and returns the differences between two CardItem lists
 *
 * @author Konsta Lehtinen
 * @version 1.0
 * @since 1.0
 */
public class CardItemListDiffCallback extends DiffUtil.Callback {
    private List<CardItem> mOldList;
    private List<CardItem> mNewList;

    /**
     * The Constuctor
     *
     * @param oldList the old list
     * @param newList the new List
     */
    public CardItemListDiffCallback(List<CardItem> oldList, List<CardItem> newList) {
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
    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        Log.i("CardItemListDiff", "Are items the same" + mNewList.get(newItemPosition).getId().equals(mOldList.get(oldItemPosition).getId()));
        return mNewList.get(newItemPosition).getId().equals(mOldList.get(oldItemPosition).getId());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        Log.i("CardItemListDiff", "Are content the same");
        //since images are not editable (CardImage) skip checking them
        if(mOldList.get(oldItemPosition).getType() == CardItem.CARD_IMAGE) {
            return true;
        }

        CardItem oldCard =  mOldList.get(oldItemPosition);
        CardItem newCard =  mNewList.get(newItemPosition);
        return oldCard.getText().equals(newCard.getText());
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        Log.i("CardItemListDiff", "get payload");
        //only text can change, so only include that
        CardItem newItem =  mNewList.get(newItemPosition);

        Bundle diffBundle = new Bundle();
        diffBundle.putSerializable("card_task_text", newItem.getText());


        if (diffBundle.size() == 0) return null;
        return diffBundle;
    }
}