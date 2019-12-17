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

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.mertcansegmen.locationbasedreminder.R;
import com.mertcansegmen.locationbasedreminder.model.Place;
import com.mertcansegmen.locationbasedreminder.model.PlaceGroup;
import com.mertcansegmen.locationbasedreminder.model.PlaceGroupWithPlaces;
import com.mertcansegmen.locationbasedreminder.ui.MainActivity;
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_edit_place_group, container, false);
        setHasOptionsMenu(true);

        placeGroupNameEditTextLayout = view.findViewById(R.id.txt_place_group_name_layout);
        placeGroupNameEditText = view.findViewById(R.id.txt_place_group_name);
        chipGroup = view.findViewById(R.id.chip_group);
        scrollView = view.findViewById(R.id.scroll_view);

        viewModel = ViewModelProviders.of(this).get(AddEditPlaceGroupFragmentViewModel.class);

        // Scroll to bottom on create because add place chip is at the bottom.
        scrollView.post(() -> scrollView.fullScroll(View.FOCUS_DOWN));

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(getArguments() != null) {
            currentPlaceGroup = getArguments().getParcelable(PLACE_GROUP_BUNDLE_KEY);
            if (!isNewPlaceGroup(currentPlaceGroup)) {
                ((MainActivity) requireActivity()).getSupportActionBar().setTitle("Edit Place Group");
                placeGroupNameEditText.setText(currentPlaceGroup.getPlaceGroup().getName());
                loadPlaceChips();
            }
        }
        createAddPlaceChip();
    }

    private void loadPlaceChips() {
        for(Place place : currentPlaceGroup.getPlaces()) {
            addChip(place);
        }
    }

    private void addChip(Place place) {
        PlaceChip chip = new PlaceChip(requireContext());
        chip.setPlace(place);
        chip.setText(place.getName());
        chip.setCloseIconVisible(true);
        chip.setCloseIconTint(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.colorBlack)));
        chip.setChipIcon(requireContext().getResources().getDrawable(R.drawable.ic_places));
        chipGroup.addView(chip, chipGroup.getChildCount() - 1);
        chip.setOnCloseIconClickListener(removeChip -> chipGroup.removeView(removeChip));
    }

    private void createAddPlaceChip() {
        Chip addPlaceChip = new Chip(requireContext());
        addPlaceChip.setText(R.string.add_place);
        addPlaceChip.setTextStartPadding(3);
        addPlaceChip.setChipIcon(requireContext().getResources().getDrawable(R.drawable.ic_add));
        addPlaceChip.setTextColor(getResources().getColor(R.color.colorBlack));
        addPlaceChip.setChipIconTintResource(R.color.colorBlack);
        addPlaceChip.setChipBackgroundColor(ColorStateList.valueOf(ContextCompat
                .getColor(requireContext(), R.color.colorChipDefault)));
        chipGroup.addView(addPlaceChip);
        addPlaceChip.setOnClickListener(v -> {
            // TODO: Navigate dialog to select new place
        });
    }

    /**
     * @return all places from the chips that are currently on the screen except for the chip
     *         which is for adding new place
     */
    private List<Place> getPlacesFromChips() {
        ArrayList<Place> places = new ArrayList<>();
        for(int i = 0; i < chipGroup.getChildCount() - 1; i++) {
            places.add(((PlaceChip) chipGroup.getChildAt(i)).getPlace());
        }
        return places;
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
                viewModel.delete(currentPlaceGroup);
                requireActivity().onBackPressed();
                Utils.closeKeyboard(requireActivity());
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private boolean isNewPlaceGroup(PlaceGroupWithPlaces placeGroup) {
        return placeGroup == null;
    }
}
