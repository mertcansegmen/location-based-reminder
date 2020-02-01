package com.mertcansegmen.locationbasedreminder.util;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Custom data observer for the adapter. This is needed for recycler view to go top when a
 * new item is added. Without this, new item will be inserted off of the screen.
 */

public class AdapterDataObserver extends RecyclerView.AdapterDataObserver {

    private RecyclerView recyclerView;

    public AdapterDataObserver(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }

    @Override
    public void onItemRangeInserted(int positionStart, int itemCount) {
        // Scroll only if inserted items position is 0, so it will only scroll to top when
        // a new item is inserted, not when item was deleted and re-inserted.
        if (positionStart == 0) recyclerView.scrollToPosition(0);
    }

}
