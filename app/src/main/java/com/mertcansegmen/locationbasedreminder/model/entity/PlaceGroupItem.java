package com.mertcansegmen.locationbasedreminder.model.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "place_group_item")
public class PlaceGroupItem {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "place_id")
    private int placeId;

    @ColumnInfo(name = "place_group_id")
    private int placeGroupId;

    public PlaceGroupItem(int placeId, int placeGroupId) {
        this.placeId = placeId;
        this.placeGroupId = placeGroupId;
    }

    public int getİd() {
        return id;
    }

    public void setİd(int id) {
        this.id = id;
    }

    public int getPlaceId() {
        return placeId;
    }

    public void setPlaceId(int placeId) {
        this.placeId = placeId;
    }

    public int getPlaceGroupId() {
        return placeGroupId;
    }

    public void setPlaceGroupId(int placeGroupId) {
        this.placeGroupId = placeGroupId;
    }
}
