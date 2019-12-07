package com.mertcansegmen.locationbasedreminder.repository;

import android.app.Application;
import android.os.AsyncTask;

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
        executor.execute(new Runnable() {
            @Override
            public void run() {
                placeDao.insert(place);
            }
        });
    }

    public void update(Place place) {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                placeDao.update(place);
            }
        });
    }

    public void delete(Place place) {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                placeDao.delete(place);
            }
        });
    }

    public LiveData<List<Place>> getAllPlaces() {
        return allPlaces;
    }
}
