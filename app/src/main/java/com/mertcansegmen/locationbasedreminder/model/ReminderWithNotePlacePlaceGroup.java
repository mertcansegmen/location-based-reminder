package com.mertcansegmen.locationbasedreminder.model;

import androidx.room.Embedded;
import androidx.room.Relation;

public class ReminderWithNotePlacePlaceGroup {

    @Embedded
    private Reminder reminder;

    @Relation(parentColumn = "noteId", entityColumn = "noteId")
    private Note note;

    @Relation(parentColumn = "placeId", entityColumn = "placeId")
    private Place place;

    @Relation(parentColumn = "placeGroupId", entityColumn = "placeGroupId", entity = PlaceGroup.class)
    private PlaceGroupWithPlaces placeGroupWithPlaces;

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
}
