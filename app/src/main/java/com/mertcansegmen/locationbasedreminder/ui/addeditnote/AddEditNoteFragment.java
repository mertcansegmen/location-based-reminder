package com.mertcansegmen.locationbasedreminder.ui.addeditnote;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.mertcansegmen.locationbasedreminder.R;
import com.mertcansegmen.locationbasedreminder.model.Note;
import com.mertcansegmen.locationbasedreminder.ui.MainActivity;
import com.mertcansegmen.locationbasedreminder.util.Utils;

public class AddEditNoteFragment extends Fragment {

    public static final String BUNDLE_KEY_NOTE = "com.mertcansegmen.locationbasedreminder.BUNDLE_KEY_NOTE";

    private EditText titleEditText;
    private EditText noteEditText;

    private Note currentNote;

    private AddEditNoteFragmentViewModel viewModel;

    private NavController navController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_edit_note, container, false);
        setHasOptionsMenu(true);

        titleEditText = view.findViewById(R.id.txt_title);
        noteEditText = view.findViewById(R.id.txt_note);

        viewModel = ViewModelProviders.of(this).get(AddEditNoteFragmentViewModel.class);

        retrieveNote();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);
    }

    private void retrieveNote() {
        if(getArguments() != null && getArguments().getParcelable(BUNDLE_KEY_NOTE) != null) {
            currentNote = getArguments().getParcelable(BUNDLE_KEY_NOTE);
            ((MainActivity) requireActivity()).getSupportActionBar().setTitle(getString(R.string.edit_note));
            if(currentNote.getTitle() != null) titleEditText.setText(currentNote.getTitle());
            if(currentNote.getBody() != null) noteEditText.setText(currentNote.getBody());
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        if(inEditMode()) {
            inflater.inflate(R.menu.edit_note_menu, menu);
        } else {
            inflater.inflate(R.menu.add_note_menu, menu);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_note:
                saveNote();
                return true;
            case R.id.delete_note:
                askToDeleteCurrentNote();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void saveNote() {
        String noteTitle = titleEditText.getText().toString().trim();
        String noteBody = noteEditText.getText().toString().trim();

        if(noteBody.isEmpty() && noteTitle.isEmpty()) return;

        if(inEditMode()) updateCurrentNote(noteTitle, noteBody);
        else insertNewNote(noteTitle, noteBody);

        Utils.closeKeyboard(requireActivity());
        navController.popBackStack();
    }

    private void updateCurrentNote(String noteTitle, String noteBody) {
        if(!noteTitle.isEmpty()) currentNote.setTitle(noteTitle);
        else currentNote.setTitle(null);

        if(!noteBody.isEmpty()) currentNote.setBody(noteBody);
        else currentNote.setBody(null);

        viewModel.update(currentNote);
    }

    private void insertNewNote(String noteTitle, String noteBody) {
        Note newNote = new Note();

        if(!noteTitle.isEmpty()) newNote.setTitle(noteTitle);
        if(!noteBody.isEmpty()) newNote.setBody(noteBody);

        viewModel.insert(newNote);
    }

    private void askToDeleteCurrentNote() {
        new MaterialAlertDialogBuilder(requireContext())
                .setMessage(getString(R.string.msg_delete_note))
                .setPositiveButton(getText(R.string.ok), (dialog, which) -> {
                    deleteCurrentNote();
                    Utils.closeKeyboard(requireActivity());
                    navController.popBackStack();
                })
                .setNegativeButton(getString(R.string.cancel), null)
                .show();
    }

    private void deleteCurrentNote() {
        viewModel.delete(currentNote);
    }

    private boolean inEditMode() {
        return currentNote != null;
    }
}