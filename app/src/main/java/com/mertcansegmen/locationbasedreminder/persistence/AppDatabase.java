package com.mertcansegmen.locationbasedreminder.persistence;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.mertcansegmen.locationbasedreminder.model.AlarmAttribute;
import com.mertcansegmen.locationbasedreminder.model.Note;
import com.mertcansegmen.locationbasedreminder.model.Place;
import com.mertcansegmen.locationbasedreminder.model.PlaceGroup;
import com.mertcansegmen.locationbasedreminder.model.PlaceGroupPlaceCrossRef;
import com.mertcansegmen.locationbasedreminder.model.Reminder;

import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Database(entities = {
        Reminder.class, Note.class, AlarmAttribute.class,
        Place.class, PlaceGroup.class, PlaceGroupPlaceCrossRef.class
}, version = 10)
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
            Executor executor = Executors.newSingleThreadExecutor();
            executor.execute(() -> {
                NoteDao noteDao = instance.noteDao();
                PlaceDao placeDao = instance.placeDao();
                PlaceGroupDao placeGroupDao = instance.placeGroupDao();

                noteDao.insert(new Note("Feed the dog", new Date()));
                noteDao.insert(new Note("Return book to library", new Date()));
                noteDao.insert(new Note("Reply to Angela", new Date()));

                placeDao.insert(new Place("Food Lion", 42.421935, -71.065640, 100));
                placeDao.insert(new Place("Target", 42.360037, -71.087794, 300));
                placeDao.insert(new Place("Walmart", 41.373641, -72.919015, 300));
                placeDao.insert(new Place("H&M", 41.297474, -72.926468, 100));
                placeDao.insert(new Place("Zara", 41.180304, -73.187537, 50));
                placeDao.insert(new Place("Mango", 41.190783, -73.139186, 40));
                placeDao.insert(new Place("Pull&Bear", 41.221928, -73.074861, 100));
            });
        }
    };
}
