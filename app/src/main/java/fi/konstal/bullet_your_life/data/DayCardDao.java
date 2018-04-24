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

import fi.konstal.bullet_your_life.data.converter.CardItemConverter;
import fi.konstal.bullet_your_life.data.converter.DateConverter;

@Dao
@TypeConverters({DateConverter.class, CardItemConverter.class})

public interface DayCardDao {
    /**
     * Get All DayCards
     *
     * @return all dayCards
     */
    @Query("SELECT * FROM DayCard")
    LiveData<List<DayCard>> getAll();

    /**
     * Get all DateCard that match the gicen dateString
     *
     * @param dateString the dateString
     * @return the daycards
     */
    @Query("SELECT * FROM DayCard WHERE datestring == :dateString")
    LiveData<List<DayCard>> getAll(String dateString);

    /**
     * Get a single DayCard by ID
     *
     * @param id the ID
     * @return the dayCard
     */
    @Query("SELECT * FROM DayCard WHERE id == :id")
    LiveData<DayCard> getById(int id);

    /**
     * Gets a Single DayCard by dateString
     *
     * @param dateString the dateString
     * @return the DayCard
     */
    @Query("SELECT * FROM DayCard WHERE datestring == :dateString")
    LiveData<DayCard> getByDateString(String dateString);

    /**
     * Select all DayCards in the next seven days
     *
     * @return the DayCards
     */
    @Query("SELECT * FROM DayCard WHERE date >= (SELECT DATETIME('now', '+7 day')) ORDER BY date ASC")
    LiveData<List<DayCard>> getNextWeek();

    /**
     * Gets the size of all dayCards
     *
     * @return
     */
    @Query("SELECT COUNT(*) FROM DayCard")
    int getSize();

    /**
     * Insert Given DayCards
     *
     * @param dayCards the DayCards
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertDayCards(DayCard... dayCards);

    /**
     * Update Given DayCards
     *
     * @param dayCards the DayCards
     */
    @Update
    void updateDayCards(DayCard... dayCards);

    /**
     * Deletes given dayCards
     *
     * @param dayCards the given dayCards
     */
    @Delete
    void deleteDayCards(DayCard... dayCards);
}