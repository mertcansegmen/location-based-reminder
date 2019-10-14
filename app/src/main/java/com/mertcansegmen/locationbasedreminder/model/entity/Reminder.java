package com.mertcansegmen.locationbasedreminder.model.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.mertcansegmen.locationbasedreminder.util.DateConverter;

import java.util.Date;

@Entity(tableName = "reminder", foreignKeys = {
        @ForeignKey(
                entity = Note.class,
                parentColumns = "id",
                childColumns = "note_id",
                onDelete = ForeignKey.CASCADE
        ),
        @ForeignKey(
                entity = Place.class,
                parentColumns = "id",
                childColumns = "place_id"
        ),
        @ForeignKey(
                entity = PlaceGroup.class,
                parentColumns = "id",
                childColumns = "place_group_id"
        )
})
public class Reminder {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "note_id")
    private int noteId;

    @ColumnInfo(name = "place_id")
    private int placeId;

    @ColumnInfo(name = "place_group_id")
    private int placeGroupId;

    private int range;

    @ColumnInfo(name = "created_at")
    @TypeConverters({DateConverter.class})
    private Date createdAt;

    @ColumnInfo(name = "is_active")
    private boolean isActive;

    public Reminder(int noteId, int placeId, int placeGroupId, int range, Date createdAt, boolean isActive) {
        this.noteId = noteId;
        this.placeId = placeId;
        this.placeGroupId = placeGroupId;
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
