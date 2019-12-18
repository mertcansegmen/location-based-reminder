package com.mertcansegmen.locationbasedreminder.ui.places;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.mertcansegmen.locationbasedreminder.model.Place;
import com.mertcansegmen.locationbasedreminder.repository.PlaceRepository;

import java.util.List;

public class PlacesFragmentViewModel extends AndroidViewModel {

    private PlaceRepository repository;
    private LiveData<List<Place>> allPlaces;

    public PlacesFragmentViewModel(@NonNull Application application) {
        super(application);
        repository = new PlaceRepository(application);
        allPlaces = repository.getAllPlaces();
    }

    public void deleteAll() {
        repository.deleteAll();
    }

    public LiveData<List<Place>> getAllPlaces() {
        return allPlaces;
    }
}
