package fi.konstal.bullet_your_life.util;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.PopupMenu;
import android.view.MenuInflater;
import android.view.View;

import fi.konstal.bullet_your_life.R;
import fi.konstal.bullet_your_life.activities.BaseActivity;
import fi.konstal.bullet_your_life.activities.LoginActivity;

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
    public void onClick(View view) {
        SharedPreferences preferences = context.getSharedPreferences("bullet_your_life", Context.MODE_PRIVATE);

        PopupMenu popup = new PopupMenu(context, anchor);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_popup, popup.getMenu());

        if (preferences.getBoolean("is_auth", false)) {
            popup.getMenu().add("Logout");
        } else {
            popup.getMenu().add("Login");
        }

        popup.show();
        popup.setOnMenuItemClickListener((item -> {

            SharedPreferences.Editor editor = preferences.edit();

            if (item.getTitle().equals("Login")) {
                Intent intent = new Intent(context, LoginActivity.class);
                context.startActivity(intent);
            } else if (item.getTitle().equals("Logout")) {
                BaseActivity baseActivity = ((BaseActivity) context);
                baseActivity.signOut();
                editor.putBoolean("init_done", false);
                editor.putBoolean("is_auth", false);
                editor.apply();
                Intent intent = new Intent(context, LoginActivity.class);
                context.startActivity(intent);
            }
            return false;
        }));
    }

}
