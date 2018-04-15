package fi.konstal.bullet_your_life.edit_recycler_view;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import fi.konstal.bullet_your_life.R;
import fi.konstal.bullet_your_life.fragment.FragmentInterface;
import fi.konstal.bullet_your_life.task.CardItem;


/**
 * Created by konka on 14.3.2018.
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

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.partial_framelayout, parent, false);

        return new ViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CardItem item = cardItemList.get(position);
        // temp fix for stacked views. I forgot that RecyclerView recycles ViewHolders..
        holder.frameLayout.removeAllViews();
        item.buildView(context, holder.frameLayout, null);

        /*if (!holder.isInitialized) {
            if (cardItemList.get(position).getType() == CardItem.CARD_IMAGE) {
                CardItem cardImage = cardItemList.get(position);

                final float scale = context.getResources().getDisplayMetrics().density;

                holder.imageView.getLayoutParams().height = (int) (250 * scale + 0.5f);
                holder.imageView.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
                holder.layout.removeView(holder.taskText);
                try {
                    //TODO do this properly
                    InputStream is = context.getContentResolver().openInputStream(cardImage.getImageUri());

                    holder.imageView.setImageBitmap(
                            Helper.getResizedBitmap(
                                    BitmapFactory.decodeStream(is),
                                    Helper.SCALE_BY_HEIGHT,
                                    300
                            )
                    );

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } else {

                CardItem cardTask = cardItemList.get(position);
                holder.taskText.setText(cardTask.getText());
                holder.imageView.setImageDrawable(context.getResources().getDrawable(cardTask.getTaskIconRef()));

            }
        }*/
    }

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


    @Override
    public int getItemCount() {
        return cardItemList.size();
    }


    public void addCardItem(CardItem cardItem) {
        List<CardItem> newList = new ArrayList<>();
        newList.addAll(cardItemList);
        newList.add(cardItem);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new CardItemListDiffCallback(cardItemList, newList));
        diffResult.dispatchUpdatesTo(this);
    }

    public void updateList(List<CardItem> newList) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new CardItemListDiffCallback(cardItemList, newList));
        cardItemList.clear();
        cardItemList.addAll(newList);

        diffResult.dispatchUpdatesTo(this);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


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

    @Override
    public void onItemDismiss(int position) {
        Log.i("joo", "dismisssssssss");
        cardItemHandler.deleteItem(position);
  /*      cardItemList.remove(position); */
        notifyItemRemoved(position);
    }


    //public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    public class ViewHolder extends RecyclerView.ViewHolder {
        /*public boolean isInitialized;
        public TextView taskText;*/
        public FrameLayout frameLayout;
       /* public ImageView imageView;*/
        //private RecyclerViewClickListener rvListener;

        //public ViewHolder(View view, RecyclerViewClickListener listener) {
        public ViewHolder(View view) {
            super(view);
            //isInitialized = false;
            frameLayout = view.findViewById(R.id.empty_framelayout);




            //rvListener = listener;
        }

       /* @Override
        public void onClick(View view) {
            rvListener.onClick(view, getAdapterPosition());
        }*/
    }
}

