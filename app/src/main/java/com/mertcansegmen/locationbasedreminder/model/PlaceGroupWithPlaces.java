package com.mertcansegmen.locationbasedreminder.model;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import java.util.List;

public class PlaceGroupWithPlaces {
    @Embedded
    private PlaceGroup placeGroup;

    @Relation(
        parentColumn = "placeGroupId",
        entityColumn = "placeId",
        associateBy  = @Junction(PlaceGroupPlaceCrossRef.class)
    )
    private List<Place> places;

    public PlaceGroup getPlaceGroup() {
        return placeGroup;
    }

    public void setPlaceGroup(PlaceGroup placeGroup) {
        this.placeGroup = placeGroup;
    }

    public List<Place> getPlaces() {
        return places;
    }

    public void setPlaces(List<Place> places) {
        this.places = places;
    }

    @Override
    public String toString() {
        return "PlaceGroupWithPlaces{" +
                "placeGroup=" + placeGroup +
                ", places=" + places +
                '}';
    }
}
