package com.mertcansegmen.locationbasedreminder.ui.addeditnote;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

import com.mertcansegmen.locationbasedreminder.R;
import com.mertcansegmen.locationbasedreminder.model.Note;
import com.mertcansegmen.locationbasedreminder.ui.AddEditFragment;
import com.mertcansegmen.locationbasedreminder.ui.MainActivity;
import com.mertcansegmen.locationbasedreminder.ui.addeditreminder.AddEditReminderFragment;
import com.mertcansegmen.locationbasedreminder.util.ConfigUtils;
import com.mertcansegmen.locationbasedreminder.viewmodel.AddEditNoteFragmentViewModel;

public class AddEditNoteFragment extends AddEditFragment {

    public static final String BUNDLE_KEY_NOTE = "com.mertcansegmen.locationbasedreminder.BUNDLE_KEY_NOTE";

    private EditText titleEditText;
    private EditText noteEditText;

    private Note currentNote;

    private AddEditNoteFragmentViewModel viewModel;

    @Override
    protected View inflateFragment(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_edit_note, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        initViewModel();
        retrieveNote();
    }

    private void initViews(View view) {
        titleEditText = view.findViewById(R.id.txt_title);
        noteEditText = view.findViewById(R.id.txt_note);
    }

    private void initViewModel() {
        viewModel = ViewModelProviders.of(this).get(AddEditNoteFragmentViewModel.class);
    }

    private void retrieveNote() {
        if (getArguments() != null && getArguments().getParcelable(BUNDLE_KEY_NOTE) != null) {
            currentNote = getArguments().getParcelable(BUNDLE_KEY_NOTE);
            ((MainActivity) requireActivity()).getSupportActionBar().setTitle(getString(R.string.edit_note));
            titleEditText.setText(currentNote.getTitle());
            noteEditText.setText(currentNote.getBody());
        }
    }

    @Override
    protected void saveMenuItemClicked() {
        saveNote();
    }

    @Override
    protected void addToReminderMenuItemClicked() {
        addToReminder();
    }

    @Override
    protected void deleteItem() {
        deleteCurrentNote();
    }

    private void saveNote() {
        String noteTitle = titleEditText.getText().toString().trim();
        String noteBody = noteEditText.getText().toString().trim();

        if (noteBody.isEmpty() && noteTitle.isEmpty()) return;

        if (inEditMode()) updateCurrentNote(noteTitle, noteBody);
        else insertNewNote(noteTitle, noteBody);

        ConfigUtils.closeKeyboard(requireActivity());
        navController.popBackStack();
    }

    private void updateCurrentNote(String noteTitle, String noteBody) {
        currentNote.setTitle(noteTitle);
        currentNote.setBody(noteBody);

        viewModel.update(currentNote);
    }

    private void insertNewNote(String noteTitle, String noteBody) {
        Note newNote = new Note(noteTitle, noteBody);

        viewModel.insert(newNote);
    }

    private void deleteCurrentNote() {
        viewModel.delete(currentNote);
    }

    private void addToReminder() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(AddEditReminderFragment.BUNDLE_KEY_NOTE_RETRIEVED, currentNote);
        navController.navigate(R.id.action_addEditNoteFragment_to_addEditReminderFragment, bundle);
    }

    @Override
    protected boolean inEditMode() {
        return currentNote != null;
    }
}