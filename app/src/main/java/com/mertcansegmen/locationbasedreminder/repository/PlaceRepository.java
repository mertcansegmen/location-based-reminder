package com.mertcansegmen.locationbasedreminder.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.mertcansegmen.locationbasedreminder.model.Place;
import com.mertcansegmen.locationbasedreminder.persistence.AppDatabase;
import com.mertcansegmen.locationbasedreminder.persistence.PlaceDao;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class PlaceRepository {

    private PlaceDao placeDao;
    private LiveData<List<Place>> allPlaces;

    public PlaceRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        placeDao = database.placeDao();
        allPlaces = placeDao.getAllPlaces();
    }

    public void insert(Place place) {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> placeDao.insert(place));
    }

    public void update(Place place) {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> placeDao.update(place));
    }

    public void delete(Place place) {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> placeDao.delete(place));
    }

    public void deleteAll() {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> placeDao.deleteAllPlaces());
    }

    public LiveData<List<Place>> getAllPlaces() {
        return allPlaces;
    }
}
