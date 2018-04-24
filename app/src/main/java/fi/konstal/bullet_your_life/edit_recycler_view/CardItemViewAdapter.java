package fi.konstal.bullet_your_life.edit_recycler_view;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.Collections;
import java.util.List;

import fi.konstal.bullet_your_life.R;
import fi.konstal.bullet_your_life.task.CardItem;

/**
 * This class handles the recyclerview and card item adapting
 *
 * @author Konsta Lehtinen
 * @author KonstaL
 * @version 1.0
 * @since 1.0
 */
public class CardItemViewAdapter extends RecyclerView.Adapter<CardItemViewAdapter.ViewHolder>
        implements ItemTouchHelperAdapter {

    private List<CardItem> cardItemList;
    private Context context;
    private RecyclerViewClickListener rvClickListerner;
    private CardItemHandler cardItemHandler;

    public CardItemViewAdapter(Context context, CardItemHandler cardItemHandler, RecyclerViewClickListener rvl, List<CardItem> cardItemList) {
        this.context = context;
        this.rvClickListerner = rvl;
        this.cardItemList = cardItemList;
        this.cardItemHandler = cardItemHandler;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.partial_framelayout, parent, false);

        return new ViewHolder(itemView);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CardItem item = cardItemList.get(position);

        holder.frameLayout.removeAllViews();
        item.buildView(context, holder.frameLayout, null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position, List<Object> payloads) {

        if (payloads == null || payloads.isEmpty()) {
            onBindViewHolder(holder, position);
        } else {
            Log.i("onBindViewHolder", "payload vastaanotettu");
            Bundle o = (Bundle) payloads.get(0);
            CardItem cardItem = cardItemList.get(position);
            for (String key : o.keySet()) {
                if (key.equals("card_task_text")) {
                    cardItem.setText((String) o.get(key));
                }
            }
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public int getItemCount() {
        return cardItemList.size();
    }


    /**
     * Update the changed values in the adapter using {@link CardItemListDiffCallback}
     *
     * @param newList The new list, which were gonna compare to the old one
     */
    public void updateList(List<CardItem> newList) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new CardItemListDiffCallback(cardItemList, newList));
        cardItemList.clear();
        cardItemList.addAll(newList);
        diffResult.dispatchUpdatesTo(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getItemId(int position) {
        return position;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(cardItemList, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(cardItemList, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onItemDismiss(int position) {
        cardItemHandler.deleteItem(position);
        notifyItemRemoved(position);
    }


    /**
     * The ViewHolder class for {@link CardItemViewAdapter}
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        public FrameLayout frameLayout;

        public ViewHolder(View view) {
            super(view);
            frameLayout = view.findViewById(R.id.empty_framelayout);
        }
    }
}

