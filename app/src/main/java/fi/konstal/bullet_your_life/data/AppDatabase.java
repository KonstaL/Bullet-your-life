package fi.konstal.bullet_your_life.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

/**
 * Room database
 *
 * @author Konsta Lehtinen
 * @version 1.0
 * @since 1.0
 */
@Database(entities = {DayCard.class, NoteCard.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    /**
     * Returns implemented DayCardDao
     *
     * @return implemented DAO
     */
    public abstract DayCardDao getDayCardDao();

    /**
     * Returns implemented NoteCardDao
     *
     * @return implemented DAO
     */
    public abstract NoteCardDao getNoteCardDao();
}
