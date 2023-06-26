package com.example.sfcm.src.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {ContactPreference.class}, version = 1, exportSchema = false)
public abstract class ContactPreferenceDB extends RoomDatabase {
    private static ContactPreferenceDB db;
    private static String DATABASE_NAME = "pref.db";

    public abstract ContactPreferenceDao mainDao();

    public synchronized static ContactPreferenceDB getInstance(Context context) {
        if (db == null) {
            db = Room.databaseBuilder(context.getApplicationContext(), ContactPreferenceDB.class, DATABASE_NAME)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return db;
    }

    public static void destroyInstance() {
        db = null;
    }
}
