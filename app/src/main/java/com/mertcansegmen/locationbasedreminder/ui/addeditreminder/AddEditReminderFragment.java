package com.mertcansegmen.locationbasedreminder.ui.addeditreminder;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.mertcansegmen.locationbasedreminder.R;
import com.mertcansegmen.locationbasedreminder.model.Note;
import com.mertcansegmen.locationbasedreminder.model.Place;
import com.mertcansegmen.locationbasedreminder.model.PlaceGroup;
import com.mertcansegmen.locationbasedreminder.model.PlaceGroupWithPlaces;
import com.mertcansegmen.locationbasedreminder.model.ReminderWithNotePlacePlaceGroup;
import com.mertcansegmen.locationbasedreminder.ui.MainActivity;
import com.mertcansegmen.locationbasedreminder.util.Animator;
import com.mertcansegmen.locationbasedreminder.ui.views.OutlineChip;
import com.mertcansegmen.locationbasedreminder.util.Utils;

import java.util.Arrays;

public class AddEditReminderFragment extends Fragment {

    public static final String BUNDLE_KEY_REMINDER = "com.mertcansegmen.locationbasedreminder.BUNDLE_KEY_REMINDER";

    private TextInputEditText titleEditText;
    private TextInputEditText noteEditText;
    private TextView noPlaceTextView;
    private ChipGroup chipGroup;
    private MaterialButton selectPlaceButton;
    private MaterialButton selectPlaceGroupButton;

    private ReminderWithNotePlacePlaceGroup currentReminder;

    private AddEditReminderFragmentViewModel viewModel;

    private NavController navController;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_edit_reminder, container, false);
        setHasOptionsMenu(true);

        titleEditText = view.findViewById(R.id.txt_title);
        noteEditText = view.findViewById(R.id.txt_note);
        noPlaceTextView = view.findViewById(R.id.txt_no_place);
        chipGroup = view.findViewById(R.id.chip_group);
        selectPlaceButton = view.findViewById(R.id.btn_select_place);
        selectPlaceGroupButton = view.findViewById(R.id.btn_select_place_group);

        viewModel = ViewModelProviders.of(this).get(AddEditReminderFragmentViewModel.class);

        setObserver();
        setAddPlaceButtonClickListener();
        setAddPlaceGroupButtonClickListener();
        retrieveReminder();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);
    }

    @Override
    public void onPause() {
        super.onPause();
        // In order for view model to be shared, it has to be scoped to the activity. That causes
        // the last selected place to stay in memory as long as the activity is alive. That's why
        // last selected place must be cleared when AddEditPlaceGroupFragment is closed.
        viewModel.select(null);
    }

    private void setObserver() {
        viewModel.getSelected().observe(this, placeOrPlaceGroup -> {
            if(placeOrPlaceGroup == null) {
                noPlaceTextView.setVisibility(View.VISIBLE);
                return;
            }

            addChip(placeOrPlaceGroup);
            noPlaceTextView.setVisibility(View.GONE);
        });
    }

    private void retrieveReminder() {
        if(getArguments() != null && getArguments().getParcelable(BUNDLE_KEY_REMINDER) != null) {
            currentReminder = getArguments().getParcelable(BUNDLE_KEY_REMINDER);
            ((MainActivity) requireActivity()).getSupportActionBar().setTitle(R.string.edit_reminder);
            titleEditText.setText(currentReminder.getNote().getTitle());
            noteEditText.setText(currentReminder.getNote().getBody());
            if(currentReminder.getPlace() != null) {
                viewModel.select(currentReminder.getPlace());
            } else {
                viewModel.select(currentReminder.getPlaceGroupWithPlaces());
            }
        }
    }

    private void addChip(Selectable placeOrPlaceGroup) {
        if(placeOrPlaceGroup == null) return;

        chipGroup.removeAllViews();

        OutlineChip chip = new OutlineChip(requireContext());
        if(placeOrPlaceGroup instanceof Place) {
            chip.setText(((Place)placeOrPlaceGroup).getName());
            chip.setChipIcon(requireContext().getResources().getDrawable(R.drawable.ic_places));
        } else if(placeOrPlaceGroup instanceof PlaceGroupWithPlaces) {
            chip.setText(((PlaceGroupWithPlaces)placeOrPlaceGroup).getPlaceGroup().getName());
            chip.setChipIcon(requireContext().getResources().getDrawable(R.drawable.ic_place_groups_small));
        }
        chip.setCloseIconVisible(true);

        chip.setOnCloseIconClickListener(v -> {
            viewModel.select(null);
            chipGroup.removeView(v);
            Animator.removeViewWithFadeAnimation(v);
        });

        chipGroup.addView(chip);
        Animator.addViewWithFadeAnimation(chip);
    }

    private void setAddPlaceButtonClickListener() {
        selectPlaceButton.setOnClickListener(v -> {
            // TODO: navigate to select place/place group dialog
            // Test data
//            Place place = new Place("Shell", 41.297474, -72.926468, 600);
//            place.setPlaceId(9L);
//            viewModel.select(place);
        });
    }

    private void setAddPlaceGroupButtonClickListener() {
        selectPlaceGroupButton.setOnClickListener(v -> {
            // TODO: navigate to select place/place group dialog
            // Test data
//            PlaceGroupWithPlaces placeGroupWithPlaces = new PlaceGroupWithPlaces();
//            PlaceGroup placeGroup = new PlaceGroup("Gas Stations");
//            placeGroup.setPlaceGroupId(3);
//            placeGroupWithPlaces.setPlaceGroup(placeGroup);
//            Place place1 = new Place("Food Lion", 42.421935, -71.065640, 100);
//            place1.setPlaceId(1L);
//            Place place2 = new Place("Falletti Foods", 42.360037, -71.087794, 300);
//            place1.setPlaceId(2L);
//            placeGroupWithPlaces.setPlaces(Arrays.asList(place1, place2));
//            viewModel.select(placeGroupWithPlaces);
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        if(inEditMode()) {
            inflater.inflate(R.menu.edit_reminder_menu, menu);
        } else {
            inflater.inflate(R.menu.add_reminder_menu, menu);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_reminder:
                saveReminder();
                return true;
            case R.id.delete_reminder:
                askToDeleteReminder();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void saveReminder() {
        String noteTitle = titleEditText.getText().toString().trim();
        String noteBody = noteEditText.getText().toString().trim();
        Selectable placeOrPlaceGroup = viewModel.getSelected().getValue();

        if(placeOrPlaceGroup == null) {
            Toast.makeText(requireContext(), R.string.provide_a_place, Toast.LENGTH_LONG).show();
            return;
        }

        if(inEditMode()) updateCurrentReminder(noteTitle, noteBody, placeOrPlaceGroup);
        else insertNewReminder(noteTitle, noteBody, placeOrPlaceGroup);

        Utils.closeKeyboard(requireActivity());
        navController.popBackStack();
    }

    private void insertNewReminder(String title, String body, Selectable placeOrPlaceGroup) {
        ReminderWithNotePlacePlaceGroup reminder = new ReminderWithNotePlacePlaceGroup();
        reminder.setNote(new Note(title, body));
        reminder.setPlace(placeOrPlaceGroup instanceof Place ?
                (Place) placeOrPlaceGroup : null);
        reminder.setPlaceGroupWithPlaces(placeOrPlaceGroup instanceof PlaceGroupWithPlaces ?
                (PlaceGroupWithPlaces) placeOrPlaceGroup : null);
        viewModel.insert(reminder);
    }

    private void updateCurrentReminder(String title, String body, Selectable placeOrPlaceGroup) {
        currentReminder.getNote().setTitle(title);
        currentReminder.getNote().setBody(body);
        currentReminder.setPlace(placeOrPlaceGroup instanceof Place ?
                (Place) placeOrPlaceGroup : null);
        currentReminder.setPlaceGroupWithPlaces(placeOrPlaceGroup instanceof PlaceGroupWithPlaces ?
                (PlaceGroupWithPlaces) placeOrPlaceGroup : null);
        viewModel.update(currentReminder);
    }

    private void askToDeleteReminder() {
        new MaterialAlertDialogBuilder(requireContext())
                .setMessage(getString(R.string.msg_delete_reminder))
                .setPositiveButton(getText(R.string.ok), (dialog, which) -> {
                    deleteReminder();
                    Utils.closeKeyboard(requireActivity());
                    navController.popBackStack();
                })
                .setNegativeButton(getString(R.string.cancel), null)
                .show();
    }

    private void deleteReminder() {
        viewModel.delete(currentReminder);
    }

    private boolean inEditMode() {
        return currentReminder != null;
    }
}
