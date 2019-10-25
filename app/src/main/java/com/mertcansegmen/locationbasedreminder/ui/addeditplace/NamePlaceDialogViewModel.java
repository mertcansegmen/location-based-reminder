package com.mertcansegmen.locationbasedreminder.ui.addeditplace;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.mertcansegmen.locationbasedreminder.model.Place;
import com.mertcansegmen.locationbasedreminder.repository.PlaceRepository;

public class NamePlaceDialogViewModel extends AndroidViewModel {

    private PlaceRepository repository;

    public NamePlaceDialogViewModel(@NonNull Application application) {
        super(application);
        repository = new PlaceRepository(application);
    }

    public void insert(Place place) {
        repository.insert(place);
    }

    public void update(Place place) {
        repository.update(place);
    }

    public void delete(Place place) {
        repository.delete(place);
    }
}
