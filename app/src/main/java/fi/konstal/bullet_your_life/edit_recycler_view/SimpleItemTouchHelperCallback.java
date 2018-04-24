package fi.konstal.bullet_your_life.edit_recycler_view;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

/**
 * A helper class which helps with Drag 'n Drop functionality
 *
 * @author Konsta Lehtinen
 * @version 1.0
 * @see fi.konstal.bullet_your_life.fragment.NotesFragment
 * @since 1.0
 */
public class SimpleItemTouchHelperCallback extends ItemTouchHelper.Callback {

    private final ItemTouchHelperAdapter mAdapter;

    /**
     * Default constructor
     *
     * @param adapter the adapter which will use these callbacks
     */
    public SimpleItemTouchHelperCallback(ItemTouchHelperAdapter adapter) {
        mAdapter = adapter;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN |
                ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT;

        int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                          RecyclerView.ViewHolder target) {
        mAdapter.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        mAdapter.onItemDismiss(viewHolder.getAdapterPosition());
    }
}