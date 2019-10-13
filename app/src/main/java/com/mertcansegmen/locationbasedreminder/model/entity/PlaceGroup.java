package com.mertcansegmen.locationbasedreminder.model.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "place_group")
public class PlaceGroup {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String name;

    public PlaceGroup(String name) {
        this.name = name;
    }

    public int getİd() {
        return id;
    }

    public void setİd(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
