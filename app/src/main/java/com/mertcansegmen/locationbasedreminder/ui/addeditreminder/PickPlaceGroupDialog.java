package com.mertcansegmen.locationbasedreminder.ui.addeditreminder;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mertcansegmen.locationbasedreminder.R;
import com.mertcansegmen.locationbasedreminder.ui.placegroups.PlaceGroupWithPlacesAdapter;

public class PickPlaceGroupDialog extends DialogFragment {

    private ImageButton createNewPlaceGroupButton;
    private RecyclerView recyclerView;
    private PlaceGroupWithPlacesAdapter adapter;

    private AddEditReminderFragmentViewModel viewModel;

    private NavController navController;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_pick_place_group, container, false);

        recyclerView = view.findViewById(R.id.recycler_view);
        createNewPlaceGroupButton = view.findViewById(R.id.btn_create_new_place_group);

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
        viewModel.getAllPlaceGroups().observe(this, placeGroups -> {
            adapter.submitList(placeGroups);
        });
    }

    private void configureRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        adapter = new PlaceGroupWithPlacesAdapter();
        recyclerView.setAdapter(adapter);
        if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            recyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        }
    }

    private void setOnAdapterItemClickListener() {
        adapter.setOnItemClickListener(placeGroup -> {
            viewModel.select(placeGroup);
            dismiss();
        });
    }

    private void setCreateNewPlaceButtonClickListener() {
        createNewPlaceGroupButton.setOnClickListener(v ->
                navController.navigate(R.id.action_pickPlaceGroupDialog_to_addEditPlaceGroupFragment)
        );
    }
}
