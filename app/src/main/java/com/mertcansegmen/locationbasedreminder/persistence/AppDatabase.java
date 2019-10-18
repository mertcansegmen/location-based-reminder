package com.mertcansegmen.locationbasedreminder.persistence;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.mertcansegmen.locationbasedreminder.model.AlarmAttribute;
import com.mertcansegmen.locationbasedreminder.model.Note;
import com.mertcansegmen.locationbasedreminder.model.Place;
import com.mertcansegmen.locationbasedreminder.model.PlaceGroup;
import com.mertcansegmen.locationbasedreminder.model.PlaceGroupItem;
import com.mertcansegmen.locationbasedreminder.model.Reminder;

import java.util.Date;

@Database(entities = {
        Reminder.class, Note.class, AlarmAttribute.class,
        Place.class, PlaceGroup.class, PlaceGroupItem.class
}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instance;

    public abstract ReminderDao reminderDao();

    public abstract NoteDao noteDao();

    public abstract AlarmAttributeDao alarmAttributeDao();

    public abstract PlaceDao placeDao();

    public abstract PlaceGroupDao placeGroupDao();

    public abstract PlaceGroupItemDao placeGroupItemDao();

    public static synchronized AppDatabase getInstance(Context context) {
        if(instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    AppDatabase.class, "location_based_reminder_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallBack)
                    .build();
        }
    return instance;
    }

    private static RoomDatabase.Callback roomCallBack = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
        private NoteDao noteDao;

        private PopulateDbAsyncTask(AppDatabase db) {
            noteDao = db.noteDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            noteDao.insert(new Note("Öğrenci işlerine uğra", new Date()));
            noteDao.insert(new Note("Kitabı kütüphaneye iade et", new Date()));
            noteDao.insert(new Note("Algoritma ödevini yap", new Date()));
            return null;
        }
    }

}
