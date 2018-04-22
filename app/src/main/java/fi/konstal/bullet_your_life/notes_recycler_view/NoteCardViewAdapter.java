package fi.konstal.bullet_your_life.notes_recycler_view;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import fi.konstal.bullet_your_life.R;
import fi.konstal.bullet_your_life.data.DayCard;
import fi.konstal.bullet_your_life.data.NoteCard;
import fi.konstal.bullet_your_life.daycard_recycler_view.CardListDiffCallback;
import fi.konstal.bullet_your_life.daycard_recycler_view.CustomLinearLayout;
import fi.konstal.bullet_your_life.daycard_recycler_view.RecyclerViewClickListener;
import fi.konstal.bullet_your_life.task.CardItem;


/**
 * Created by konka on 14.3.2018.
 */

public class NoteCardViewAdapter extends RecyclerView.Adapter<NoteCardViewAdapter.MyViewHolder> {
    private List<NoteCard> cardsList;
    private Context context;
    //private RecyclerViewClickListener rvClickListener;


    //public CardViewAdapter(Context context, RecyclerViewClickListener rvl) {
    public NoteCardViewAdapter(Context context) {
        this.context = context;
        //this.rvClickListener = rvl;
        this.cardsList = new ArrayList<>();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.day_card, parent, false);

        //return new MyViewHolder(itemView, rvClickListener);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.setCard(cardsList.get(position));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position, List<Object> payloads) {
        Log.i("onBindViewHolder", "TÄÄLÄ");

        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position);
        } else {
            Log.i("onBindViewHolder", "payload ei ollu tyhjä");

            Bundle o = (Bundle) payloads.get(0);
            NoteCard card = cardsList.get(position);
            for (String key : o.keySet()) {
                if (key.equals("card_item_list")) {
                    card.setCardItems((CopyOnWriteArrayList<CardItem>) o.get(key)); // TODO: maybe put this inside a try catch
                    holder.cll.removeAllViews();
                    for (CardItem item : card.getCardItems()) {
                        item.buildView(context, holder.cll, null);
                    }
                } else if (key.equals("card_title")) {
                    String title = (String) o.get(key);
                    card.setTitle(title);
                    holder.title.setText(title);
                }
            }
        }
    }


    public List<NoteCard> getCardList() {
        return cardsList;
    }

    public void updateCardList(List<NoteCard> newList) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new NoteCardListDiffCallback(cardsList, newList));
        diffResult.dispatchUpdatesTo(this);
    }

    public void setCardList(List<NoteCard> newList) {
        cardsList.clear();
        cardsList.addAll(newList);
        notifyDataSetChanged();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return cardsList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


    //public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    public class MyViewHolder extends RecyclerView.ViewHolder {
        private boolean isInitialized;
        public TextView title;
        public CustomLinearLayout cll;
        private RecyclerViewClickListener rvListener;

        //public MyViewHolder(View view, RecyclerViewClickListener listener) {
        public MyViewHolder(View view) {
            super(view);
            isInitialized = false;
            //rvListener = listener;
            cll = view.findViewById(R.id.card_content_layout);
            title = view.findViewById(R.id.title);

        }

        public void setCard(NoteCard card) {
            if(!isInitialized) {
                title.setText(card.getTitle());

                List<CardItem> cardItems = card.getCardItems();

                for (CardItem item : cardItems) {
                    item.buildView(context, cll, null);
                }
                isInitialized = true;
            }

        }
       /* @Override
        public void onClick(View view) {
            rvListener.onClick(view, getAdapterPosition());
        }*/
    }
}

