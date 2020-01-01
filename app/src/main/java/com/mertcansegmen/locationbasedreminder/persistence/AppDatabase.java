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
}, version = 18)
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
                ReminderDao reminderDao = instance.reminderDao();

                noteDao.insert(new Note("Feed the dog"));
                noteDao.insert(new Note("Schedule meeting with Alex"));
                noteDao.insert(new Note("Find hotel recommendations in London"));
                noteDao.insert(new Note("Do laundries"));
                noteDao.insert(new Note("Shopping List",
                        "Curly Pasta\n" +
                        "Breakfast cereal\n" +
                        "Rice\n" +
                        "Soup\n" +
                        "Fruits, nuts and seeds\n" +
                        "Skinless white meat\n" +
                        "Salt\n" +
                        "Honey\n" +
                        "Eggs"));
                noteDao.insert(new Note("Buy a graduation gift for Sarah"));
                noteDao.insert(new Note("Return book to library"));
                noteDao.insert(new Note("Reply to Angela"));
                noteDao.insert(new Note("Movies to Watch",
                        "Scorpion\n" +
                        "Joker\n" +
                        "Countdown\n" +
                        "Star Wars: The Rise of Skywalker"));
                noteDao.insert(new Note("Buy a cooler for the laptop"));
                noteDao.insert(new Note("Return Quinn's flash drive back"));
                noteDao.insert(new Note("Do laundries"));

                noteDao.insert(new Note("Buy a cooler for the laptop"));
                noteDao.insert(new Note("Return Quinn's flash drive back"));
                noteDao.insert(new Note("Do laundries"));
                noteDao.insert(new Note("Shopping List",
                        "Curly Pasta\n" +
                                "Breakfast cereal\n" +
                                "Rice\n" +
                                "Soup\n" +
                                "Fruits, nuts and seeds\n" +
                                "Skinless white meat\n" +
                                "Salt\n" +
                                "Honey\n" +
                                "Eggs"));
                noteDao.insert(new Note("Buy a graduation gift for Sarah"));

                placeDao.insert(new Place("Food Lion", 42.421935, -71.065640, 100));
                placeDao.insert(new Place("Falletti Foods", 42.360037, -71.087794, 300));
                placeDao.insert(new Place("Bi-Rite Market", 41.373641, -72.919015, 300));
                placeDao.insert(new Place("Rainbow Grocery", 41.373641, -72.919015, 300));
                placeDao.insert(new Place("H&M", 41.297474, -72.926468, 100));
                placeDao.insert(new Place("Old Navy", 41.180304, -73.187537, 100));
                placeDao.insert(new Place("Mango", 41.190783, -73.139186, 160));
                placeDao.insert(new Place("Pull&Bear", 41.221928, -73.074861, 100));
                placeDao.insert(new Place("Shell", 41.297474, -72.926468, 600));
                placeDao.insert(new Place("Buc-ee's", 41.180304, -73.187537, 400));
                placeDao.insert(new Place("QuikTrip", 41.190783, -73.139186, 120));
                placeDao.insert(new Place("Hy-Vee Gas", 41.221928, -73.074861, 100));
                placeDao.insert(new Place("Celine Patisserie", 41.180304, -73.187537, 150));
                placeDao.insert(new Place("Daily Donuts", 41.190783, -73.139186, 100));
                placeDao.insert(new Place("Defloured", 41.221928, -73.074861, 100));
                placeDao.insert(new Place("Crocker Galleria", 41.221928, -73.074861, 100));
                placeDao.insert(new Place("Embarcadero Center", 41.180304, -73.187537, 150));
                placeDao.insert(new Place("Osaka Way", 41.190783, -73.139186, 100));
                placeDao.insert(new Place("Potrero Center ", 41.221928, -73.074861, 100));
                placeDao.insert(new Place("Dyson Demo Store", 41.180304, -73.187537, 150));
                placeDao.insert(new Place("Central Computers", 41.190783, -73.139186, 100));
                placeDao.insert(new Place("Best Buy ", 41.221928, -73.074861, 100));
                placeDao.insert(new Place("Quinn's House", 41.221928, -73.074861, 200));
                placeDao.insert(new Place("Home", 41.221928, -73.074861, 200));

                placeGroupDao.insert(new PlaceGroup("Grocery Stores"));
                placeGroupDao.insert(new PlaceGroup("Clothing Stores"));
                placeGroupDao.insert(new PlaceGroup("Gas Stations"));
                placeGroupDao.insert(new PlaceGroup("Bakeries"));
                placeGroupDao.insert(new PlaceGroup("Shopping Malls"));
                placeGroupDao.insert(new PlaceGroup("Electronics Stores"));

                placeGroupDao.insert(new PlaceGroupPlaceCrossRef(1, 1));
                placeGroupDao.insert(new PlaceGroupPlaceCrossRef(2, 1));
                placeGroupDao.insert(new PlaceGroupPlaceCrossRef(3, 1));
                placeGroupDao.insert(new PlaceGroupPlaceCrossRef(4, 1));
                placeGroupDao.insert(new PlaceGroupPlaceCrossRef(5, 2));
                placeGroupDao.insert(new PlaceGroupPlaceCrossRef(6, 2));
                placeGroupDao.insert(new PlaceGroupPlaceCrossRef(7, 2));
                placeGroupDao.insert(new PlaceGroupPlaceCrossRef(8, 2));
                placeGroupDao.insert(new PlaceGroupPlaceCrossRef(9, 3));
                placeGroupDao.insert(new PlaceGroupPlaceCrossRef(10, 3));
                placeGroupDao.insert(new PlaceGroupPlaceCrossRef(11, 3));
                placeGroupDao.insert(new PlaceGroupPlaceCrossRef(12, 3));
                placeGroupDao.insert(new PlaceGroupPlaceCrossRef(13, 4));
                placeGroupDao.insert(new PlaceGroupPlaceCrossRef(14, 4));
                placeGroupDao.insert(new PlaceGroupPlaceCrossRef(15, 4));
                placeGroupDao.insert(new PlaceGroupPlaceCrossRef(16, 5));
                placeGroupDao.insert(new PlaceGroupPlaceCrossRef(17, 5));
                placeGroupDao.insert(new PlaceGroupPlaceCrossRef(18, 5));
                placeGroupDao.insert(new PlaceGroupPlaceCrossRef(19, 5));
                placeGroupDao.insert(new PlaceGroupPlaceCrossRef(20, 6));
                placeGroupDao.insert(new PlaceGroupPlaceCrossRef(21, 6));
                placeGroupDao.insert(new PlaceGroupPlaceCrossRef(22, 6));

                reminderDao.insert(new Reminder(13, null, 6L, true));
                reminderDao.insert(new Reminder(14, 23L, null, true));
                reminderDao.insert(new Reminder(15, 24L, null, true));
                reminderDao.insert(new Reminder(16, null, 1L, true));
                reminderDao.insert(new Reminder(17, null, 5L, true));
            });
        }
    };
}
