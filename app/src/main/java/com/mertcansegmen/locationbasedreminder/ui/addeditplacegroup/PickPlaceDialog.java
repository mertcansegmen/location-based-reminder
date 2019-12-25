package com.mertcansegmen.locationbasedreminder.ui.addeditplacegroup;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.ChipGroup;
import com.mertcansegmen.locationbasedreminder.R;
import com.mertcansegmen.locationbasedreminder.model.Place;
import com.mertcansegmen.locationbasedreminder.util.Animator;
import com.mertcansegmen.locationbasedreminder.util.PlaceChip;

import java.util.ArrayList;
import java.util.List;

public class PickPlaceDialog extends DialogFragment {

    public static final String SELECTED_PLACES_BUNDLE_KEY = "com.mertcansegmen.locationbasedreminder.SELECTED_PLACES_BUNDLE_KEY";

    private ChipGroup chipGroup;
    private MaterialButton okButton;
    private MaterialButton createNewPlaceButton;

    private AddEditPlaceGroupFragmentViewModel viewModel;

    private NavController navController;

    private List<Place> selectedPlaces;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_pick_places, container, false);

        chipGroup = view.findViewById(R.id.chip_group);
        okButton = view.findViewById(R.id.btn_ok);
        createNewPlaceButton = view.findViewById(R.id.btn_create_new_place);

        if(getArguments() != null) {
            selectedPlaces = getArguments().getParcelableArrayList(SELECTED_PLACES_BUNDLE_KEY);
        }

        viewModel = ViewModelProviders.of(requireActivity()).get(AddEditPlaceGroupFragmentViewModel.class);

        viewModel.getAllPlaces().observe(this, allPlaces -> {
            List<Place> availablePlaces = new ArrayList<>(allPlaces);
            for (Place place : selectedPlaces)
                availablePlaces.remove(place);
            loadPlaceChips(availablePlaces);
        });

        okButton.setOnClickListener(v -> dismiss());

        createNewPlaceButton.setOnClickListener(v -> navController.navigate(R.id.action_pickPlaceDialog_to_addEditPlaceFragment));

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = NavHostFragment.findNavController(this);
    }

    private void loadPlaceChips(List<Place> places) {
        chipGroup.removeAllViews();

        if(places.isEmpty()) {
            showNoPlaceMessage();
        } else {
            for(Place newPlace : places) {
                addChip(newPlace);
            }
        }
    }

    private void addChip(Place place) {
        PlaceChip chip = new PlaceChip(requireContext());
        chip.setPlace(place);
        chip.setText(place.getName());
        chip.setChipIcon(requireContext().getResources().getDrawable(R.drawable.ic_places));
        chipGroup.addView(chip);
        Animator.addViewWithFadeAnimation(chip);

        chip.setOnClickListener(v -> removeChip(place, chip));
    }

    private void removeChip(Place place, PlaceChip chip) {
        viewModel.selectPlace(place);
        chipGroup.removeView(chip);
        Animator.removeViewWithFadeAnimation(chip);
        if(chipGroup.getChildCount() == 0) {
            showNoPlaceMessage();
        }
    }

    private void showNoPlaceMessage() {
        TextView noPlaceTextView = new TextView(requireContext());
        noPlaceTextView.setText(getResources().getString(R.string.msg_no_available_place));
        chipGroup.addView(noPlaceTextView);
    }
}
