package com.mertcansegmen.locationbasedreminder.ui.addeditplacegroup;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.mertcansegmen.locationbasedreminder.ui.views.OutlineChip;
import com.mertcansegmen.locationbasedreminder.ui.views.ColorfulChip;
import com.mertcansegmen.locationbasedreminder.util.ConfigUtils;

import java.util.ArrayList;
import java.util.List;

public class AddEditPlaceGroupFragment extends Fragment {

    public static final String BUNDLE_KEY_PLACE_GROUP = "com.mertcansegmen.locationbasedreminder.BUNDLE_KEY_PLACE_GROUP";

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

        setObserver();
        createAddPlaceChip();
        retrievePlaceGroup();
        scrollToBottom();
        setTextChangedListener();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // In order for view model to be shared, it has to be scoped to the activity. That causes
        // the last selected place or place group to stay in memory as long as the activity is
        // alive. That's why last selected place must be cleared when AddEditPlaceGroupFragment is
        // closed.
        viewModel.selectPlace(null);
    }

    private void setObserver() {
        viewModel.getSelectedPlace().observe(this, selectedPlace -> {
            if(selectedPlace == null) return;
            addChip(selectedPlace);
        });
    }

    /**
     * Scroll to bottom on create in case of chips doesn't fit screen, because add place chip is at
     * the bottom.
     */
    private void scrollToBottom() {
        scrollView.post(() -> scrollView.fullScroll(View.FOCUS_DOWN));
    }

    /**
     * If text input has error enabled, clear error when user starts typing.
     */
    private void setTextChangedListener() {
        placeGroupNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                placeGroupNameEditTextLayout.setErrorEnabled(false);
            }
        });
    }

    private void retrievePlaceGroup() {
        if(getArguments() != null && getArguments().getParcelable(BUNDLE_KEY_PLACE_GROUP) != null) {
            currentPlaceGroup = getArguments().getParcelable(BUNDLE_KEY_PLACE_GROUP);
            ((MainActivity) requireActivity()).getSupportActionBar().setTitle(currentPlaceGroup.getPlaceGroup().getName());
            placeGroupNameEditText.setText(currentPlaceGroup.getPlaceGroup().getName());
            loadPlaceChips(currentPlaceGroup.getPlaces());
        }
    }

    private void loadPlaceChips(List<Place> places) {
        for(Place place : places) {
            addChip(place);
        }
    }

    private void addChip(Place place) {
        ColorfulChip chip = new ColorfulChip(requireContext());
        chip.setPlace(place);
        chip.setText(place.getName());
        chip.setCloseIconVisible(true);
        chip.setCloseIconTint(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.colorBlack)));
        chip.setChipIcon(requireContext().getResources().getDrawable(R.drawable.ic_places));

        chipGroup.addView(chip, chipGroup.getChildCount() - 1);
        Animator.addViewWithFadeAnimation(chip);

        chip.setOnCloseIconClickListener(v -> removeChip(chip));
    }

    private void removeChip(ColorfulChip chip) {
        chipGroup.removeView(chip);
        Animator.removeViewWithFadeAnimation(chip);
    }

    private void createAddPlaceChip() {
        OutlineChip chip = new OutlineChip(requireContext());
        chip.setChipIcon(requireContext().getResources().getDrawable(R.drawable.ic_add));
        chip.setText(R.string.add_place);

        chipGroup.addView(chip);

        chip.setOnClickListener(v -> navigateToPickPlaceDialog());
    }

    private void navigateToPickPlaceDialog() {
        Bundle bundle = new Bundle();
        ArrayList<Place> selectedPlaces = new ArrayList<>(getPlacesFromChips());
        bundle.putParcelableArrayList(PickPlacesDialog.BUNDLE_KEY_SELECTED_PLACES, selectedPlaces);
        navController.navigate(R.id.action_addEditPlaceGroupFragment_to_pickPlacesDialog, bundle);
    }

    /**
     * @return all places from the chips that are currently on the screen except for the chip
     *         which is for adding new place
     */
    private List<Place> getPlacesFromChips() {
        ArrayList<Place> places = new ArrayList<>();
        for(int i = 0; i < chipGroup.getChildCount(); i++) {
            if(chipGroup.getChildAt(i) instanceof ColorfulChip)
                places.add(((ColorfulChip) chipGroup.getChildAt(i)).getPlace());
        }
        return places;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        if(inEditMode()) {
            inflater.inflate(R.menu.edit_menu, menu);
        } else {
            inflater.inflate(R.menu.add_menu, menu);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save:
                savePlaceGroup();
                return true;
            case R.id.delete:
                askToDeletePlaceGroup();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void savePlaceGroup() {
        String placeGroupName = placeGroupNameEditText.getText().toString().trim();
        List<Place> places = getPlacesFromChips();

        if(placeGroupName.isEmpty()) {
            placeGroupNameEditTextLayout.setError(getString(R.string.error_empty_place_group_name));
            return;
        }

        if(inEditMode()) updateCurrentPlaceGroup(placeGroupName, places);
        else insertNewPlaceGroup(placeGroupName, places);

        ConfigUtils.closeKeyboard(requireActivity());
        navController.popBackStack();
    }

    private void insertNewPlaceGroup(String placeGroupName, List<Place> places) {
        PlaceGroupWithPlaces placeGroup = new PlaceGroupWithPlaces();
        placeGroup.setPlaceGroup(new PlaceGroup(placeGroupName));
        placeGroup.setPlaces(places);
        viewModel.insert(placeGroup);
    }

    private void updateCurrentPlaceGroup(String placeGroupName, List<Place> places) {
        currentPlaceGroup.getPlaceGroup().setName(placeGroupName);
        currentPlaceGroup.setPlaces(places);
        viewModel.update(currentPlaceGroup);
    }

    private void askToDeletePlaceGroup() {
        new MaterialAlertDialogBuilder(requireContext())
                .setMessage(getString(R.string.msg_delete_place_group))
                .setPositiveButton(getText(R.string.ok), (dialog, which) -> {
                    deletePlaceGroup();
                    ConfigUtils.closeKeyboard(requireActivity());
                    navController.popBackStack();
                })
                .setNegativeButton(getString(R.string.cancel), null)
                .show();
    }

    private void deletePlaceGroup() {
        viewModel.delete(currentPlaceGroup);
    }

    private boolean inEditMode() {
        return currentPlaceGroup != null;
    }
}