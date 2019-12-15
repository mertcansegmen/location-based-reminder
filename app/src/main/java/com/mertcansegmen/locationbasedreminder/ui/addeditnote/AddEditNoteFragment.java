package com.mertcansegmen.locationbasedreminder.ui.addeditnote;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.mertcansegmen.locationbasedreminder.R;
import com.mertcansegmen.locationbasedreminder.model.Note;
import com.mertcansegmen.locationbasedreminder.ui.MainActivity;

public class AddEditNoteFragment extends Fragment {

    public static final String NOTE_BUNDLE_KEY ="com.mertcansegmen.locationbasedreminder.EXTRA_NOTE";

    private EditText noteEditText;

    private Note currentNote;

    private AddEditNoteFragmentViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_edit_note, container, false);
        setHasOptionsMenu(true);

        noteEditText = view.findViewById(R.id.txt_note);

        viewModel = ViewModelProviders.of(this).get(AddEditNoteFragmentViewModel.class);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(getArguments() != null) {
            currentNote = getArguments().getParcelable(NOTE_BUNDLE_KEY);
            if (!isNewNote(currentNote)) {
                ((MainActivity) requireActivity()).getSupportActionBar().setTitle("Edit Note");
                noteEditText.setText(currentNote.getBody());
            }
        }
    }

    private void saveNote() {
        String text = noteEditText.getText().toString().trim();
        if(text.isEmpty()) {
            if(isNewNote(currentNote)) {
                Toast.makeText(getContext(), "Empty note deleted", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        if(isNewNote(currentNote)) {
            Note newNote = new Note(text);
            viewModel.insert(newNote);
        } else {
            currentNote.setBody(text);
            viewModel.update(currentNote);
        }
    }

    private boolean isNewNote(Note note) {
        return note == null;
    }

    private void closeKeyboard() {
        View view = requireActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        if(isNewNote(currentNote)) {
            inflater.inflate(R.menu.add_note_menu, menu);
        } else {
            inflater.inflate(R.menu.edit_note_menu, menu);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_note:
                saveNote();
                closeKeyboard();
                requireActivity().onBackPressed();
                return true;
            case R.id.delete_note:
                viewModel.delete(currentNote);
                closeKeyboard();
                requireActivity().onBackPressed();
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
