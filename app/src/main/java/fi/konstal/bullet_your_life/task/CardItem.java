package fi.konstal.bullet_your_life.task;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import java.io.Serializable;

/**
 * Created by e4klehti on 29.3.2018.
 */

public interface CardItem extends Serializable {
    void buildView(Context context, ViewGroup parent, View.OnClickListener clickListener);
}
