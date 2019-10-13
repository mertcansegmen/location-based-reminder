package com.mertcansegmen.locationbasedreminder.model.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "note")
public class Note {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String text;

    @ColumnInfo(name = "created_at")
    private Date createdAt;

    public Note(String text) {
        this.text = text;
    }

    public Note(String text, Date createdAt) {
        this.text = text;
        this.createdAt = createdAt;
    }

    public int getİd() {
        return id;
    }

    public void setİd(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
