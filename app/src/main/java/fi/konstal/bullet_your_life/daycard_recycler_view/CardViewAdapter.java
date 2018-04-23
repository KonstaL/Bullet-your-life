package fi.konstal.bullet_your_life.daycard_recycler_view;

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
import fi.konstal.bullet_your_life.task.CardItem;


/**
 * Created by konka on 14.3.2018.
 */

public class CardViewAdapter extends RecyclerView.Adapter<CardViewAdapter.MyViewHolder> {
    private List<DayCard> cardsList;
    private Context context;
    //private RecyclerViewClickListener rvClickListener;


    //public CardViewAdapter(Context context, RecyclerViewClickListener rvl) {
    public CardViewAdapter(Context context) {
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
       /* if(!holder.isInitialized) {
            DayCard dayCard = cardsList.get(position);
            holder.title.setText(dayCard.getTitle());

            List<CardItem> cardItems = dayCard.getCardItems();

            for (CardItem item : cardItems) {
                item.buildView(context, holder.cll, null);
            }
            holder.date.setText(dayCard.getDateString());
            holder.isInitialized = true;
        }*/
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position, List<Object> payloads) {


        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position);
        } else {
            Log.i("onBindViewHolder", "payload ei ollu tyhjä");

            Bundle o = (Bundle) payloads.get(0);
            DayCard card = cardsList.get(position);
            for (String key : o.keySet()) {
                if (key.equals("card_item_list")) {
                    card.setCardItems((CopyOnWriteArrayList<CardItem>) o.get(key)); // TODO: maybe put this inside a try catch
                    holder.cll.removeAllViews();
                    for(CardItem item : card.getCardItems()) {
                        item.buildView(context, holder.cll, null);
                    }
                } else if (key.equals("card_title")) {
                    String title = (String) o.get(key);
                    card.setTitle(title);
                    holder.title.setText(title);
                } else if (key.equals("card_date_string")) {
                    String date = (String) o.get(key);
                    card.setDateString(date);
                    holder.date.setText(date);
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return cardsList.size();
    }

    public List<DayCard> getCardList() {
        return cardsList;
    }

    public void updateCardList(List<DayCard> newList) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new CardListDiffCallback(cardsList, newList));
        cardsList.clear();
        cardsList.addAll(newList);
        diffResult.dispatchUpdatesTo(this);

    }


    public void setCardList(List<DayCard> newList) {
        this.cardsList = newList;
        notifyDataSetChanged();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


    //public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    public class MyViewHolder extends RecyclerView.ViewHolder {
        private boolean isInitialized;
        public TextView title, date;
        public CustomLinearLayout cll;
        private RecyclerViewClickListener rvListener;

        //public MyViewHolder(View view, RecyclerViewClickListener listener) {
        public MyViewHolder(View view) {
            super(view);
            isInitialized = false;
            //rvListener = listener;
            cll = view.findViewById(R.id.card_content_layout);
            title = view.findViewById(R.id.title);
            date = view.findViewById(R.id.card_date);
        }

        public void setCard(DayCard dayCard) {
            //if(!holder.isInitialized) {

                title.setText(dayCard.getTitle());
                date.setText(dayCard.getDateString());

                List<CardItem> cardItems = dayCard.getCardItems();

                if(isInitialized) {
                    cll.removeAllViews();
                }
                for (CardItem item : cardItems) {
                    item.buildView(context, cll, null);
                }
                isInitialized = true;
            //}
        }

       /* @Override
        public void onClick(View view) {
            rvListener.onClick(view, getAdapterPosition());
        }*/
    }
}

