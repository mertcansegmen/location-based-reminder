package com.mertcansegmen.locationbasedreminder.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.mertcansegmen.locationbasedreminder.model.Place;
import com.mertcansegmen.locationbasedreminder.model.PlaceGroup;
import com.mertcansegmen.locationbasedreminder.model.PlaceGroupPlaceCrossRef;
import com.mertcansegmen.locationbasedreminder.model.ReminderWithNotePlacePlaceGroup;
import com.mertcansegmen.locationbasedreminder.persistence.AppDatabase;
import com.mertcansegmen.locationbasedreminder.persistence.NoteDao;
import com.mertcansegmen.locationbasedreminder.persistence.PlaceDao;
import com.mertcansegmen.locationbasedreminder.persistence.PlaceGroupDao;
import com.mertcansegmen.locationbasedreminder.persistence.ReminderDao;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ReminderRepository {

    private ReminderDao reminderDao;
    private NoteDao noteDao;
    private PlaceDao placeDao;
    private PlaceGroupDao placeGroupDao;
    private LiveData<List<ReminderWithNotePlacePlaceGroup>> allRemindersWithNotePlacePlaceGroup;

    public ReminderRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        noteDao = database.noteDao();
        placeDao = database.placeDao();
        placeGroupDao = database.placeGroupDao();
        reminderDao = database.reminderDao();
        allRemindersWithNotePlacePlaceGroup = reminderDao.getAllRemindersWithNotePlacePlaceGroup();
    }

    public void insert(ReminderWithNotePlacePlaceGroup reminderWithNotePlacePlaceGroup) {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            noteDao.insert(reminderWithNotePlacePlaceGroup.getNote());
            if(reminderWithNotePlacePlaceGroup.getPlace() != null) {
                placeDao.insert(reminderWithNotePlacePlaceGroup.getPlace());
            } else {
                placeGroupDao.insert(reminderWithNotePlacePlaceGroup.getPlaceGroup());
            }
            reminderDao.insert(reminderWithNotePlacePlaceGroup.getReminder());
        });
    }

    public void update(ReminderWithNotePlacePlaceGroup reminderWithNotePlacePlaceGroup) {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            noteDao.update(reminderWithNotePlacePlaceGroup.getNote());
            if(reminderWithNotePlacePlaceGroup.getPlace() != null) {
                placeDao.update(reminderWithNotePlacePlaceGroup.getPlace());
            } else {
                placeGroupDao.update(reminderWithNotePlacePlaceGroup.getPlaceGroup());
            }
            reminderDao.update(reminderWithNotePlacePlaceGroup.getReminder());
        });
    }

    public void delete(ReminderWithNotePlacePlaceGroup reminderWithNotePlacePlaceGroup) {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            noteDao.delete(reminderWithNotePlacePlaceGroup.getNote());
            if(reminderWithNotePlacePlaceGroup.getPlace() != null) {
                placeDao.delete(reminderWithNotePlacePlaceGroup.getPlace());
            } else {
                placeGroupDao.delete(reminderWithNotePlacePlaceGroup.getPlaceGroup());
            }
            reminderDao.delete(reminderWithNotePlacePlaceGroup.getReminder());
        });
    }

    public LiveData<List<ReminderWithNotePlacePlaceGroup>> getAllRemindersWithNotePlacePlaceGroup() {
        return allRemindersWithNotePlacePlaceGroup;
    }
}
