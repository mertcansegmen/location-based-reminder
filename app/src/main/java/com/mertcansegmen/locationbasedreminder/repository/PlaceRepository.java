package com.mertcansegmen.locationbasedreminder.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.mertcansegmen.locationbasedreminder.model.Place;
import com.mertcansegmen.locationbasedreminder.persistence.AppDatabase;
import com.mertcansegmen.locationbasedreminder.persistence.PlaceDao;

import java.util.List;

public class PlaceRepository {

    private PlaceDao placeDao;
    private LiveData<List<Place>> allPlaces;

    public PlaceRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        placeDao = database.placeDao();
        allPlaces = placeDao.getAllPlaces();
    }

    public void insert(Place place) {
        new PlaceRepository.InsertPlaceAsyncTask(placeDao).execute(place);
    }

    public void update(Place place) {
        new PlaceRepository.UpdatePlaceAsyncTask(placeDao).execute(place);
    }

    public void delete(Place place) {
        new PlaceRepository.DeletePlaceAsyncTask(placeDao).execute(place);
    }

    public LiveData<List<Place>> getAllPlaces() {
        return allPlaces;
    }

    private static class InsertPlaceAsyncTask extends AsyncTask<Place, Void, Void> {

        private PlaceDao placeDao;

        InsertPlaceAsyncTask(PlaceDao placeDao) {
            this.placeDao = placeDao;
        }

        @Override
        protected Void doInBackground(Place... places) {
            placeDao.insert(places[0]);
            return null;
        }
    }

    private static class UpdatePlaceAsyncTask extends AsyncTask<Place, Void, Void> {

        private PlaceDao placeDao;

        UpdatePlaceAsyncTask(PlaceDao placeDao) {
            this.placeDao = placeDao;
        }

        @Override
        protected Void doInBackground(Place... places) {
            placeDao.update(places[0]);
            return null;
        }
    }

    private static class DeletePlaceAsyncTask extends AsyncTask<Place, Void, Void> {

        private PlaceDao placeDao;

        DeletePlaceAsyncTask(PlaceDao placeDao) {
            this.placeDao = placeDao;
        }

        @Override
        protected Void doInBackground(Place... places) {
            placeDao.delete(places[0]);
            return null;
        }
    }
}
