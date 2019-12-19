package com.mertcansegmen.locationbasedreminder.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.mertcansegmen.locationbasedreminder.persistence.AppDatabase;
import com.mertcansegmen.locationbasedreminder.persistence.NoteDao;
import com.mertcansegmen.locationbasedreminder.model.Note;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class NoteRepository {

    private NoteDao noteDao;
    private LiveData<List<Note>> allNotes;

    public NoteRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        noteDao = database.noteDao();
        allNotes = noteDao.getAllNotes();
    }

    public void insert(Note note) {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> noteDao.insert(note.getBody()));
    }

    public void update(Note note) {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> noteDao.update(note));
    }

    public void delete(Note note) {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> noteDao.delete(note));
    }

    public void deleteAll() {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> noteDao.deleteAllNotes());
    }

    public LiveData<List<Note>> getAllNotes() {
        return allNotes;
    }
}
