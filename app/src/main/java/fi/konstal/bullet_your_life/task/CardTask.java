package fi.konstal.bullet_your_life.task;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.Serializable;

import fi.konstal.bullet_your_life.R;

/**
 * Created by konka on 22.3.2018.
 */

public class CardTask extends CardItem implements Serializable {
    private String text;
    private boolean done;
    private int taskIconRef;

    public CardTask(String text, int taskIconRef) {
        super();
        this.text = text;
        this.done = false;
        this.taskIconRef = taskIconRef;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public int getTaskIconRef() {
        return taskIconRef;
    }

    public void setTaskIconRef(int taskIconRef) {
        this.taskIconRef = taskIconRef;
    }


    @Override
    public CardItem replicate() {
        return new CardTask(text, taskIconRef);
    }

    @Override
    public void buildView(Context context, ViewGroup parent, View.OnClickListener onClickListener) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.partial_card_item_task, null);

        ImageView icon = view.findViewById(R.id.task_icon);
        icon.setImageResource(taskIconRef);
        TextView tv = view.findViewById(R.id.task_text);
        tv.setText(text);


        view.setOnClickListener(onClickListener);

        parent.addView(view);
    }
}
