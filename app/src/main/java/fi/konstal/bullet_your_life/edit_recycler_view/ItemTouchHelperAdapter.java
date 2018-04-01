package fi.konstal.bullet_your_life.edit_recycler_view;

/**
 * Created by e4klehti on 1.4.2018.
 */

public interface ItemTouchHelperAdapter {
    boolean onItemMove(int fromPosition, int toPosition);
    void onItemDismiss(int position);
}

