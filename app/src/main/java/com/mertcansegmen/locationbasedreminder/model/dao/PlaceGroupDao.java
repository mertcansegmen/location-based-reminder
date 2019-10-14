package com.mertcansegmen.locationbasedreminder.model.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.mertcansegmen.locationbasedreminder.model.entity.PlaceGroup;

import java.util.List;

@Dao
public interface PlaceGroupDao {

    @Insert
    void insert(PlaceGroup placeGroup);

    @Update
    void update(PlaceGroup placeGroup);

    @Delete
    void delete(PlaceGroup placeGroup);

    @Query("DELETE FROM place_group")
    void deleteAllPlaceGroups();

    @Query("SELECT * FROM place_group")
    LiveData<List<PlaceGroup>> getAllPlaceGroups();
}
