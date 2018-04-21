package fi.konstal.bullet_your_life.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {DayCard.class, NoteCard.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public abstract DayCardDao getDayCardDao();
    public abstract NoteCardDao getNoteCardDao();
}
