package com.mertcansegmen.locationbasedreminder.ui.placegroups;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.mertcansegmen.locationbasedreminder.R;
import com.mertcansegmen.locationbasedreminder.model.PlaceGroupWithPlaces;
import com.mertcansegmen.locationbasedreminder.ui.ListingFragment;
import com.mertcansegmen.locationbasedreminder.ui.addeditplacegroup.AddEditPlaceGroupFragment;
import com.mertcansegmen.locationbasedreminder.util.Animator;

public class PlaceGroupsFragment extends ListingFragment {

    private PlaceGroupWithPlacesAdapter adapter;
    private FloatingActionButton addPlaceGroupButton;

    private PlaceGroupsFragmentViewModel viewModel;

    @Override
    protected View inflateFragment(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_place_groups, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        addPlaceGroupButton = view.findViewById(R.id.btn_add_place_group);
        Animator.animateFloatingActionButton(addPlaceGroupButton);

        initViewModel();
        initObserver();
        setAddButtonClickListener();
        setAdapterItemClickListener();
    }

    private void initViewModel() {
        viewModel = ViewModelProviders.of(this).get(PlaceGroupsFragmentViewModel.class);
    }

    private void initObserver() {
        viewModel.getAllPlaceGroupsWithPlaces().observe(this, placeGroupsWithPlaces -> {
            emptyMessageLayout.setVisibility(placeGroupsWithPlaces.isEmpty() ? View.VISIBLE : View.GONE);

            adapter.submitList(placeGroupsWithPlaces);
        });
    }

    private void setAddButtonClickListener() {
        addPlaceGroupButton.setOnClickListener(v ->
                navController.navigate(R.id.action_placeGroupsFragment_to_addEditPlaceGroupFragment)
        );
    }

    private void setAdapterItemClickListener() {
        adapter.setOnItemClickListener(placeGroupWithPlaces -> navigateForEdit(placeGroupWithPlaces));
    }

    private void navigateForEdit(PlaceGroupWithPlaces placeGroupWithPlaces) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(AddEditPlaceGroupFragment.BUNDLE_KEY_PLACE_GROUP, placeGroupWithPlaces);
        navController.navigate(R.id.action_placeGroupsFragment_to_addEditPlaceGroupFragment, bundle);
    }

    @Override
    protected void initAdapter() {
        adapter = new PlaceGroupWithPlacesAdapter();
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        return adapter;
    }

    @Override
    protected void onAdapterItemSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        int deletedPosition = viewHolder.getAdapterPosition();
        PlaceGroupWithPlaces deletedPlaceGroup = adapter.getPlaceGroupWithPlacesAt(deletedPosition);

        viewModel.delete(adapter.getPlaceGroupWithPlacesAt(deletedPosition));

        Snackbar.make(viewHolder.itemView, getString(R.string.place_group_deleted), Snackbar.LENGTH_LONG)
                .setAction(getString(R.string.undo), v -> viewModel.insert(deletedPlaceGroup))
                .setAnchorView(addPlaceGroupButton)
                .show();
    }

    @Override
    protected void onDeleteAllOptionSelected() {
        new MaterialAlertDialogBuilder(requireContext())
                .setMessage(getString(R.string.msg_delete_all_place_groups))
                .setPositiveButton(getText(R.string.ok), (dialog, which) -> viewModel.deleteAll())
                .setNegativeButton(getString(R.string.cancel), null)
                .show();
    }
}
