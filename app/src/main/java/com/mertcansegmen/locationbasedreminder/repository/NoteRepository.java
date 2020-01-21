package com.mertcansegmen.locationbasedreminder.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.mertcansegmen.locationbasedreminder.persistence.AppDatabase;
import com.mertcansegmen.locationbasedreminder.persistence.NoteDao;
import com.mertcansegmen.locationbasedreminder.model.Note;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class NoteRepository extends BaseRepository<Note>{

    public NoteRepository(Application application) {
        super();
        AppDatabase database = AppDatabase.getInstance(application);
        dao = database.noteDao();
        allItems = dao.getAll();
    }
}
