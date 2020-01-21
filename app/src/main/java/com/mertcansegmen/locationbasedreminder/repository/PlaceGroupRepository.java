package com.mertcansegmen.locationbasedreminder.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.mertcansegmen.locationbasedreminder.model.Place;
import com.mertcansegmen.locationbasedreminder.model.PlaceGroupPlaceCrossRef;
import com.mertcansegmen.locationbasedreminder.model.PlaceGroupWithPlaces;
import com.mertcansegmen.locationbasedreminder.persistence.AppDatabase;
import com.mertcansegmen.locationbasedreminder.persistence.PlaceGroupDao;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class PlaceGroupRepository {

    private AppDatabase database;

    private PlaceGroupDao placeGroupDao;
    private LiveData<List<PlaceGroupWithPlaces>> allPlaceGroupsWithPlaces;

    public PlaceGroupRepository(Application application) {
        database = AppDatabase.getInstance(application);
        placeGroupDao = database.placeGroupDao();
        allPlaceGroupsWithPlaces = placeGroupDao.getAllPlaceGroupsWithPlaces();
    }

    public void insert(PlaceGroupWithPlaces placeGroupWithPlaces) {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() ->
            database.runInTransaction(() -> {
                long placeGroupId = placeGroupDao.insert(placeGroupWithPlaces.getPlaceGroup());
                for(Place place : placeGroupWithPlaces.getPlaces()) {
                    placeGroupDao.insert(new PlaceGroupPlaceCrossRef(place.getPlaceId(), placeGroupId));
                }
            })
        );
    }

    public void update(PlaceGroupWithPlaces placeGroupWithPlaces) {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() ->
            database.runInTransaction(() -> {
                placeGroupDao.deletePlaceGroupRefs(placeGroupWithPlaces.getPlaceGroup().getPlaceGroupId());
                placeGroupDao.update(placeGroupWithPlaces.getPlaceGroup());
                for(Place place : placeGroupWithPlaces.getPlaces()) {
                    placeGroupDao.insert(new PlaceGroupPlaceCrossRef(place.getPlaceId(),
                            placeGroupWithPlaces.getPlaceGroup().getPlaceGroupId()));
                }
            })
        );
    }

    public void delete(PlaceGroupWithPlaces placeGroupWithPlaces) {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() ->
            database.runInTransaction(() -> {
                placeGroupDao.delete(placeGroupWithPlaces.getPlaceGroup());
                placeGroupDao.deletePlaceGroupRefs(placeGroupWithPlaces.getPlaceGroup().getPlaceGroupId());
            })
        );
    }

    public void deleteAll() {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() ->
            database.runInTransaction(() -> {
                placeGroupDao.deleteAllPlaceGroups();
                placeGroupDao.deleteAllPlaceGroupRefs();
            })
        );
    }

    public LiveData<List<PlaceGroupWithPlaces>> getAll() {
        return allPlaceGroupsWithPlaces;
    }
}