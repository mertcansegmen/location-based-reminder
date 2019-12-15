package com.mertcansegmen.locationbasedreminder.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class PlaceGroup {

    @PrimaryKey(autoGenerate = true)
    private long placeGroupId;

    private String name;

    @ColumnInfo(defaultValue = "CURRENT_TIMESTAMP")
    private String createdAt;

    public PlaceGroup(String name) {
        this.name = name;
    }

    public long getPlaceGroupId() {
        return placeGroupId;
    }

    public void setPlaceGroupId(long placeGroupId) {
        this.placeGroupId = placeGroupId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "PlaceGroup{" +
                "placeGroupId=" + placeGroupId +
                ", name='" + name + '\'' +
                '}';
    }
}
