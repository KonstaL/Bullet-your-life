package fi.konstal.bullet_your_life.edit_recycler_view;

/**
 * This interface helps listen to more complex touch events
 *
 * @author Konsta Lehtinen
 * @author KonstaL
 * @version 1.0
 * @since 1.0
 */
public interface ItemTouchHelperAdapter {

    /**
     * Called when item is moved
     *
     * @param fromPosition Position where the item moved from
     * @param toPosition   position where the item moved to
     * @return if there was movement from position to another
     */
    boolean onItemMove(int fromPosition, int toPosition);

    /**
     * Called when item is dismissed
     *
     * @param position the position of the dismissed item
     */
    void onItemDismiss(int position);
}

