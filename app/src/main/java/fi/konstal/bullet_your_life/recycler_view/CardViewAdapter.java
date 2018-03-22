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
import fi.konstal.bullet_your_life.task.Task;

/**
 * Created by konka on 14.3.2018.
 */

public class CardViewAdapter extends RecyclerView.Adapter<CardViewAdapter.MyViewHolder> {
        private List<DayCard> cardsList;
        private Context context;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView title, date, content;
            public CustomLinearLayout cll;

            public MyViewHolder(View view) {
                super(view);

                cll = view.findViewById(R.id.card_content_layout);
                title = (TextView) view.findViewById(R.id.title);
                content = (TextView) view.findViewById(R.id.card_content);
                date = (TextView) view.findViewById(R.id.card_date);
            }
        }


        public CardViewAdapter(Context context, List<DayCard> cardsList) {
            this.context = context;
            this.cardsList = cardsList;

        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.day_card, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            ;
            DayCard dayCard = cardsList.get(position);
            holder.title.setText(dayCard.getTitle());
            //holder.content.setText(dayCard.getContent());
            Task[] tasks = dayCard.getTasks();
            Log.d("shit", tasks.toString());
            for (Task task : tasks) {
                TextView tv = new TextView(context);
                tv.setCompoundDrawablesWithIntrinsicBounds(task.getTaskIconRef(), 0, 0, 0);
                tv.setText(task.getText());
                holder.cll.addView(tv);
            }
            holder.date.setText(dayCard.getDateString());
        }

        @Override
        public int getItemCount() {
            return cardsList.size();
        }
    }

