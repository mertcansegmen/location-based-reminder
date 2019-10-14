package com.mertcansegmen.locationbasedreminder.model.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.mertcansegmen.locationbasedreminder.model.entity.Reminder;

import java.util.List;

@Dao
public interface ReminderDao {

    @Insert
    void insert(Reminder reminder);

    @Update
    void update(Reminder reminder);

    @Delete
    void delete(Reminder reminder);

    @Query("DELETE FROM reminder")
    void deleteAllReminders();

    @Query("SELECT * FROM reminder")
    LiveData<List<Reminder>> getAllReminders();

}
