package com.mertcansegmen.locationbasedreminder.ui.addeditreminder;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;
import com.mertcansegmen.locationbasedreminder.R;
import com.mertcansegmen.locationbasedreminder.model.Note;
import com.mertcansegmen.locationbasedreminder.model.Place;
import com.mertcansegmen.locationbasedreminder.model.PlaceGroupWithPlaces;
import com.mertcansegmen.locationbasedreminder.model.ReminderWithNotePlacePlaceGroup;
import com.mertcansegmen.locationbasedreminder.service.ReminderService;
import com.mertcansegmen.locationbasedreminder.ui.AddEditFragment;
import com.mertcansegmen.locationbasedreminder.ui.MainActivity;
import com.mertcansegmen.locationbasedreminder.ui.views.OutlineChip;
import com.mertcansegmen.locationbasedreminder.util.Animator;
import com.mertcansegmen.locationbasedreminder.util.ConfigUtils;
import com.mertcansegmen.locationbasedreminder.viewmodel.AddEditReminderFragmentViewModel;

public class AddEditReminderFragment extends AddEditFragment {

    public static final String BUNDLE_KEY_REMINDER = "com.mertcansegmen.locationbasedreminder.BUNDLE_KEY_REMINDER";
    public static final String BUNDLE_KEY_NOTE_RETRIEVED = "com.mertcansegmen.locationbasedreminder.BUNDLE_KEY_NOTE_RETRIEVED";
    public static final String BUNDLE_KEY_PLACE_RETRIEVED = "com.mertcansegmen.locationbasedreminder.BUNDLE_KEY_PLACE_RETRIEVED";
    public static final String BUNDLE_KEY_PLACE_GROUP_RETRIEVED = "com.mertcansegmen.locationbasedreminder.BUNDLE_KEY_PLACE_GROUP_RETRIEVED";

    private TextInputEditText titleEditText;
    private TextInputEditText noteEditText;
    private TextView noPlaceTextView;
    private ChipGroup chipGroup;
    private MaterialButton selectPlaceButton;
    private MaterialButton selectPlaceGroupButton;

    private ReminderWithNotePlacePlaceGroup currentReminder;

    private AddEditReminderFragmentViewModel viewModel;

    @Override
    protected View inflateFragment(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_edit_reminder, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        initViewModel();
        setObserver();
        setAddPlaceButtonClickListener();
        setAddPlaceGroupButtonClickListener();
        retrieveReminder();
        retrieveReminderExtras();
    }

    private void initViews(View view) {
        titleEditText = view.findViewById(R.id.txt_title);
        noteEditText = view.findViewById(R.id.txt_note);
        noPlaceTextView = view.findViewById(R.id.txt_no_place);
        chipGroup = view.findViewById(R.id.chip_group);
        selectPlaceButton = view.findViewById(R.id.btn_select_place);
        selectPlaceGroupButton = view.findViewById(R.id.btn_select_place_group);
    }

    private void initViewModel() {
        viewModel = ViewModelProviders.of(requireActivity()).get(AddEditReminderFragmentViewModel.class);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // In order for view model to be shared, it has to be scoped to the activity. That causes
        // the last selected place to stay in memory as long as the activity is alive. That's why
        // last selected place must be cleared when AddEditPlaceGroupFragment is closed.
        viewModel.select(null);
    }

    private void setObserver() {
        viewModel.getSelected().observe(this, selected -> {
            if (selected == null) {
                noPlaceTextView.setVisibility(View.VISIBLE);
                return;
            }

            addChip(selected);
            noPlaceTextView.setVisibility(View.GONE);
        });
    }

    private void retrieveReminder() {
        if (getArguments() != null && getArguments().getParcelable(BUNDLE_KEY_REMINDER) != null) {
            currentReminder = getArguments().getParcelable(BUNDLE_KEY_REMINDER);
            ((MainActivity) requireActivity()).getSupportActionBar().setTitle(R.string.edit_reminder);
            titleEditText.setText(currentReminder.getNote().getTitle());
            noteEditText.setText(currentReminder.getNote().getBody());
            if (currentReminder.getPlace() != null) {
                viewModel.select(currentReminder.getPlace());
            } else {
                viewModel.select(currentReminder.getPlaceGroupWithPlaces());
            }
        }
    }

    private void retrieveReminderExtras() {
        if (getArguments() != null) {
            boolean noteRetrieved = getArguments().getParcelable(BUNDLE_KEY_NOTE_RETRIEVED) != null;
            boolean placeRetrieved = getArguments().getParcelable(BUNDLE_KEY_PLACE_RETRIEVED) != null;
            boolean placeGroupRetrieved = getArguments().getParcelable(BUNDLE_KEY_PLACE_GROUP_RETRIEVED) != null;

            if (noteRetrieved) {
                Note note = getArguments().getParcelable(BUNDLE_KEY_NOTE_RETRIEVED);
                titleEditText.setText(note.getTitle());
                noteEditText.setText(note.getBody());
            } else if (placeRetrieved) {
                Place place = getArguments().getParcelable(BUNDLE_KEY_PLACE_RETRIEVED);
                viewModel.select(place);
            } else if (placeGroupRetrieved) {
                PlaceGroupWithPlaces placeGroup = getArguments().getParcelable(BUNDLE_KEY_PLACE_GROUP_RETRIEVED);
                viewModel.select(placeGroup);
            }
        }
    }


    private void addChip(Selectable selectable) {
        if (selectable == null) return;

        chipGroup.removeAllViews();

        OutlineChip chip = new OutlineChip(requireContext());
        chip.setText(selectable.getDisplayText());
        chip.setChipIcon(requireContext().getResources().getDrawable(selectable.getDisplayIcon()));
        chip.setCloseIconVisible(true);

        chip.setOnCloseIconClickListener(v -> {
            viewModel.select(null);
            chipGroup.removeView(v);
            Animator.fadeOut(v);
        });

        chipGroup.addView(chip);
        Animator.fadeIn(chip);
    }

    private void setAddPlaceButtonClickListener() {
        selectPlaceButton.setOnClickListener(v -> {
            navController.navigate(R.id.action_addEditReminderFragment_to_pickPlaceDialog);

            selectPlaceButton.toggle(); // Prevent toggle
        });
    }

    private void setAddPlaceGroupButtonClickListener() {
        selectPlaceGroupButton.setOnClickListener(v -> {
            navController.navigate(R.id.action_addEditReminderFragment_to_pickPlaceGroupDialog);

            selectPlaceGroupButton.toggle(); // Prevent toggle
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        if (inEditMode()) {
            inflater.inflate(R.menu.edit_reminder_menu, menu);
        } else {
            inflater.inflate(R.menu.add_menu, menu);
        }
    }

    @Override
    protected void saveMenuItemClicked() {
        saveReminder();
    }

    @Override
    protected void deleteItem() {
        deleteReminder();
    }

    @Override
    protected void addToReminderMenuItemClicked() {
    } // ignore for reminders

    private void saveReminder() {
        String noteTitle = titleEditText.getText().toString().trim();
        String noteBody = noteEditText.getText().toString().trim();
        Selectable selectable = viewModel.getSelected().getValue();

        if (selectable == null) {
            Toast.makeText(requireContext(), R.string.provide_a_place, Toast.LENGTH_LONG).show();
            return;
        }

        if (inEditMode()) updateCurrentReminder(noteTitle, noteBody, selectable);
        else insertNewReminder(noteTitle, noteBody, selectable);

        startReminderService();

        ConfigUtils.closeKeyboard(requireActivity());
        navController.popBackStack();
    }

    private void startReminderService() {
        Intent serviceIntent = new Intent(requireContext(), ReminderService.class);
        ContextCompat.startForegroundService(requireContext(), serviceIntent);
    }

    private void insertNewReminder(String title, String body, Selectable selectable) {
        ReminderWithNotePlacePlaceGroup reminder = new ReminderWithNotePlacePlaceGroup();
        reminder.setNote(new Note(title, body));
        reminder.setPlace(selectable.getType() == Selectable.PLACE ? (Place) selectable : null);
        reminder.setPlaceGroupWithPlaces(selectable.getType() == Selectable.PLACE_GROUP ?
                (PlaceGroupWithPlaces) selectable : null);
        viewModel.insert(reminder);
    }

    private void updateCurrentReminder(String title, String body, Selectable selectable) {
        currentReminder.getNote().setTitle(title);
        currentReminder.getNote().setBody(body);
        currentReminder.setPlace(selectable.getType() == Selectable.PLACE ?
                (Place) selectable : null);
        currentReminder.setPlaceGroupWithPlaces(selectable.getType() == Selectable.PLACE_GROUP ?
                (PlaceGroupWithPlaces) selectable : null);
        viewModel.update(currentReminder);
    }

    private void deleteReminder() {
        viewModel.delete(currentReminder);
    }

    @Override
    protected boolean inEditMode() {
        return currentReminder != null;
    }
}
