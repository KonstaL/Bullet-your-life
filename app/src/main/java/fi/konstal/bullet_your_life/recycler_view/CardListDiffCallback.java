package fi.konstal.bullet_your_life.recycler_view;

/**
 * Created by e4klehti on 30.3.2018.
 */

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;

import java.io.Serializable;
import java.util.List;


public class CardListDiffCallback extends DiffUtil.Callback {
    private List<DayCard> mOldList;
    private List<DayCard> mNewList;

    public CardListDiffCallback(List<DayCard> oldList, List<DayCard> newList) {
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
        return mNewList.get(newItemPosition).getId() == mOldList.get(oldItemPosition).getId();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return mNewList.get(newItemPosition).equals(mOldList.get(oldItemPosition));
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        DayCard newCard = mNewList.get(newItemPosition);
        DayCard oldCard = mOldList.get(oldItemPosition);
        Bundle diffBundle = new Bundle();

        if (newCard.getCardItems().size() != oldCard.getCardItems().size()) {
            diffBundle.putSerializable("card_item_list", (Serializable) newCard.getCardItems());
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