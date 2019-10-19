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

    public abstract PlaceDao placeDao();

    public abstract PlaceGroupDao placeGroupDao();

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
        private PlaceDao placeDao;

        private PopulateDbAsyncTask(AppDatabase db) {
            noteDao = db.noteDao();
            placeDao = db.placeDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            noteDao.insert(new Note("Öğrenci işlerine uğra", new Date()));
            noteDao.insert(new Note("Kitabı kütüphaneye iade et", new Date()));
            noteDao.insert(new Note("Algoritma ödevini yap", new Date()));
            placeDao.insert(new Place("Home", 37.12583, 26.48934));
            placeDao.insert(new Place("School", 37.13583, 26.45934));
            placeDao.insert(new Place("Walmart", 37.11583, 26.41934));
            placeDao.insert(new Place("Metro Station", 37.19583, 26.14934));
            placeDao.insert(new Place("Bus Station", 37.12583, 26.46934));
            placeDao.insert(new Place("Buy More", 37.15583, 26.42934));
            placeDao.insert(new Place("Down Town", 37.16583, 26.39934));
            return null;
        }
    }

}
