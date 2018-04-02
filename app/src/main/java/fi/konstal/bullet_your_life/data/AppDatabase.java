package fi.konstal.bullet_your_life.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {DayCard.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase instance;

    public static AppDatabase getAppDatabase(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "country-database")
                    // allow queries on the main thread.
                    // Don't do this on a real app! See PersistenceBasicSample for an example.
                    // Temporary testing
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }

    public static void destroyInstance() {
        instance = null;
    }

    public abstract DayCardDao dayCardDao();
}