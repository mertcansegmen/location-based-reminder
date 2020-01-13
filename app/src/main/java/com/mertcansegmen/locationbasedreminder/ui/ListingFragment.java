package com.mertcansegmen.locationbasedreminder.ui;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mertcansegmen.locationbasedreminder.R;
import com.mertcansegmen.locationbasedreminder.util.ConfigUtils;

public abstract class ListingFragment extends BaseFragment {

    protected LinearLayout emptyMessageLayout;
    private RecyclerView recyclerView;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        emptyMessageLayout = view.findViewById(R.id.empty_msg_layout);
        recyclerView = view.findViewById(R.id.recycler_view);

        configureRecyclerView();
        registerAdapterDataObserver();
        setItemTouchHelper();
    }

    private void configureRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        if(ConfigUtils.inLandscapeMode(requireContext())) {
            recyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        }
        initAdapter();
        recyclerView.setAdapter(getAdapter());
    }

    /**
     * Subclasses must override this method and initialize their adapter in it because adapter is
     * needed for configuring recycler view in this class.
     */
    protected abstract void initAdapter();

    /**
     * Registers data observer for the adapter. This is needed for recycler view to go top when a
     * new item is added. Without this, new item will be inserted off of the screen.
     */
    private void registerAdapterDataObserver() {
        getAdapter().registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                // Scroll only if inserted items position is 0, so it will only scroll to top when
                // a new item is inserted, not when item was deleted and re-inserted.
                if(positionStart == 0) recyclerView.scrollToPosition(0);
            }
        });
    }

    /**
     * @return recycler view adapter.
     */
    protected abstract RecyclerView.Adapter getAdapter();

    /**
     * Creates new ItemTouchHelper for swipe actions and attaches it to recycler view.
     */
    private void setItemTouchHelper() {
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                onAdapterItemSwiped(viewHolder, direction);
            }
        }).attachToRecyclerView(recyclerView);
    }

    /**
     * Implement what happens when user swipes an item from the recycler view in here.
     *
     * @param viewHolder view holder of the swiped item.
     * @param direction  swipe direction.
     */
    protected abstract void onAdapterItemSwiped(RecyclerView.ViewHolder viewHolder, int direction);

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.items_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_all:
                onDeleteAllOptionSelected();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Implement what happens when user selects option "Delete All" in here.
     */
    protected abstract void onDeleteAllOptionSelected();
}