package com.mertcansegmen.locationbasedreminder.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "place")
public class Place {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String name;

    private double latitude;

    private double longitude;

    public Place(String name, double latitude, double longitude) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
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

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
