package fi.konstal.bullet_your_life.edit_recycler_view;

/**
 * Created by e4klehti on 1.4.2018.
 */

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;

import java.util.List;


import fi.konstal.bullet_your_life.task.CardItem;



public class CardItemListDiffCallback extends DiffUtil.Callback {
    private List<CardItem> mOldList;
    private List<CardItem> mNewList;

    public CardItemListDiffCallback(List<CardItem> oldList, List<CardItem> newList) {
        this.mOldList = oldList;
        this.mNewList = newList;
    }

    @Override
    public int getOldListSize() {
        return mOldList != null ? mOldList.size() : 0;
    }

    @Override
    public int getNewListSize() {
        return mNewList != null ? mNewList.size() : 0;
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return mNewList.get(newItemPosition).equals(mOldList.get(oldItemPosition));
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        //since images are not editable (CardImage) skip checking them
        if(mOldList.get(oldItemPosition).getType() == CardItem.CARD_IMAGE) {
            return true;
        }

        CardItem oldCard =  mOldList.get(oldItemPosition);
        CardItem newCard =  mNewList.get(newItemPosition);
        return oldCard.getText().equals(newCard.getText());
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        //only text can change, so only include that
        CardItem newItem =  mNewList.get(newItemPosition);

        Bundle diffBundle = new Bundle();
        diffBundle.putSerializable("card_task_text", newItem.getText());


        if (diffBundle.size() == 0) return null;
        return diffBundle;
    }
}