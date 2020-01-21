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
import com.mertcansegmen.locationbasedreminder.ui.places.PlaceAdapter;
import com.mertcansegmen.locationbasedreminder.viewmodel.AddEditReminderFragmentViewModel;

public class PickPlaceDialog extends ListingDialog {

    private ImageButton createNewPlaceButton;
    private PlaceAdapter adapter;

    private AddEditReminderFragmentViewModel viewModel;

    @Override
    protected View inflateFragment(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_pick_place, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        createNewPlaceButton = view.findViewById(R.id.btn_create_new_place);

        initViewModel();
        initObserver();
        setCreateNewPlaceButtonClickListener();
        setAdapterItemClickListener();
    }

    private void initViewModel() {
        viewModel = ViewModelProviders.of(requireActivity()).get(AddEditReminderFragmentViewModel.class);
    }


    private void initObserver() {
        viewModel.getAllPlaces().observe(this, places -> {
            emptyMessageLayout.setVisibility(places.isEmpty() ? View.VISIBLE : View.GONE);

            adapter.submitList(places);
        });
    }

    private void setCreateNewPlaceButtonClickListener() {
        createNewPlaceButton.setOnClickListener(v ->
                navController.navigate(R.id.action_pickPlaceDialog_to_addEditPlaceFragment)
        );
    }

    private void setAdapterItemClickListener() {
        adapter.setOnItemClickListener(place -> {
            viewModel.select(place);
            dismiss();
        });
    }

    @Override
    protected void initAdapter() {
        adapter = new PlaceAdapter();
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        return adapter;
    }
}
