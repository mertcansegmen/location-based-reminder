package com.mertcansegmen.locationbasedreminder.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Note implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private long noteId;

    private String body;

    @ColumnInfo(defaultValue = "CURRENT_TIMESTAMP")
    private String createdAt;

    public Note(String body) {
        this.body = body;
    }

    public long getNoteId() {
        return noteId;
    }

    public void setNoteId(long noteId) {
        this.noteId = noteId;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    protected Note(Parcel in) {
        noteId = in.readLong();
        body = in.readString();
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
        dest.writeLong(noteId);
        dest.writeString(body);
    }

    @Override
    public String toString() {
        return "Note{" +
                "noteId=" + noteId +
                ", body='" + body + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
