package fi.konstal.bullet_your_life.data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.TypeConverters;
import android.arch.persistence.room.Update;

import java.util.List;

/**
 * Created by e4klehti on 21.4.2018.
 */


@Dao
@TypeConverters({DateConverter.class, CardItemConverter.class})
public interface NoteCardDao {


    @Query("SELECT * FROM NoteCard")
    LiveData<List<DayCard>> getAll();

    @Query("SELECT * FROM NoteCard WHERE id == :id")
    LiveData<DayCard> getById(int id);

    @Query("SELECT COUNT(*) FROM NoteCard")
    int getSize();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertDayCards(NoteCard... noteCards);

    @Update
    void updateDayCards(NoteCard... noteCards);

    @Delete
    void deleteDayCards(NoteCard... noteCards);
}
