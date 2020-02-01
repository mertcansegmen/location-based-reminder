package com.mertcansegmen.locationbasedreminder.ui.addeditplacegroup;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.chip.ChipGroup;
import com.mertcansegmen.locationbasedreminder.R;
import com.mertcansegmen.locationbasedreminder.model.Place;
import com.mertcansegmen.locationbasedreminder.ui.views.ColorfulChip;
import com.mertcansegmen.locationbasedreminder.util.Animator;
import com.mertcansegmen.locationbasedreminder.viewmodel.AddEditPlaceGroupFragmentViewModel;

import java.util.ArrayList;
import java.util.List;

public class PickPlacesDialog extends DialogFragment {

    public static final String BUNDLE_KEY_SELECTED_PLACES = "com.mertcansegmen.locationbasedreminder.BUNDLE_KEY_SELECTED_PLACES";

    private ChipGroup chipGroup;
    private ImageButton createNewPlaceButton;

    private List<Place> selectedPlaces;

    private AddEditPlaceGroupFragmentViewModel viewModel;

    private NavController navController;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_pick_places, container, false);

        chipGroup = view.findViewById(R.id.chip_group);
        createNewPlaceButton = view.findViewById(R.id.btn_create_new_place);

        viewModel = ViewModelProviders.of(requireActivity()).get(AddEditPlaceGroupFragmentViewModel.class);

        setObserver();
        retrievePlaces();
        setCreateNewPlaceButtonClickListener();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = NavHostFragment.findNavController(this);
    }

    private void setObserver() {
        viewModel.getAllPlaces().observe(this, allPlaces -> {
            List<Place> availablePlaces = new ArrayList<>(allPlaces);
            for (Place place : selectedPlaces)
                availablePlaces.remove(place);
            loadPlaceChips(availablePlaces);
        });
    }

    private void retrievePlaces() {
        if (getArguments() != null) {
            selectedPlaces = getArguments().getParcelableArrayList(BUNDLE_KEY_SELECTED_PLACES);
        }
    }

    private void setCreateNewPlaceButtonClickListener() {
        createNewPlaceButton.setOnClickListener(v ->
                navController.navigate(R.id.action_pickPlacesDialog_to_addEditPlaceFragment)
        );
    }

    private void loadPlaceChips(List<Place> places) {
        chipGroup.removeAllViews();

        if (places.isEmpty()) {
            showNoPlaceMessage();
        } else {
            for (Place newPlace : places) {
                addChip(newPlace);
            }
        }
    }

    private void addChip(Place place) {
        ColorfulChip chip = new ColorfulChip(requireContext());
        chip.setPlace(place);
        chip.setText(place.getName());
        chip.setChipIcon(requireContext().getResources().getDrawable(R.drawable.ic_places));
        chipGroup.addView(chip);
        Animator.fadeIn(chip);

        chip.setOnClickListener(v -> {
            viewModel.selectPlace(place);
            removeChip(chip);
        });
    }

    private void removeChip(ColorfulChip chip) {
        chipGroup.removeView(chip);
        Animator.fadeOut(chip);
        if (chipGroup.getChildCount() == 0) {
            showNoPlaceMessage();
        }
    }

    private void showNoPlaceMessage() {
        TextView noPlaceTextView = new TextView(requireContext());
        noPlaceTextView.setText(getResources().getString(R.string.msg_no_available_place));
        chipGroup.addView(noPlaceTextView);
    }
}