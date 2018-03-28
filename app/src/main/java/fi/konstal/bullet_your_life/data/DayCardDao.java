package fi.konstal.bullet_your_life.data;



import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import fi.konstal.bullet_your_life.recycler_view.DayCard;

@Dao
public interface DayCardDao {
    @Query("SELECT * FROM DayCard")
    List<DayCard> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertDayCards(DayCard... dayCards);

    @Update
    public void updateDayCards(DayCard... dayCards);

    @Delete
    public void deleteDayCards(DayCard... dayCards);
}