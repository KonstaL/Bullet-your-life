package fi.konstal.bullet_your_life.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.PopupMenu;
import android.view.MenuInflater;
import android.view.View;

import fi.konstal.bullet_your_life.App;
import fi.konstal.bullet_your_life.R;

/**
 * Created by e4klehti on 15.4.2018.
 */

public class PopUpHandler implements View.OnClickListener {

    View anchor;
    Context context;

    public PopUpHandler(Context context, View anchor) {
        this.anchor = anchor;
        this.context = context;
    }

    @Override
    public void onClick( View view) {
        PopupMenu popup = new PopupMenu(context, anchor);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_popup, popup.getMenu());
        popup.show();
        popup.setOnMenuItemClickListener((item -> {
            SharedPreferences preferences = context.getSharedPreferences("bullet_your_life", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();


            switch (item.getItemId()) {
                case R.id.popup_logout:
                    editor.putBoolean("init_done", false);
                    editor.commit();
                    break;
                case R.id.popup_login:

                    editor.putBoolean("init_done", false);
                    editor.putBoolean("is_auth", false);
                    editor.commit();
                    break;
            }

            return false;
        }));
    }

}
