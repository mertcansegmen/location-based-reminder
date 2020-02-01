package com.mertcansegmen.locationbasedreminder.repository;

import android.app.Application;

import com.mertcansegmen.locationbasedreminder.model.Note;
import com.mertcansegmen.locationbasedreminder.persistence.AppDatabase;

public class NoteRepository extends BaseRepository<Note>{

    public NoteRepository(Application application) {
        super();
        AppDatabase database = AppDatabase.getInstance(application);
        dao = database.noteDao();
        allItems = dao.getAll();
    }
}
