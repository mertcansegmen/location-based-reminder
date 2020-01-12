package com.mertcansegmen.locationbasedreminder.repository;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.mertcansegmen.locationbasedreminder.model.Reminder;
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

    private AppDatabase database;

    private ReminderDao reminderDao;
    private NoteDao noteDao;

    private LiveData<List<ReminderWithNotePlacePlaceGroup>> allRemindersWithNotePlacePlaceGroup;
    private LiveData<List<ReminderWithNotePlacePlaceGroup>> activeReminders;

    public ReminderRepository(Application application) {
        database = AppDatabase.getInstance(application);

        noteDao = database.noteDao();
        reminderDao = database.reminderDao();

        allRemindersWithNotePlacePlaceGroup = reminderDao.getAllRemindersWithNotePlacePlaceGroup();
        activeReminders = reminderDao.getActiveReminders();
    }

    public void insert(ReminderWithNotePlacePlaceGroup reminderWithNotePlacePlaceGroup) {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() ->
            database.runInTransaction(() -> {
                long noteId;
                Long placeId = null;
                Long placeGroupId = null;

                noteId = noteDao.insert(reminderWithNotePlacePlaceGroup.getNote());
                if(reminderWithNotePlacePlaceGroup.getPlace() != null) {
                    placeId = reminderWithNotePlacePlaceGroup.getPlace().getPlaceId();
                } else if(reminderWithNotePlacePlaceGroup.getPlaceGroupWithPlaces() != null) {
                    placeGroupId = reminderWithNotePlacePlaceGroup.getPlaceGroupWithPlaces().getPlaceGroup().getPlaceGroupId();
                }
                if(reminderWithNotePlacePlaceGroup.getReminder() == null) {
                    reminderWithNotePlacePlaceGroup.setReminder(new Reminder(noteId, placeId, placeGroupId, true));
                }
                reminderDao.insert(reminderWithNotePlacePlaceGroup.getReminder());
            })
        );
    }

    public void update(ReminderWithNotePlacePlaceGroup reminderWithNotePlacePlaceGroup) {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() ->
            database.runInTransaction(() -> {
                noteDao.update(reminderWithNotePlacePlaceGroup.getNote());
                if(reminderWithNotePlacePlaceGroup.getPlace() != null) {
                    reminderWithNotePlacePlaceGroup.getReminder().setPlaceId(
                            reminderWithNotePlacePlaceGroup.getPlace().getPlaceId());
                } else if(reminderWithNotePlacePlaceGroup.getPlaceGroupWithPlaces() != null){
                    reminderWithNotePlacePlaceGroup.getReminder().setPlaceGroupId(
                            reminderWithNotePlacePlaceGroup.getPlaceGroupWithPlaces().getPlaceGroup().getPlaceGroupId());
                }
                reminderDao.update(reminderWithNotePlacePlaceGroup.getReminder());
            })
        );
    }

    public void delete(ReminderWithNotePlacePlaceGroup reminderWithNotePlacePlaceGroup) {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() ->
            database.runInTransaction(() -> {
                noteDao.delete(reminderWithNotePlacePlaceGroup.getNote());
                reminderDao.delete(reminderWithNotePlacePlaceGroup.getReminder());
            })
        );
    }

    public void deleteAll() {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() ->
            database.runInTransaction(() -> {
                reminderDao.deleteAllReminderNotes();
                reminderDao.deleteAllReminders();
            })
        );
    }

    public void setActive(ReminderWithNotePlacePlaceGroup reminderWithNotePlacePlaceGroup, boolean active) {
        setActive(reminderWithNotePlacePlaceGroup.getReminder().getReminderId(), active);
    }

    public void setActive(long reminderId, boolean active) {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() ->
                reminderDao.setActive(reminderId, active)
        );
    }

    public LiveData<List<ReminderWithNotePlacePlaceGroup>> getActiveReminders() {
        return activeReminders;
    }

    public LiveData<List<ReminderWithNotePlacePlaceGroup>> getAllRemindersWithNotePlacePlaceGroup() {
        return allRemindersWithNotePlacePlaceGroup;
    }
}
