package com.mertcansegmen.locationbasedreminder.persistence;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.mertcansegmen.locationbasedreminder.model.Place;

import java.util.List;

@Dao
public interface PlaceDao {

    @Query("INSERT INTO Place (name,latitude,longitude,radius) VALUES(:name,:latitude,:longitude,:radius)")
    void insert(String name, double latitude, double longitude, int radius);

    @Update
    void update(Place place);

    @Delete
    void delete(Place place);

    @Query("DELETE FROM place")
    void deleteAllPlaces();

    @Query("SELECT * FROM Place")
    LiveData<List<Place>> getAllPlaces();
}
