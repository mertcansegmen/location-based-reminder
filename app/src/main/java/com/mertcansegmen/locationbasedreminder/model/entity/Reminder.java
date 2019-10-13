package com.mertcansegmen.locationbasedreminder.model.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "reminder")
public class Reminder {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "note_id")
    private int noteId;

    @ColumnInfo(name = "place_id")
    private int placeId;

    @ColumnInfo(name = "place_group_id")
    private int placeGroupId;

    @ColumnInfo(name = "reminder_type_id")
    private int reminderTypeId;

    private int range;

    @ColumnInfo(name = "created_at")
    private Date createdAt;

    @ColumnInfo(name = "is_active")
    private boolean isActive;

    public Reminder(int noteId, int placeId, int placeGroupId, int reminderTypeId, int range, Date createdAt, boolean isActive) {
        this.noteId = noteId;
        this.placeId = placeId;
        this.placeGroupId = placeGroupId;
        this.reminderTypeId = reminderTypeId;
        this.range = range;
        this.createdAt = createdAt;
        this.isActive = isActive;
    }

    public int getİd() {
        return id;
    }

    public void setİd(int id) {
        this.id = id;
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

    public int getReminderTypeId() {
        return reminderTypeId;
    }

    public void setReminderTypeId(int reminderTypeId) {
        this.reminderTypeId = reminderTypeId;
    }

    public int getRange() {
        return range;
    }

    public void setRange(int range) {
        this.range = range;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
