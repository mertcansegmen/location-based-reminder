package com.mertcansegmen.locationbasedreminder.ui.addeditreminder;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.mertcansegmen.locationbasedreminder.R;
import com.mertcansegmen.locationbasedreminder.ui.ListingDialog;
import com.mertcansegmen.locationbasedreminder.ui.placegroups.PlaceGroupWithPlacesAdapter;
import com.mertcansegmen.locationbasedreminder.viewmodel.AddEditReminderFragmentViewModel;

public class PickPlaceGroupDialog extends ListingDialog {

    private ImageButton createNewPlaceGroupButton;
    private PlaceGroupWithPlacesAdapter adapter;

    private AddEditReminderFragmentViewModel viewModel;

    @Override
    protected View inflateFragment(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_pick_place_group, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        createNewPlaceGroupButton = view.findViewById(R.id.btn_create_new_place_group);

        initViewModel();
        initObserver();
        setCreateNewPlaceButtonClickListener();
        setAdapterItemClickListener();
    }

    private void initViewModel() {
        viewModel = ViewModelProviders.of(requireActivity()).get(AddEditReminderFragmentViewModel.class);
    }

    private void initObserver() {
        viewModel.getAllPlaceGroups().observe(this, placeGroups -> {
            emptyMessageLayout.setVisibility(placeGroups.isEmpty() ? View.VISIBLE : View.GONE);

            adapter.submitList(placeGroups);
        });
    }

    private void setCreateNewPlaceButtonClickListener() {
        createNewPlaceGroupButton.setOnClickListener(v ->
                navController.navigate(R.id.action_pickPlaceGroupDialog_to_addEditPlaceGroupFragment)
        );
    }

    private void setAdapterItemClickListener() {
        adapter.setOnItemClickListener(placeGroup -> {
            viewModel.select(placeGroup);
            dismiss();
        });
    }

    @Override
    protected void initAdapter() {
        adapter = new PlaceGroupWithPlacesAdapter();
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        return adapter;
    }
}
