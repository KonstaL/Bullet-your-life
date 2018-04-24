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
 * Helper Class to simplify {@link PopupMenu} usage
 *
 * @author Konsta Lehtinen
 * @version 1.0
 * @since 1.0
 */
public class PopUpHandler implements View.OnClickListener {

    View anchor;
    Context context;

    /**
     * Constructor
     *
     * @param context Used for Login / Logout
     * @param anchor  Anchor view, from which the popup is showed
     */
    public PopUpHandler(Context context, View anchor) {
        this.anchor = anchor;
        this.context = context;
    }

    /**
     * Fired when user the "open menu" button.
     * Inflates the view and adds clicklisteners to it
     *
     * @param view The clicked view
     */
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
