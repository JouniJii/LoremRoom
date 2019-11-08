package com.example.loremroom;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {LoremEntity.class}, version = 1)
public abstract class LoremDatabase extends RoomDatabase {

    private static LoremDatabase INSTANCE;
    public static final String NAME = "TIETOKANTA";
    public abstract LoremDao loremDao();

    public static LoremDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            //INSTANCE = Room.databaseBuilder(context, LoremDatabase.class, NAME).allowMainThreadQueries().build();
            INSTANCE = Room.databaseBuilder(context, LoremDatabase.class, NAME).build();
        }
        return INSTANCE;
    }

}
