package com.poc.android.myhospitals;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {Item.class}, version = 1)
public abstract class ItemRoomDatabase extends RoomDatabase {

    // Define the DAOs that work with the database
    public abstract ItemDao itemDao();

    private static ItemRoomDatabase INSTANCE;

    /**
     * Create the WordRoomDatabase as a singleton to prevent having multiple instances
     * of the database opened at the same time, which would be a bad thing
     */
    public static ItemRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (ItemRoomDatabase.class) {
                if (INSTANCE == null) {
                    // Create db
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            ItemRoomDatabase.class, "item_database")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
