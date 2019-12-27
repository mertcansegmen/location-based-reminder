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

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Database(entities = {
        Reminder.class, Note.class, AlarmAttribute.class,
        Place.class, PlaceGroup.class, PlaceGroupPlaceCrossRef.class
}, version = 16)
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

                noteDao.insert(new Note("Feed the dog"));
                noteDao.insert(new Note("Return book to library"));
                noteDao.insert(new Note("Reply to Angela"));
                noteDao.insert(new Note("Schedule meeting with Alex"));
                noteDao.insert(new Note("Find hotel recommendations in London"));
                noteDao.insert(new Note("Shopping List: \n" +
                        "Pasta\n" +
                        "Breakfast cereal\n" +
                        "Rice\n" +
                        "Soup\n" +
                        "Fruits, nuts and seeds\n" +
                        "Skinless white meat\n" +
                        "Salt\n" +
                        "Honey\n" +
                        "Eggs"));
                noteDao.insert(new Note("Find hotel recommendations in London"));
                noteDao.insert(new Note("Buy a graduation gift for Sarah"));
                noteDao.insert(new Note("Movies to Watch: \n" +
                        "Scorpion\n" +
                        "Joker\n" +
                        "Countdown\n" +
                        "Star Wars: The Rise of Skywalker"));

                placeDao.insert(new Place("Food Lion", 42.421935, -71.065640, 100));
                placeDao.insert(new Place("Target", 42.360037, -71.087794, 300));
                placeDao.insert(new Place("Walmart", 41.373641, -72.919015, 300));
                placeDao.insert(new Place("H&M", 41.297474, -72.926468, 100));
                placeDao.insert(new Place("Zara", 41.180304, -73.187537, 50));
                placeDao.insert(new Place("Mango", 41.190783, -73.139186, 40));
                placeDao.insert(new Place("Pull&Bear", 41.221928, -73.074861, 100));
                placeDao.insert(new Place("Shell", 41.297474, -72.926468, 100));
                placeDao.insert(new Place("Buc-ee's", 41.180304, -73.187537, 50));
                placeDao.insert(new Place("QuikTrip", 41.190783, -73.139186, 40));
                placeDao.insert(new Place("Hy-Vee Gas", 41.221928, -73.074861, 100));
                placeDao.insert(new Place("Celine Patisserie", 41.180304, -73.187537, 50));
                placeDao.insert(new Place("Daily Donuts", 41.190783, -73.139186, 40));
                placeDao.insert(new Place("Defloured ", 41.221928, -73.074861, 100));

                placeGroupDao.insert(new PlaceGroup("Grocery Stores"));
                placeGroupDao.insert(new PlaceGroup("Clothing Stores"));
                placeGroupDao.insert(new PlaceGroup("Gas Stations"));
                placeGroupDao.insert(new PlaceGroup("Bakeries"));

                placeGroupDao.insert(new PlaceGroupPlaceCrossRef(1, 1));
                placeGroupDao.insert(new PlaceGroupPlaceCrossRef(2, 1));
                placeGroupDao.insert(new PlaceGroupPlaceCrossRef(3, 1));
                placeGroupDao.insert(new PlaceGroupPlaceCrossRef(4, 2));
                placeGroupDao.insert(new PlaceGroupPlaceCrossRef(5, 2));
                placeGroupDao.insert(new PlaceGroupPlaceCrossRef(6, 2));
                placeGroupDao.insert(new PlaceGroupPlaceCrossRef(7, 2));
                placeGroupDao.insert(new PlaceGroupPlaceCrossRef(8, 3));
                placeGroupDao.insert(new PlaceGroupPlaceCrossRef(9, 3));
                placeGroupDao.insert(new PlaceGroupPlaceCrossRef(10, 3));
                placeGroupDao.insert(new PlaceGroupPlaceCrossRef(11, 3));
                placeGroupDao.insert(new PlaceGroupPlaceCrossRef(12, 4));
                placeGroupDao.insert(new PlaceGroupPlaceCrossRef(13, 4));
                placeGroupDao.insert(new PlaceGroupPlaceCrossRef(14, 4));
            });
        }
    };
}
