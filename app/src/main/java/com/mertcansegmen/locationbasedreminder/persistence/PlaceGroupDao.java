package com.mertcansegmen.locationbasedreminder.persistence;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.mertcansegmen.locationbasedreminder.model.PlaceGroup;
import com.mertcansegmen.locationbasedreminder.model.PlaceGroupPlaceCrossRef;
import com.mertcansegmen.locationbasedreminder.model.PlaceGroupWithPlaces;

import java.util.List;

@Dao
public interface PlaceGroupDao {

    @Query("INSERT INTO PlaceGroup (name) VALUES(:name)")
    void insert(String name);

    @Insert
    void insert(PlaceGroupPlaceCrossRef placeGroupPlaceCrossRef);

    @Update
    void update(PlaceGroup placeGroup);

    @Delete
    void delete(PlaceGroup placeGroup);

    @Query("DELETE FROM PlaceGroup")
    void deleteAllPlaceGroups();

    @Query("SELECT * FROM PlaceGroup")
    LiveData<List<PlaceGroup>> getAllPlaceGroups();

    @Transaction
    @Query("SELECT * FROM PlaceGroup")
    LiveData<List<PlaceGroupWithPlaces>> getAllPlaceGroupsWithPlaces();
}
