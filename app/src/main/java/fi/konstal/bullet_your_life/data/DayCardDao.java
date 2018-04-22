package fi.konstal.bullet_your_life.data;



import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.TypeConverters;
import android.arch.persistence.room.Update;

import java.util.Date;
import java.util.List;

@Dao
@TypeConverters({DateConverter.class, CardItemConverter.class})

public interface DayCardDao {
    @Query("SELECT * FROM DayCard")
    LiveData<List<DayCard>> getAll();

    @Query("SELECT * FROM DayCard WHERE datestring == :dateString")
    LiveData<List<DayCard>> getAll(String dateString);

    @Query("SELECT * FROM DayCard WHERE id == :id")
    LiveData<DayCard> getById(int id);

    @Query("SELECT * FROM DayCard WHERE datestring == :dateString")
    LiveData<DayCard> getByDateString(String dateString);


    @Query("SELECT COUNT(*) FROM DayCard")
    int getSize();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertDayCards(DayCard... dayCards);

    @Update
    void updateDayCards(DayCard... dayCards);

    @Delete
    void deleteDayCards(DayCard... dayCards);
}