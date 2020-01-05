package com.mertcansegmen.locationbasedreminder.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Embedded;
import androidx.room.Ignore;
import androidx.room.Relation;

public class ReminderWithNotePlacePlaceGroup implements Parcelable {

    @Embedded
    private Reminder reminder;

    @Relation(parentColumn = "noteId", entityColumn = "noteId")
    private Note note;

    @Relation(parentColumn = "placeId", entityColumn = "placeId")
    private Place place;

    @Relation(parentColumn = "placeGroupId", entityColumn = "placeGroupId", entity = PlaceGroup.class)
    private PlaceGroupWithPlaces placeGroupWithPlaces;

    public ReminderWithNotePlacePlaceGroup() {}

    public Reminder getReminder() {
        return reminder;
    }

    public void setReminder(Reminder reminder) {
        this.reminder = reminder;
    }

    public Note getNote() {
        return note;
    }

    public void setNote(Note note) {
        this.note = note;
    }

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    public PlaceGroupWithPlaces getPlaceGroupWithPlaces() {
        return placeGroupWithPlaces;
    }

    public void setPlaceGroupWithPlaces(PlaceGroupWithPlaces placeGroupWithPlaces) {
        this.placeGroupWithPlaces = placeGroupWithPlaces;
    }

    @Override
    public String toString() {
        return "ReminderWithNotePlacePlaceGroup{" +
                "reminder=" + reminder +
                ", note=" + note +
                ", place=" + place +
                ", placeGroupWithPlaces=" + placeGroupWithPlaces +
                '}';
    }

    protected ReminderWithNotePlacePlaceGroup(Parcel in) {
        note = in.readParcelable(Note.class.getClassLoader());
        place = in.readParcelable(Place.class.getClassLoader());
        placeGroupWithPlaces = in.readParcelable(PlaceGroupWithPlaces.class.getClassLoader());
    }

    public static final Creator<ReminderWithNotePlacePlaceGroup> CREATOR = new Creator<ReminderWithNotePlacePlaceGroup>() {
        @Override
        public ReminderWithNotePlacePlaceGroup createFromParcel(Parcel in) {
            return new ReminderWithNotePlacePlaceGroup(in);
        }

        @Override
        public ReminderWithNotePlacePlaceGroup[] newArray(int size) {
            return new ReminderWithNotePlacePlaceGroup[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(note, flags);
        dest.writeParcelable(place, flags);
        dest.writeParcelable(placeGroupWithPlaces, flags);
    }
}
