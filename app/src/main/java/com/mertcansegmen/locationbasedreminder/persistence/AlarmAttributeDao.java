package com.mertcansegmen.locationbasedreminder.persistence;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.mertcansegmen.locationbasedreminder.model.AlarmAttribute;

import java.util.List;

@Dao
public interface AlarmAttributeDao {

    @Insert
    void insert(AlarmAttribute alarmAttribute);

    @Update
    void update(AlarmAttribute alarmAttribute);

    @Delete
    void delete(AlarmAttribute alarmAttribute);

    @Query("DELETE FROM alarm_attribute")
    void deleteAllAlarmAttributes();

    @Query("SELECT * FROM alarm_attribute")
    LiveData<List<AlarmAttribute>> getAllAlarmAttributes();
}
