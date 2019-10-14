package com.mertcansegmen.locationbasedreminder.model.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.mertcansegmen.locationbasedreminder.model.entity.Place;

import java.util.List;

@Dao
public interface PlaceDao {

    @Insert
    void insert(Place place);

    @Update
    void update(Place place);

    @Delete
    void delete(Place place);

    @Query("DELETE FROM place")
    void deleteAllPlaces();

    @Query("SELECT * FROM place")
    LiveData<List<Place>> getAllPlaces();
}
