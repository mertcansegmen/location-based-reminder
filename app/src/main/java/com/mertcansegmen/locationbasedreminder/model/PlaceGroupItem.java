package com.mertcansegmen.locationbasedreminder.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "place_group_item", foreignKeys = {
        @ForeignKey(
                entity = PlaceGroup.class,
                parentColumns = "id",
                childColumns = "place_group_id"
        ),
        @ForeignKey(
                entity = Place.class,
                parentColumns = "id",
                childColumns = "place_id"
        )
})
public class PlaceGroupItem {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "place_id", index = true)
    private int placeId;

    @ColumnInfo(name = "place_group_id", index = true)
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
