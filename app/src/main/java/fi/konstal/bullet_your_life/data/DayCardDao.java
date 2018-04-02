package fi.konstal.bullet_your_life.data;



import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.TypeConverters;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
@TypeConverters({DateConverter.class, DataConverter.class})

public interface DayCardDao {
    @Query("SELECT * FROM DayCard")
    List<DayCard> getAll();
    //test this later
    //LiveData<List<DayCard>> getAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertDayCards(DayCard... dayCards);

    @Update
    void updateDayCards(DayCard... dayCards);

    @Delete
    void deleteDayCards(DayCard... dayCards);
}