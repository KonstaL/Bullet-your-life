package fi.konstal.bullet_your_life.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import fi.konstal.bullet_your_life.recycler_view.DayCard;

@Database(entities = {DayCard.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
        public abstract DayCardDao dayCardDao();
}