package com.mertcansegmen.locationbasedreminder.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.mertcansegmen.locationbasedreminder.util.DateConverter;

import java.util.Date;

@Entity(tableName = "note")
public class Note implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String text;

    @ColumnInfo(name = "created_at")
    @TypeConverters({DateConverter.class})
    private Date createdAt;

    public Note(String text, Date createdAt) {
        this.text = text;
        this.createdAt = createdAt;
    }

    protected Note(Parcel in) {
        id = in.readInt();
        text = in.readString();
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

    public static final Creator<Note> CREATOR = new Creator<Note>() {
        @Override
        public Note createFromParcel(Parcel in) {
            return new Note(in);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(text);
    }
}
