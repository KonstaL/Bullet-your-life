package fi.konstal.bullet_your_life.daycard_recycler_view;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import fi.konstal.bullet_your_life.R;
import fi.konstal.bullet_your_life.data.DayCard;
import fi.konstal.bullet_your_life.fragment.MonthlyLogFragment;
import fi.konstal.bullet_your_life.fragment.NotesFragment;
import fi.konstal.bullet_your_life.fragment.WeeklyLogFragment;
import fi.konstal.bullet_your_life.task.CardItem;


/**
 * This adapter holds Views for each Card in {@link WeeklyLogFragment}
 *
 * @author Konsta Lehtinen
 * @version 1.0
 * @see WeeklyLogFragment
 * @see MonthlyLogFragment
 * @see NotesFragment
 * @since 1.0
 */
public class CardViewAdapter extends RecyclerView.Adapter<CardViewAdapter.MyViewHolder> {
    private List<DayCard> cardsList;
    private Context context;

    /**
     * Constructor
     * @param context context for layout inflator usage
     */
    public CardViewAdapter(Context context) {
        this.context = context;
        this.cardsList = new ArrayList<>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.day_card, parent, false);

        return new MyViewHolder(itemView);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.setCard(cardsList.get(position));
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position, List<Object> payloads) {

        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position);
        } else {
            Bundle o = (Bundle) payloads.get(0);
            DayCard card = cardsList.get(position);
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
                } else if (key.equals("card_date_string")) {
                    String date = (String) o.get(key);
                    card.setDateString(date);
                    holder.date.setText(date);
                }
            }
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public int getItemCount() {
        return cardsList.size();
    }

    /**
     * Gets the adapters data list
     * @return the list of Cards
     */
    public List<DayCard> getCardList() {
        return cardsList;
    }

    /**
     * Sets the adapters cardlist and notifies about dataset changing
     * @param newList the list of data
     */
    public void setCardList(List<DayCard> newList) {
        this.cardsList = newList;
        notifyDataSetChanged();
    }

    /**
     * Updates only the changed Cards using {@link CardListDiffCallback}
     * @param newList New list of daycards
     */
    public void updateCardList(List<DayCard> newList) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new CardListDiffCallback(cardsList, newList));
        cardsList.clear();
        cardsList.addAll(newList);
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
    public int getItemViewType(int position) {
        return position;
    }


    /**
     * This class holds the Recyclerviews View
     */
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, date;
        public CustomLinearLayout cll;
        private boolean isInitialized;
        private RecyclerViewClickListener rvListener;

        /**
         * Constructor
         * @param view the anchor view
         */
        public MyViewHolder(View view) {
            super(view);
            isInitialized = false;
            //rvListener = listener;
            cll = view.findViewById(R.id.card_content_layout);
            title = view.findViewById(R.id.title);
            date = view.findViewById(R.id.card_date);
        }

        /**
         * Configs the
         * @param dayCard
         */
        public void setCard(DayCard dayCard) {
            title.setText(dayCard.getTitle());
            date.setText(dayCard.getDateString());

            List<CardItem> cardItems = dayCard.getCardItems();

            if (isInitialized) {
                cll.removeAllViews();
            }
            for (CardItem item : cardItems) {
                item.buildView(context, cll, null);
            }
            isInitialized = true;

        }
    }
}

