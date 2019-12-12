package com.mertcansegmen.locationbasedreminder.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.mertcansegmen.locationbasedreminder.model.PlaceGroupWithPlaces;
import com.mertcansegmen.locationbasedreminder.persistence.AppDatabase;
import com.mertcansegmen.locationbasedreminder.persistence.PlaceGroupDao;

import java.util.List;

public class PlaceGroupRepository {

    private PlaceGroupDao placeGroupDao;
    private LiveData<List<PlaceGroupWithPlaces>> allPlaceGroupsWithPlaces;

    public PlaceGroupRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        placeGroupDao = database.placeGroupDao();
        allPlaceGroupsWithPlaces = placeGroupDao.getAllPlaceGroupsWithPlaces();
    }

    public LiveData<List<PlaceGroupWithPlaces>> getAllPlaceGroupsWithPlaces() {
        return allPlaceGroupsWithPlaces;
    }
}
