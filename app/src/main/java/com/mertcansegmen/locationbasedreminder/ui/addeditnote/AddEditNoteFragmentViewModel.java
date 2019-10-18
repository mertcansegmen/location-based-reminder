package com.mertcansegmen.locationbasedreminder.ui.addeditnote;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.mertcansegmen.locationbasedreminder.model.Note;
import com.mertcansegmen.locationbasedreminder.repository.NoteRepository;

public class AddEditNoteFragmentViewModel extends AndroidViewModel {

    private NoteRepository repository;

    public AddEditNoteFragmentViewModel(@NonNull Application application) {
        super(application);
        repository = new NoteRepository(application);
    }

    public void insert(Note note) {
        repository.insert(note);
    }

    public void update(Note note) {
        repository.update(note);
    }

    public void delete(Note note) {
        repository.delete(note);
    }
}
