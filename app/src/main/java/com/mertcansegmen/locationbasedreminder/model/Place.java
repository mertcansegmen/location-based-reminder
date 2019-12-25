package com.mertcansegmen.locationbasedreminder.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class Place implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private Long placeId;

    private String name;

    private double latitude;

    private double longitude;

    private int radius;

    @ColumnInfo(defaultValue = "CURRENT_TIMESTAMP")
    private String createdAt;

    @Ignore
    public Place() {}

    public Place(String name, double latitude, double longitude, int radius) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.radius = radius;
    }

    public Long getPlaceId() {
        return placeId;
    }

    public void setPlaceId(Long placeId) {
        this.placeId = placeId;
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

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    protected Place(Parcel in) {
        placeId = in.readLong();
        name = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
        radius = in.readInt();
    }

    public static final Creator<Place> CREATOR = new Creator<Place>() {
        @Override
        public Place createFromParcel(Parcel in) {
            return new Place(in);
        }

        @Override
        public Place[] newArray(int size) {
            return new Place[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(placeId);
        dest.writeString(name);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeInt(radius);
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if(obj instanceof Place) {
            return placeId.equals(((Place) obj).getPlaceId());
        }
        return false;
    }

    @Override
    public String toString() {
        return "Place{" +
                "placeId=" + placeId +
                ", name='" + name + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", radius=" + radius +
                '}';
    }
}