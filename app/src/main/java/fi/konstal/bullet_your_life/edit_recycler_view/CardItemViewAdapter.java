package fi.konstal.bullet_your_life.edit_recycler_view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import fi.konstal.bullet_your_life.Helper;
import fi.konstal.bullet_your_life.R;
import fi.konstal.bullet_your_life.task.CardImage;
import fi.konstal.bullet_your_life.task.CardItem;
import fi.konstal.bullet_your_life.task.CardTask;


/**
 * Created by konka on 14.3.2018.
 */

public class CardItemViewAdapter extends RecyclerView.Adapter<CardItemViewAdapter.ViewHolder>
    implements ItemTouchHelperAdapter {

    private List<CardItem> cardItemList;
    private Context context;
    private RecyclerViewClickListener rvClickListerner;


    public CardItemViewAdapter(Context context, RecyclerViewClickListener rvl, List<CardItem> cardItemList) {
        this.context = context;
        this.rvClickListerner = rvl;
        this.cardItemList = cardItemList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.partial_card_item_task, parent, false);

        //return new ViewHolder(itemView, rvClickListerner);
        return new ViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(!holder.isInitialized) {
            if(cardItemList.get(position) instanceof CardImage) {
                CardImage cardImage = (CardImage) cardItemList.get(position);

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

                CardTask cardTask = (CardTask) cardItemList.get(position);
                holder.taskText.setText(cardTask.getText());
                holder.imageView.setImageDrawable(context.getResources().getDrawable(cardTask.getTaskIconRef()));

            }
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position, List<Object> payloads) {
        Log.i("onBindViewHolder", "TÄÄLÄ");

        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position);
        } else {
            Bundle o = (Bundle) payloads.get(0);
            CardTask cardItem = (CardTask) cardItemList.get(position);
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
        cardItemList.remove(position);
        notifyItemRemoved(position);
    }


    //public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
     public class ViewHolder extends RecyclerView.ViewHolder {
        public boolean isInitialized;
        public TextView taskText;
        public LinearLayout layout;
        public ImageView imageView;
        //private RecyclerViewClickListener rvListener;

        //public ViewHolder(View view, RecyclerViewClickListener listener) {
        public ViewHolder(View view) {
            super(view);
            isInitialized = false;
            layout = view.findViewById(R.id.task_layout);
            taskText = view.findViewById(R.id.task_text);
            imageView = view.findViewById(R.id.task_icon);

            //rvListener = listener;
        }

       /* @Override
        public void onClick(View view) {
            rvListener.onClick(view, getAdapterPosition());
        }*/
    }
}

