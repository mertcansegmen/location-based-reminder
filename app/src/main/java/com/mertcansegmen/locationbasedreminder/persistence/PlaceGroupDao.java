package com.mertcansegmen.locationbasedreminder.persistence;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.mertcansegmen.locationbasedreminder.model.PlaceGroup;

import java.util.List;

@Dao
public interface PlaceGroupDao {

    @Insert
    void insert(PlaceGroup placeGroup);

    @Update
    void update(PlaceGroup placeGroup);

    @Delete
    void delete(PlaceGroup placeGroup);

    @Query("DELETE FROM PlaceGroup")
    void deleteAllPlaceGroups();

    @Query("SELECT * FROM PlaceGroup")
    LiveData<List<PlaceGroup>> getAllPlaceGroups();
}
