package com.mertcansegmen.locationbasedreminder.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;

import com.mertcansegmen.locationbasedreminder.model.Note;
import com.mertcansegmen.locationbasedreminder.repository.NoteRepository;

public class AddEditNoteFragmentViewModel extends BaseViewModel<Note> {

    public AddEditNoteFragmentViewModel(@NonNull Application application) {
        super(application);
        repository = new NoteRepository(application);
        allItems = repository.getAll();
    }
}
