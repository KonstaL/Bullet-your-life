package fi.konstal.bullet_your_life.task;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import java.io.Serializable;

/**
 * Created by e4klehti on 29.3.2018.
 */

public abstract class CardItem implements Serializable {
    protected static int idCounter;
    private int id;

    protected CardItem() {
        id = idCounter++;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public abstract CardItem replicate();
    public abstract void buildView(Context context, ViewGroup parent, View.OnClickListener clickListener);
}
