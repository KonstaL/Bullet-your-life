package fi.konstal.bullet_your_life.daycard_recycler_view;

import android.view.View;


/**
 * Listens to recyclerview item click
 */
public interface RecyclerViewClickListener {
        /**
         * Called on recyclerview item click
         * @param view The clicked items view
         * @param position The clicked items position
         */
        void onClick(View view, int position);
}
