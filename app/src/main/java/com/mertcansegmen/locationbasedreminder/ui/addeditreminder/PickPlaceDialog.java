package com.mertcansegmen.locationbasedreminder.ui.addeditreminder;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.mertcansegmen.locationbasedreminder.R;
import com.mertcansegmen.locationbasedreminder.ui.places.PlaceAdapter;

public class PickPlaceDialog extends DialogFragment {

    private MaterialButton createNewPlaceButton;
    private RecyclerView recyclerView;
    private PlaceAdapter adapter;

    private AddEditReminderFragmentViewModel viewModel;

    private NavController navController;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_pick_place, container, false);

        recyclerView = view.findViewById(R.id.recycler_view);
        createNewPlaceButton = view.findViewById(R.id.btn_create_new_place);

        viewModel = ViewModelProviders.of(requireActivity()).get(AddEditReminderFragmentViewModel.class);

        setObserver();
        configureRecyclerView();
        setOnAdapterItemClickListener();
        setCreateNewPlaceButtonClickListener();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = NavHostFragment.findNavController(this);
    }

    private void setObserver() {
        viewModel.getAllPlaces().observe(this, places -> {
            adapter.submitList(places);
        });
    }

    private void configureRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        adapter = new PlaceAdapter();
        recyclerView.setAdapter(adapter);
        if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            recyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        }
    }

    private void setOnAdapterItemClickListener() {
        adapter.setOnItemClickListener(place -> {
            viewModel.select(place);
            dismiss();
        });
    }

    private void setCreateNewPlaceButtonClickListener() {
        createNewPlaceButton.setOnClickListener(v ->
                navController.navigate(R.id.action_pickPlaceDialog_to_addEditPlaceFragment)
        );
    }
}
