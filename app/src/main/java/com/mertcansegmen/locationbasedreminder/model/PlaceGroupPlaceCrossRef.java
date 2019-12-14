package com.mertcansegmen.locationbasedreminder.model;

import androidx.room.Entity;
import androidx.room.Index;

@Entity(primaryKeys = {"placeId", "placeGroupId"}, indices = @Index("placeGroupId"))
public class PlaceGroupPlaceCrossRef {

    private long placeId;

    private long placeGroupId;

    public PlaceGroupPlaceCrossRef(long placeId, long placeGroupId) {
        this.placeId = placeId;
        this.placeGroupId = placeGroupId;
    }

    public long getPlaceId() {
        return placeId;
    }

    public void setPlaceId(long placeId) {
        this.placeId = placeId;
    }

    public long getPlaceGroupId() {
        return placeGroupId;
    }

    public void setPlaceGroupId(long placeGroupId) {
        this.placeGroupId = placeGroupId;
    }
}
