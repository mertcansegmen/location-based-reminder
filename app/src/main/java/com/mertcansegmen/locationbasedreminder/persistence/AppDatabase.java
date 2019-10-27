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
}, version = 2)
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
            noteDao.insert(new Note("Feed the dog", new Date()));
            noteDao.insert(new Note("Return book to library", new Date()));
            noteDao.insert(new Note("Reply to Angela", new Date()));
            placeDao.insert(new Place("Home", 42.421935, -71.065640));
            placeDao.insert(new Place("School", 42.360037, -71.087794));
            placeDao.insert(new Place("Walmart", 41.373641, -72.919015));
            placeDao.insert(new Place("Train Station", 41.297474, -72.926468));
            placeDao.insert(new Place("Bus Station", 41.180304, -73.187537));
            placeDao.insert(new Place("Jesica's Home", 41.190783, -73.139186));
            placeDao.insert(new Place("Grocery Store", 41.221928, -73.074861));
            return null;
        }
    }

}
