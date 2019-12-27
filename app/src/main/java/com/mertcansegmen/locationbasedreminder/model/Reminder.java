package com.mertcansegmen.locationbasedreminder.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Reminder {

    @PrimaryKey(autoGenerate = true)
    private long reminderId;

    private int noteId;

    private int placeId;

    private int placeGroupId;

    private boolean isActive;

    public Reminder(int noteId, int placeId, int placeGroupId, boolean isActive) {
        this.noteId = noteId;
        this.placeId = placeId;
        this.placeGroupId = placeGroupId;
        this.isActive = isActive;
    }

    public long getReminderId() {
        return reminderId;
    }

    public void setReminderId(long reminderId) {
        this.reminderId = reminderId;
    }

    public int getNoteId() {
        return noteId;
    }

    public void setNoteId(int noteId) {
        this.noteId = noteId;
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

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    @Override
    public String toString() {
        return "Reminder{" +
                "reminderId=" + reminderId +
                ", noteId=" + noteId +
                ", placeId=" + placeId +
                ", placeGroupId=" + placeGroupId +
                ", isActive=" + isActive +
                '}';
    }
}