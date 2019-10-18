package com.mertcansegmen.locationbasedreminder.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "alarm_attribute")
public class AlarmAttribute {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "reminder_id")
    private int reminderId;

    private boolean vibration;

    private int volume;

    @ColumnInfo(name = "ringtone_path")
    private String ringtonePath;

    public AlarmAttribute(int reminderId, boolean vibration, int volume, String ringtonePath) {
        this.reminderId = reminderId;
        this.vibration = vibration;
        this.volume = volume;
        this.ringtonePath = ringtonePath;
    }

    public int getİd() {
        return id;
    }

    public void setİd(int id) {
        this.id = id;
    }

    public int getReminderId() {
        return reminderId;
    }

    public void setReminderId(int reminderId) {
        this.reminderId = reminderId;
    }

    public boolean isVibration() {
        return vibration;
    }

    public void setVibration(boolean vibration) {
        this.vibration = vibration;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public String getRingtonePath() {
        return ringtonePath;
    }

    public void setRingtonePath(String ringtonePath) {
        this.ringtonePath = ringtonePath;
    }
}
