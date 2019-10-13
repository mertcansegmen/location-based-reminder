package com.mertcansegmen.locationbasedreminder.model.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "reminder_type")
public class ReminderType {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private int name;

    public ReminderType(int name) {
        this.name = name;
    }

    public int getİd() {
        return id;
    }

    public void setİd(int id) {
        this.id = id;
    }

    public int getName() {
        return name;
    }

    public void setName(int name) {
        this.name = name;
    }
}
