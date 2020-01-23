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
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.mertcansegmen.locationbasedreminder.R;
import com.mertcansegmen.locationbasedreminder.util.AdapterDataObserver;
import com.mertcansegmen.locationbasedreminder.util.ConfigUtils;
import com.mertcansegmen.locationbasedreminder.util.SpacingItemDecoration;

public abstract class ListingFragment extends BaseFragment {

    protected LinearLayout emptyMessageLayout;
    private RecyclerView recyclerView;
    protected ExtendedFloatingActionButton fab;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        emptyMessageLayout = view.findViewById(R.id.empty_msg_layout);
        recyclerView = view.findViewById(R.id.recycler_view);
        fab = view.findViewById(R.id.fab);

        configureRecyclerView();
    }

    private void configureRecyclerView() {
        initAdapter();
        // Set column count 2 if phone is in landscape mode, set it 1 if it's in portrait mode.
        int columnCount = ConfigUtils.inLandscapeMode(requireContext()) ? 2 : 1;
        int spacingInDp = 10;
        recyclerView.setLayoutManager(new GridLayoutManager(requireContext(), columnCount));
        recyclerView.addItemDecoration(new SpacingItemDecoration(columnCount, spacingInDp, getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(getAdapter());

        // Register the custom DataObserver
        getAdapter().registerAdapterDataObserver(new AdapterDataObserver(recyclerView));

        // Set ItemTouchHelper for swipe actions
        setItemTouchHelper();

        // Set ScrollListener
        setRecyclerViewScrollListener();
    }

    /**
     * Concrete subclasses must override this method and initialize their adapter in it because
     * adapter is needed for configuring recycler view.
     */
    protected abstract void initAdapter();

    /**
     * @return recycler view adapter.
     */
    protected abstract RecyclerView.Adapter getAdapter();

    /**
     * Adds a scroll listener to recycler view that makes floating action button shrink when
     * scrolled down, and extend when scrolled up.
     */
    private void setRecyclerViewScrollListener() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx,int dy){
                super.onScrolled(recyclerView, dx, dy);

                if (dy > 0) { // Scroll Down
                    if (fab.isExtended()) {
                        fab.shrink();
                    }
                } else if (dy < 0) { // Scroll Up
                    if (!fab.isExtended()) {
                        fab.extend();
                    }
                }
            }
        });
    }

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
     * @param viewHolder view holder of the swiped item
     * @param direction  swipe direction
     */
    protected abstract void onAdapterItemSwiped(RecyclerView.ViewHolder viewHolder, int direction);

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.listing_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_all:
                onDeleteAllOptionSelected();
                return true;
            case R.id.settings:
                navigateSettings();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Implement what happens when user selects the option "Delete All" in here.
     */
    protected abstract void onDeleteAllOptionSelected();

    /**
     * Concrete subclasses must navigate to settings fragment here.
     */
    protected abstract void navigateSettings();
}