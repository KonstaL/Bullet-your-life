package fi.konstal.bullet_your_life.notes_recycler_view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;
import android.util.Log;

import java.io.Serializable;
import java.util.List;

import fi.konstal.bullet_your_life.data.NoteCard;


public class NoteCardListDiffCallback extends DiffUtil.Callback {
    private List<NoteCard> mOldList;
    private List<NoteCard> mNewList;

    public NoteCardListDiffCallback(List<NoteCard> oldList, List<NoteCard> newList) {
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
        Log.i("test", "are items the same");
        return mNewList.get(newItemPosition).getId() == mOldList.get(oldItemPosition).getId();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        Log.i("test", "are contents the same");
        NoteCard oldCard = mOldList.get(oldItemPosition);
        NoteCard newCard = mNewList.get(newItemPosition);

        return (
                newCard.getCardItems() == oldCard.getCardItems() &&
                        newCard.getTitle().equals(oldCard.getTitle())
        );
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        NoteCard newCard = null;
        NoteCard oldCard = null;

        try {
            newCard = mNewList.get(newItemPosition);
            oldCard = mOldList.get(oldItemPosition);
        } catch (IndexOutOfBoundsException e) {
            // do nothing
        }

        Bundle diffBundle = new Bundle();

        if (newCard != null &&
                oldCard == null ||
                newCard.getCardItems().size() != oldCard.getCardItems().size()) {
            diffBundle.putSerializable("card_item_list", newCard.getCardItems());
        }
        if (newCard != null &&
                oldCard == null ||
                !newCard.getTitle().equals(oldCard.getTitle())) {
            diffBundle.putString("card_title", newCard.getTitle());
        }
        if (diffBundle.size() == 0) return null;
        return diffBundle;
    }
}
