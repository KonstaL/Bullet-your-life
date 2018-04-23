package fi.konstal.bullet_your_life.data;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executor;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

/**
 * Acts as a gateway for all application data
 *
 * @author Konsta Lehtinen
 * @version 1.0
 * @since 1.0
 */
@Singleton
public class CardRepository {

    private NoteCardDao noteCardDao;
    private DayCardDao dayCardDao;
    private Executor executor;


    /**
     * Constructs the CardRepository singleton
     *
     * @param noteCardDao DAO for {@link NoteCard}
     * @param dayCardDao DAO for {@link DayCard}
     * @param executor for multithread exexution
     */
    @Inject
    public CardRepository(NoteCardDao noteCardDao,
                          DayCardDao dayCardDao,
                           Executor executor) {
        this.noteCardDao = noteCardDao;
       this.dayCardDao = dayCardDao;
       this.executor = executor;
    }

    /**
     * Gets all DayCards
     *
     * @return LiveData List of all DayCards
     */
    public synchronized LiveData<List<DayCard>> getDayCardList() {
            return dayCardDao.getAll();
    }

    /**
     * Returns next weeks DayCards
     * @return DayCards for the next week
     */
    public synchronized LiveData<List<DayCard>> getDayCardListNextWeek() {
        return dayCardDao.getNextWeek();
    }

    public synchronized LiveData<List<DayCard>> getDayCardList(String dateString) {
        return dayCardDao.getAll(dateString);
    }

    public synchronized LiveData<DayCard> getDayCard(int id) {
        return dayCardDao.getById(id);
    }

    public LiveData<DayCard> getDayCard(String dateString) {
        return dayCardDao.getByDateString(dateString);
    }


    public synchronized void insertDayCards(DayCard... cards) {
       executor.execute(() -> {
           dayCardDao.insertDayCards(cards);
       });
    }

    public synchronized void updateCard(Card card) {
        if(card instanceof DayCard) {
            executor.execute(()-> dayCardDao.updateDayCards((DayCard) card));
        } else if(card instanceof NoteCard) {
            executor.execute(()-> noteCardDao.updateNoteCards((NoteCard)card));
        } else {
            throw new RuntimeException("Card type has not been setup");
        }
    }

    public void removeCard(Card card) {
        if(card instanceof DayCard) {
            executor.execute(()-> dayCardDao.deleteDayCards((DayCard) card));
        } else if(card instanceof NoteCard) {
            executor.execute(()-> noteCardDao.deleteNoteCards((NoteCard)card));
        } else {
            throw new RuntimeException("Card type has not been setup");
        }
    }

    public int getSize() {
        return dayCardDao.getSize();
    }

    public LiveData<List<NoteCard>> getNoteCards() {
        return noteCardDao.getAll();
    }

    public LiveData<NoteCard> getNoteCard(int id) {
        return noteCardDao.getById(id);
    }

    public void addNoteCards(NoteCard... noteCards) {
        executor.execute(()-> noteCardDao.insertNoteCards(noteCards));
    }

    public LiveData getGeneric(int id, int type) {
        if(type == Card.CARD_TYPE_DAYCARD) {

            return dayCardDao.getById(id);
        } else if(type == Card.CARD_TYPE_NOTECARD) {
            return noteCardDao.getById(id);
        } else {
            throw new IllegalArgumentException("Illegal card type!");
        }
    }
}
