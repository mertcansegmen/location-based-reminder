package com.mertcansegmen.locationbasedreminder.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Embedded;
import androidx.room.Ignore;
import androidx.room.Junction;
import androidx.room.Relation;

import java.util.List;

public class PlaceGroupWithPlaces implements Parcelable {
    @Embedded
    private PlaceGroup placeGroup;

    @Relation(
        parentColumn = "placeGroupId",
        entityColumn = "placeId",
        associateBy  = @Junction(PlaceGroupPlaceCrossRef.class)
    )
    private List<Place> places;

    @Ignore
    public PlaceGroupWithPlaces() {}

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

    protected PlaceGroupWithPlaces(Parcel in) {
        places = in.createTypedArrayList(Place.CREATOR);
    }

    public static final Creator<PlaceGroupWithPlaces> CREATOR = new Creator<PlaceGroupWithPlaces>() {
        @Override
        public PlaceGroupWithPlaces createFromParcel(Parcel in) {
            return new PlaceGroupWithPlaces(in);
        }

        @Override
        public PlaceGroupWithPlaces[] newArray(int size) {
            return new PlaceGroupWithPlaces[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(places);
    }

    @Override
    public String toString() {
        return "PlaceGroupWithPlaces{" +
                "placeGroup=" + placeGroup +
                ", places=" + places +
                '}';
    }
}
