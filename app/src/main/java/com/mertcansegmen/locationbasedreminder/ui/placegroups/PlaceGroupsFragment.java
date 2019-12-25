package com.mertcansegmen.locationbasedreminder.ui.placegroups;

import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.mertcansegmen.locationbasedreminder.R;
import com.mertcansegmen.locationbasedreminder.model.PlaceGroupWithPlaces;
import com.mertcansegmen.locationbasedreminder.ui.addeditplacegroup.AddEditPlaceGroupFragment;
import com.mertcansegmen.locationbasedreminder.util.Animator;

public class PlaceGroupsFragment extends Fragment {

    private LinearLayout emptyMessageLayout;
    private RecyclerView recyclerView;
    private PlaceGroupWithPlacesAdapter adapter;
    private FloatingActionButton addPlaceGroupButton;

    private int deletedPosition;
    private PlaceGroupWithPlaces deletedPlaceGroup;

    private PlaceGroupsFragmentViewModel viewModel;

    private NavController navController;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_place_groups, container, false);
        setHasOptionsMenu(true);

        addPlaceGroupButton = view.findViewById(R.id.btn_add_place_group);
        recyclerView = view.findViewById(R.id.recycler_view);
        emptyMessageLayout = view.findViewById(R.id.empty_msg_layout);

        viewModel = ViewModelProviders.of(this).get(PlaceGroupsFragmentViewModel.class);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        adapter = new PlaceGroupWithPlacesAdapter();
        recyclerView.setAdapter(adapter);
        if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            recyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        }

        Animator.animateFloatingActionButton(addPlaceGroupButton);

        viewModel.getAllPlaceGroupsWithPlaces().observe(this, placeGroupsWithPlaces -> {
            emptyMessageLayout.setVisibility(placeGroupsWithPlaces.isEmpty() ? View.VISIBLE : View.GONE);

            adapter.submitList(placeGroupsWithPlaces);
        });

        addPlaceGroupButton.setOnClickListener(v -> {
            navController.navigate(R.id.action_placeGroupsFragment_to_addEditPlaceGroupFragment);
        });

        adapter.setOnItemClickListener(placeGroupWithPlaces -> {
            Bundle bundle = new Bundle();
            bundle.putParcelable(AddEditPlaceGroupFragment.PLACE_GROUP_BUNDLE_KEY, placeGroupWithPlaces);
            navController.navigate(R.id.action_placeGroupsFragment_to_addEditPlaceGroupFragment, bundle);
        });

        // This is needed for recycler view to go top when new record is added. Without this, new
        // element is getting inserted off of the screen.
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                if(positionStart == 0) {
                    recyclerView.scrollToPosition(0);
                }
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                deletedPosition = viewHolder.getAdapterPosition();
                deletedPlaceGroup = adapter.getPlaceGroupWithPlacesAt(deletedPosition);

                viewModel.delete(adapter.getPlaceGroupWithPlacesAt(deletedPosition));

                Snackbar.make(viewHolder.itemView, "Place group deleted.", Snackbar.LENGTH_LONG)
                        .setAction("UNDO", v -> viewModel.insert(deletedPlaceGroup))
                        .setAnchorView(addPlaceGroupButton)
                        .show();
            }
        }).attachToRecyclerView(recyclerView);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.place_groups_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_all_place_groups:
                new MaterialAlertDialogBuilder(requireContext())
                        .setMessage(getString(R.string.question_delete_all_place_groups))
                        .setPositiveButton(getText(R.string.ok), (dialog, which) -> viewModel.deleteAll())
                        .setNegativeButton(getString(R.string.cancel), null)
                        .show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
