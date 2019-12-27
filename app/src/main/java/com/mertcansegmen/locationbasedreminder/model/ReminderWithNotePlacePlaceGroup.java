package com.mertcansegmen.locationbasedreminder.model;

import androidx.room.Embedded;
import androidx.room.Relation;

public class ReminderWithNotePlacePlaceGroup {

    @Embedded
    private Reminder reminder;

    @Relation(
            parentColumn = "reminderId",
            entityColumn = "noteId"
    )
    private Note note;

    @Relation(
            parentColumn = "reminderId",
            entityColumn = "placeId"
    )
    private Place place;

    @Relation(
            parentColumn = "reminderId",
            entityColumn = "placeGroupId"
    )
    private PlaceGroup placeGroup;

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

    public PlaceGroup getPlaceGroup() {
        return placeGroup;
    }

    public void setPlaceGroup(PlaceGroup placeGroup) {
        this.placeGroup = placeGroup;
    }
}
