package com.mertcansegmen.locationbasedreminder.persistence;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.mertcansegmen.locationbasedreminder.model.PlaceGroupItem;

import java.util.List;

@Dao
public interface PlaceGroupItemDao {

    @Insert
    void insert(PlaceGroupItem placeGroupItem);

    @Update
    void update(PlaceGroupItem placeGroupItem);

    @Delete
    void delete(PlaceGroupItem placeGroupItem);

    @Query("DELETE FROM place_group_item")
    void deleteAllPlaceGroupItems();

    @Query("SELECT * FROM place_group_item")
    LiveData<List<PlaceGroupItem>> getAllPlaceGroupItems();
}
