package com.mertcansegmen.locationbasedreminder.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.mertcansegmen.locationbasedreminder.util.DateConverter;

import java.util.Date;

@Entity(foreignKeys = {
        @ForeignKey(
                entity = Note.class,
                parentColumns = "noteId",
                childColumns = "noteId",
                onDelete = ForeignKey.CASCADE
        ),
        @ForeignKey(
                entity = Place.class,
                parentColumns = "placeId",
                childColumns = "placeId"
        ),
        @ForeignKey(
                entity = PlaceGroup.class,
                parentColumns = "placeGroupId",
                childColumns = "placeGroupId"
        )
})
public class Reminder {

    @PrimaryKey(autoGenerate = true)
    private long reminderId;

    @ColumnInfo(index = true)
    private int noteId;

    @ColumnInfo(index = true)
    private int placeId;

    @ColumnInfo(index = true)
    private int placeGroupId;

    @TypeConverters({DateConverter.class})
    private Date createdAt;

    private boolean isActive;

    public Reminder(int noteId, int placeId, int placeGroupId, Date createdAt, boolean isActive) {
        this.noteId = noteId;
        this.placeId = placeId;
        this.placeGroupId = placeGroupId;
        this.createdAt = createdAt;
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

    @Override
    public String toString() {
        return "Reminder{" +
                "reminderId=" + reminderId +
                ", noteId=" + noteId +
                ", placeId=" + placeId +
                ", placeGroupId=" + placeGroupId +
                ", createdAt=" + createdAt +
                ", isActive=" + isActive +
                '}';
    }
}
