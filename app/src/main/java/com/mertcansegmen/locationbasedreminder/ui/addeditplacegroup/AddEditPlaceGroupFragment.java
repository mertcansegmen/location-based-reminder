package com.mertcansegmen.locationbasedreminder.ui.addeditplacegroup;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.mertcansegmen.locationbasedreminder.R;
import com.mertcansegmen.locationbasedreminder.model.Place;
import com.mertcansegmen.locationbasedreminder.model.PlaceGroup;
import com.mertcansegmen.locationbasedreminder.model.PlaceGroupWithPlaces;
import com.mertcansegmen.locationbasedreminder.ui.MainActivity;
import com.mertcansegmen.locationbasedreminder.util.Animator;
import com.mertcansegmen.locationbasedreminder.util.PlaceChip;
import com.mertcansegmen.locationbasedreminder.util.Utils;

import java.util.ArrayList;
import java.util.List;

public class AddEditPlaceGroupFragment extends Fragment {

    public static final String PLACE_GROUP_BUNDLE_KEY = "com.mertcansegmen.locationbasedreminder.PLACE_GROUP_BUNDLE_KEY";

    private TextInputLayout placeGroupNameEditTextLayout;
    private TextInputEditText placeGroupNameEditText;
    private ChipGroup chipGroup;
    private ScrollView scrollView;

    private PlaceGroupWithPlaces currentPlaceGroup;

    private AddEditPlaceGroupFragmentViewModel viewModel;

    private NavController navController;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_edit_place_group, container, false);
        setHasOptionsMenu(true);

        placeGroupNameEditTextLayout = view.findViewById(R.id.txt_place_group_name_layout);
        placeGroupNameEditText = view.findViewById(R.id.txt_place_group_name);
        chipGroup = view.findViewById(R.id.chip_group);
        scrollView = view.findViewById(R.id.scroll_view);

        viewModel = ViewModelProviders.of(requireActivity()).get(AddEditPlaceGroupFragmentViewModel.class);

        viewModel.getSelectedPlace().observe(this, selectedPlace -> addChip(selectedPlace));

        // Scroll to bottom on create because add place chip is at the bottom.
        scrollView.post(() -> scrollView.fullScroll(View.FOCUS_DOWN));

        if(getArguments() != null) {
            currentPlaceGroup = getArguments().getParcelable(PLACE_GROUP_BUNDLE_KEY);
        }

        createAddPlaceChip();

        if(!isNewPlaceGroup(currentPlaceGroup)) {
            ((MainActivity) requireActivity()).getSupportActionBar().setTitle(currentPlaceGroup.getPlaceGroup().getName());

            placeGroupNameEditText.setText(currentPlaceGroup.getPlaceGroup().getName());
            loadPlaceChips(currentPlaceGroup.getPlaces());
        }

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);
    }

    private void loadPlaceChips(List<Place> places) {
        for(Place place : places) {
            addChip(place);
        }
    }

    private void addChip(Place place) {
        PlaceChip chipToAdd = new PlaceChip(requireContext());
        chipToAdd.setPlace(place);
        chipToAdd.setText(place.getName());
        chipToAdd.setCloseIconVisible(true);
        chipToAdd.setCloseIconTint(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.colorBlack)));
        chipToAdd.setChipIcon(requireContext().getResources().getDrawable(R.drawable.ic_places));

        chipGroup.addView(chipToAdd, chipGroup.getChildCount() - 1);
        Animator.addViewWithFadeAnimation(chipToAdd);

        chipToAdd.setOnCloseIconClickListener(chipToRemove -> {
            chipGroup.removeView(chipToRemove);
            Animator.removeViewWithFadeAnimation(chipToRemove);
        });
    }

    private void createAddPlaceChip() {
        Chip addPlaceChip = new Chip(requireContext());
        addPlaceChip.setText(R.string.add_place);
        addPlaceChip.setTextStartPadding(3);
        addPlaceChip.setChipStrokeWidth(2);
        addPlaceChip.setChipIcon(requireContext().getResources().getDrawable(R.drawable.ic_add));

        if(Utils.isDarkModeEnabled(requireContext())) {
            createLightChip(addPlaceChip);
        } else {
            createDarkChip(addPlaceChip);
        }
        chipGroup.addView(addPlaceChip);
        addPlaceChip.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            ArrayList<Place> selectedPlaces = new ArrayList<>(getPlacesFromChips());
            bundle.putParcelableArrayList(PickPlaceDialog.SELECTED_PLACES_BUNDLE_KEY, selectedPlaces);
            navController.navigate(R.id.action_addEditPlaceGroupFragment_to_pickPlaceDialog, bundle);
        });
    }

    private void createLightChip(Chip chip) {
        styleChip(chip, R.color.colorBlack, R.color.colorWhite);
    }

    private void createDarkChip(Chip chip) {
        styleChip(chip, R.color.colorWhite, R.color.colorBlack);
    }

    private void styleChip(Chip chip, int backgroundColor, int borderColor) {
        chip.setChipIconTintResource(borderColor);
        chip.setChipStrokeColor(ColorStateList.valueOf(ContextCompat
                .getColor(requireContext(), borderColor)));
        chip.setChipBackgroundColor(ColorStateList.valueOf(ContextCompat
                .getColor(requireContext(), backgroundColor)));
    }

    /**
     * @return all places from the chips that are currently on the screen except for the chip
     *         which is for adding new place
     */
    private List<Place> getPlacesFromChips() {
        ArrayList<Place> places = new ArrayList<>();
        for(int i = 0; i < chipGroup.getChildCount(); i++) {
            if(chipGroup.getChildAt(i) instanceof PlaceChip)
                places.add(((PlaceChip) chipGroup.getChildAt(i)).getPlace());
        }
        return places;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        if(isNewPlaceGroup(currentPlaceGroup)) {
            inflater.inflate(R.menu.add_place_group_menu, menu);
        } else {
            inflater.inflate(R.menu.edit_place_group_menu, menu);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_place_group:
                savePlaceGroup();
                Utils.closeKeyboard(requireActivity());
                return true;
            case R.id.delete_place_group:
                deletePlaceGroup();
                Utils.closeKeyboard(requireActivity());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void savePlaceGroup() {
        String placeGroupName = placeGroupNameEditText.getText().toString().trim();
        List<Place> placesToSave = getPlacesFromChips();

        // TODO: Add validation

        if(isNewPlaceGroup(currentPlaceGroup)) {
            // Add new place group
            PlaceGroupWithPlaces placeGroup = new PlaceGroupWithPlaces();
            placeGroup.setPlaceGroup(new PlaceGroup(placeGroupName));
            placeGroup.setPlaces(placesToSave);
            viewModel.insert(placeGroup);
        } else {
            // Update current place group
            currentPlaceGroup.getPlaceGroup().setName(placeGroupName);
            currentPlaceGroup.setPlaces(placesToSave);
            viewModel.update(currentPlaceGroup);
        }

        requireActivity().onBackPressed();
    }

    private void deletePlaceGroup() {
        new MaterialAlertDialogBuilder(requireContext())
                .setMessage(getString(R.string.delete_place_group_message))
                .setPositiveButton(getText(R.string.ok), (dialog, which) -> {
                    viewModel.delete(currentPlaceGroup);
                    requireActivity().onBackPressed();
                })
                .setNegativeButton(getString(R.string.cancel), null)
                .show();
    }

    private boolean isNewPlaceGroup(PlaceGroupWithPlaces placeGroup) {
        return placeGroup == null;
    }
}