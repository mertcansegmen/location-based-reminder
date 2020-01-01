package com.mertcansegmen.locationbasedreminder.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Reminder {

    @PrimaryKey(autoGenerate = true)
    private long reminderId;

    private long noteId;

    private Long placeId;

    private Long placeGroupId;

    private boolean isActive;

    public Reminder(long noteId, Long placeId, Long placeGroupId, boolean isActive) {
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

    public long getNoteId() {
        return noteId;
    }

    public void setNoteId(long noteId) {
        this.noteId = noteId;
    }

    public Long getPlaceId() {
        return placeId;
    }

    public void setPlaceId(Long placeId) {
        this.placeId = placeId;
    }

    public Long getPlaceGroupId() {
        return placeGroupId;
    }

    public void setPlaceGroupId(Long placeGroupId) {
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