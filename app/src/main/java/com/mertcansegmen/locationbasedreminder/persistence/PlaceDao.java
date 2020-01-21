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
public interface PlaceDao extends BaseDao<Place> {

    @Insert
    long insert(Place place);

    @Update
    void update(Place place);

    @Delete
    void delete(Place place);

    @Query("DELETE FROM Place")
    void deleteAll();

    @Query("SELECT * FROM Place ORDER BY placeId DESC")
    LiveData<List<Place>> getAll();
}
