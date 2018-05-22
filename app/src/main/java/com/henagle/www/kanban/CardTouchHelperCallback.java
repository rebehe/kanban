package com.henagle.www.kanban;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

/**
 * Created by rebeccahe on 5/21/18.
 *
 * Interface that listens for move and swipe events.
 *
 * Reference: https://medium.com/@ipaulpro/drag-and-swipe-with-recyclerview-b9456d2b1aaf
 */

public class CardTouchHelperCallback extends ItemTouchHelper.Callback {

    private final CardTouchHelperAdapter adapter;

    public CardTouchHelperCallback(CardTouchHelperAdapter adapter) {
        this.adapter = adapter;
    }

    /*
    Specifies which direction of drags and swipes are supported
    */
    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN; // Enables dragging in both directions
        int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END; // Enables swiping in both directions
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    /*
    Notifies anything in charge of updating data
     */
    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        adapter.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    /*
    Notifies anything in charge of updating data
     */
    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        adapter.onItemDismiss(viewHolder.getAdapterPosition());
    }

    // --- Helpers ---

    /*
    Support starting drag events from a long press of a RecyclerView item
     */
    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    /*
    Enable swiping from touch events that start anywhere within the view
     */
    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }


    /*
    Allows us to pass event callbacks back up the chain
     */
    public interface CardTouchHelperAdapter {

        boolean onItemMove(int fromPosition, int toPosition);

        void onItemDismiss(int position);
    }
}
