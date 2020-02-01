package com.mertcansegmen.locationbasedreminder.ui;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.mertcansegmen.locationbasedreminder.R;
import com.mertcansegmen.locationbasedreminder.util.AdapterDataObserver;
import com.mertcansegmen.locationbasedreminder.util.DevicePrefs;

public abstract class ListingFragment extends BaseFragment {

    private static final String PREF_KEY_LISTING_LAYOUT = "com.mertcansegmen.locationbasedreminder.PREF_KEY_LISTING_LAYOUT";
    private static final int GRID_LAYOUT = 0;
    private static final int LINEAR_LAYOUT = 1;

    protected SwipeRefreshLayout swipeRefreshLayout;
    protected LinearLayout emptyMessageLayout;
    private RecyclerView recyclerView;
    protected ExtendedFloatingActionButton fab;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        swipeRefreshLayout = view.findViewById(R.id.swipe_layout);
        emptyMessageLayout = view.findViewById(R.id.empty_msg_layout);
        recyclerView = view.findViewById(R.id.recycler_view);
        fab = view.findViewById(R.id.fab);

        initViewModel();
        initListObserver();
        configureRecyclerView();
        setSwipeRefreshListener();
    }

    protected abstract void initViewModel();

    private void configureRecyclerView() {
        initAdapter();

        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(getAdapter());

        getAdapter().registerAdapterDataObserver(new AdapterDataObserver(recyclerView));
        setRecyclerViewLayoutManager();
        setItemTouchHelper();
        setRecyclerViewScrollListener();
    }

    /**
     * Sets recycler view's layout manager depending on shared preferences.
     */
    private void setRecyclerViewLayoutManager() {
        if (getLayoutPref() == GRID_LAYOUT) {
            recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        } else if (getLayoutPref() == LINEAR_LAYOUT) {
            recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        }
    }

    /**
     * Concrete subclasses must override this method and initialize their adapter in it because
     * adapter is needed for configuring recycler view.
     */
    protected abstract void initAdapter();

    protected abstract <T extends RecyclerView.Adapter> T getAdapter();

    protected abstract void initListObserver();

    /**
     * Adds a scroll listener to recycler view that makes floating action button shrink when
     * scrolled down, and extend when scrolled up.
     */
    private void setRecyclerViewScrollListener() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
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

    private void setSwipeRefreshListener() {
        swipeRefreshLayout.setOnRefreshListener(() -> initListObserver());
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.listing_menu, menu);

        swapLayoutMenuIcon(menu.getItem(1));

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
            case R.id.layout:
                swapLayoutPref();
                swapLayoutMenuIcon(item);
                setRecyclerViewLayoutManager();
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

    /**
     * Swaps the shared preference value of layout manager type.
     */
    private void swapLayoutPref() {
        if (getLayoutPref() == GRID_LAYOUT) {
            setLayoutPref(LINEAR_LAYOUT);
        } else if (getLayoutPref() == LINEAR_LAYOUT) {
            setLayoutPref(GRID_LAYOUT);
        }
    }

    /**
     * Swaps the menu icon of layout manager type.
     */
    private void swapLayoutMenuIcon(MenuItem menuItem) {
        if (getLayoutPref() == GRID_LAYOUT) {
            menuItem.setIcon(R.drawable.ic_linear_layout);
        } else if (getLayoutPref() == LINEAR_LAYOUT) {
            menuItem.setIcon(R.drawable.ic_grid_layout);
        }
    }

    private int getLayoutPref() {
        return DevicePrefs.getPrefs(requireContext(), PREF_KEY_LISTING_LAYOUT, LINEAR_LAYOUT);
    }

    private void setLayoutPref(int layout) {
        DevicePrefs.setPrefs(requireContext(), PREF_KEY_LISTING_LAYOUT, layout);
    }
}