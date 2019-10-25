package com.mertcansegmen.locationbasedreminder.ui.addeditplace;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.mertcansegmen.locationbasedreminder.model.Place;
import com.mertcansegmen.locationbasedreminder.repository.PlaceRepository;

public class AddEditPlaceFragmentViewModel extends AndroidViewModel {

    private PlaceRepository repository;

    public AddEditPlaceFragmentViewModel(@NonNull Application application) {
        super(application);
        repository = new PlaceRepository(application);
    }

    public void delete(Place place) {
        repository.delete(place);
    }
}
