package se.miun.kran1800.dt031g.bathingsites;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

// Bathing site database instance
@Database(entities = {BathingSite.class}, version = 1, exportSchema = false)
public abstract class BathingSiteDatabase extends RoomDatabase {

    private static BathingSiteDatabase databaseInstance; // Instance
    public abstract BathingSiteDao bathingSiteDao(); // Dao

    // Get the instance of database
    static BathingSiteDatabase getInstance(Context context) {
        if(databaseInstance == null) {
            databaseInstance = Room.databaseBuilder
                    (context, BathingSiteDatabase.class, "bathing_site_database").build();
        }
        return databaseInstance;
    }
}
