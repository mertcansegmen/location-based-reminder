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
}, version = 20)
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
//        @Override
//        public void onOpen(@NonNull SupportSQLiteDatabase db) {
//            super.onOpen(db);
//            onCreate(db);
//        }

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

                long note1Id = noteDao.insert(new Note("Buy a cooler for the laptop"));
                long note2Id = noteDao.insert(new Note("Return Quinn's flash drive back"));
                long note3Id = noteDao.insert(new Note("Do laundries"));
                long note4Id = noteDao.insert(new Note("Shopping List",
                        "Curly Pasta\n" +
                                "Breakfast cereal\n" +
                                "Rice\n" +
                                "Soup\n" +
                                "Fruits, nuts and seeds\n" +
                                "Skinless white meat\n" +
                                "Salt\n" +
                                "Honey\n" +
                                "Eggs"));
                long note5Id = noteDao.insert(new Note("Buy a graduation gift for Sarah"));
                long note6Id = noteDao.insert(new Note("Return book to library"));
                long note7Id = noteDao.insert(new Note("Withdraw money"));

                long place1Id = placeDao.insert(new Place("Food Lion", 42.421935, -71.065640, 100));
                long place2Id = placeDao.insert(new Place("Falletti Foods", 42.360037, -71.087794, 300));
                long place3Id = placeDao.insert(new Place("Bi-Rite Market", 41.373641, -72.919015, 300));
                long place4Id = placeDao.insert(new Place("Rainbow Grocery", 41.373641, -72.919015, 300));
                long place5Id = placeDao.insert(new Place("H&M", 41.297474, -72.926468, 100));
                long place6Id = placeDao.insert(new Place("Old Navy", 41.180304, -73.187537, 100));
                long place7Id = placeDao.insert(new Place("Mango", 41.190783, -73.139186, 160));
                long place8Id = placeDao.insert(new Place("Pull&Bear", 41.221928, -73.074861, 100));
                long place9Id = placeDao.insert(new Place("Shell", 41.297474, -72.926468, 600));
                long place10Id = placeDao.insert(new Place("Buc-ee's", 41.180304, -73.187537, 400));
                long place11Id = placeDao.insert(new Place("QuikTrip", 41.190783, -73.139186, 120));
                long place12Id = placeDao.insert(new Place("Hy-Vee Gas", 41.221928, -73.074861, 100));
                long place13Id = placeDao.insert(new Place("Celine Patisserie", 41.180304, -73.187537, 150));
                long place14Id = placeDao.insert(new Place("Daily Donuts", 41.190783, -73.139186, 100));
                long place15Id = placeDao.insert(new Place("Defloured", 41.221928, -73.074861, 100));
                long place16Id = placeDao.insert(new Place("Crocker Galleria", 41.221928, -73.074861, 100));
                long place17Id = placeDao.insert(new Place("Embarcadero Center", 41.180304, -73.187537, 150));
                long place18Id = placeDao.insert(new Place("Osaka Way", 41.190783, -73.139186, 100));
                long place19Id = placeDao.insert(new Place("Potrero Center ", 41.221928, -73.074861, 100));
                long place20Id = placeDao.insert(new Place("Dyson Demo Store", 41.180304, -73.187537, 150));
                long place21Id = placeDao.insert(new Place("Central Computers", 41.190783, -73.139186, 100));
                long place22Id = placeDao.insert(new Place("Best Buy ", 41.221928, -73.074861, 100));
                long place23Id = placeDao.insert(new Place("Quinn's House", 41.221928, -73.074861, 200));
                long place24Id = placeDao.insert(new Place("Home", 41.221928, -73.074861, 200));
                long place25Id = placeDao.insert(new Place("School", 41.221928, -73.074861, 150));
                long place26Id = placeDao.insert(new Place("ATM", 47.221928, -73.074861, 130));

                long placeGroup1Id = placeGroupDao.insert(new PlaceGroup("Grocery Stores"));
                long placeGroup2Id = placeGroupDao.insert(new PlaceGroup("Clothing Stores"));
                long placeGroup3Id = placeGroupDao.insert(new PlaceGroup("Gas Stations"));
                long placeGroup4Id = placeGroupDao.insert(new PlaceGroup("Bakeries"));
                long placeGroup5Id = placeGroupDao.insert(new PlaceGroup("Shopping Malls"));
                long placeGroup6Id = placeGroupDao.insert(new PlaceGroup("Electronics Stores"));

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

                reminderDao.insert(new Reminder(note1Id, null, placeGroup6Id, false));
                reminderDao.insert(new Reminder(note2Id, place23Id, null, false));
                reminderDao.insert(new Reminder(note3Id, place24Id, null, false));
                reminderDao.insert(new Reminder(note4Id, null, placeGroup1Id, false));
                reminderDao.insert(new Reminder(note5Id, null, placeGroup5Id, false));
                reminderDao.insert(new Reminder(note6Id, place25Id, null, false));
                reminderDao.insert(new Reminder(note7Id, place26Id, null, false));
            });
        }
    };
}
