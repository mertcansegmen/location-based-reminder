package com.mertcansegmen.locationbasedreminder.ui.places;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.mertcansegmen.locationbasedreminder.R;
import com.mertcansegmen.locationbasedreminder.model.Place;
import com.mertcansegmen.locationbasedreminder.ui.ListingFragment;
import com.mertcansegmen.locationbasedreminder.ui.addeditplace.AddEditPlaceFragment;
import com.mertcansegmen.locationbasedreminder.util.Animator;

public class PlacesFragment extends ListingFragment {

    private PlaceAdapter adapter;
    private FloatingActionButton addPlaceButton;

    private PlacesFragmentViewModel viewModel;

    @Override
    protected View inflateFragment(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_places, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        addPlaceButton = view.findViewById(R.id.btn_add_place);
        Animator.animateFloatingActionButton(addPlaceButton);

        initViewModel();
        initObserver();
        setAddButtonClickListener();
        setAdapterItemClickListener();
    }

    private void initViewModel() {
        viewModel = ViewModelProviders.of(this).get(PlacesFragmentViewModel.class);
    }

    private void initObserver() {
        viewModel.getAllPlaces().observe(this, places -> {
            emptyMessageLayout.setVisibility(places.isEmpty() ? View.VISIBLE : View.GONE);

            adapter.submitList(places);
        });
    }

    private void setAddButtonClickListener() {
        addPlaceButton.setOnClickListener(v ->
                navController.navigate(R.id.action_placesFragment_to_addEditPlaceFragment)
        );
    }

    private void setAdapterItemClickListener() {
        adapter.setOnItemClickListener(place -> navigateForEdit(place));
    }

    private void navigateForEdit(Place place) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(AddEditPlaceFragment.BUNDLE_KEY_PLACE, place);
        navController.navigate(R.id.action_placesFragment_to_addEditPlaceFragment, bundle);
    }

    @Override
    protected void initAdapter() {
        adapter = new PlaceAdapter();
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        return adapter;
    }

    @Override
    protected void onAdapterItemSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        int deletedPosition = viewHolder.getAdapterPosition();
        Place deletedPlace = adapter.getPlaceAt(deletedPosition);

        viewModel.delete(adapter.getPlaceAt(viewHolder.getAdapterPosition()));

        Snackbar.make(viewHolder.itemView, getString(R.string.place_deleted), Snackbar.LENGTH_LONG)
                .setAction(getString(R.string.undo), v -> viewModel.insert(deletedPlace))
                .setAnchorView(addPlaceButton)
                .show();
    }

    @Override
    protected void onDeleteAllOptionSelected() {
        new MaterialAlertDialogBuilder(requireContext())
                .setMessage(getString(R.string.msg_delete_all_places))
                .setPositiveButton(getText(R.string.ok), (dialog, which) -> viewModel.deleteAll())
                .setNegativeButton(getString(R.string.cancel), null)
                .show();
    }
}
