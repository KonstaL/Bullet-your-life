package fi.konstal.bullet_your_life.recycler_view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import fi.konstal.bullet_your_life.R;
import fi.konstal.bullet_your_life.task.CardItem;


/**
 * Created by konka on 14.3.2018.
 */

public class CardViewAdapter extends RecyclerView.Adapter<CardViewAdapter.MyViewHolder> {
        private List<DayCard> cardsList;
        private Context context;
        private RecyclerViewClickListener rvClickListerner;


        public CardViewAdapter(Context context, RecyclerViewClickListener rvl,  List<DayCard> cardsList) {
            this.context = context;
            this.rvClickListerner = rvl;
            this.cardsList = cardsList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.day_card, parent, false);

            return new MyViewHolder(itemView, rvClickListerner);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            DayCard dayCard = cardsList.get(position);
            holder.title.setText(dayCard.getTitle());

            List<CardItem> cardItems = dayCard.getCardItems();
            Log.d("shit", cardItems.toString());
            for (CardItem item : cardItems) {
                item.buildView(context, holder.cll, null);
            }
            holder.date.setText(dayCard.getDateString());
        }

        @Override
        public int getItemCount() {
            return cardsList.size();
        }



    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView title, date;
        public CustomLinearLayout cll;
        private RecyclerViewClickListener rvListener;

        public MyViewHolder(View view, RecyclerViewClickListener listener) {
            super(view);

            rvListener = listener;
            cll = view.findViewById(R.id.card_content_layout);
            title = view.findViewById(R.id.title);
            date = view.findViewById(R.id.card_date);
        }

        @Override
        public void onClick(View view) {
            rvListener.onClick(view, getAdapterPosition());
        }
    }
}

