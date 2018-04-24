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

/**
 * Created by e4klehti on 21.4.2018.
 */


@Dao
@TypeConverters({DateConverter.class, CardItemConverter.class})
public interface NoteCardDao {

    /**
     * Gets all notecars
     * @return livedate of list
     */
    @Query("SELECT * FROM NoteCard")
    LiveData<List<NoteCard>> getAll();

    /**
     * Gets all noteCards by ID
     * @param id the ID
     * @return LiveData of the notecards
     */
    @Query("SELECT * FROM NoteCard WHERE id == :id")
    LiveData<NoteCard> getById(int id);

    /**
     * Get the size of this Table
     * @return the size
     */
    @Query("SELECT COUNT(*) FROM NoteCard")
    int getSize();

    /**
     * Insert NoteCards
     * @param noteCards the notecards
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertNoteCards(NoteCard... noteCards);

    /**
     * update noteCards
     * @param noteCards the noteCards
     */
    @Update
    void updateNoteCards(NoteCard... noteCards);

    /**
     * Delete the noteCards
     * @param noteCards the notecards
     */
    @Delete
    void deleteNoteCards(NoteCard... noteCards);
}
