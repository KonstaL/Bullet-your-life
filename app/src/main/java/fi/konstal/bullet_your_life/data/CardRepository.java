package fi.konstal.bullet_your_life.data;

import android.arch.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.Executor;

import javax.inject.Inject;
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
     * @param dayCardDao  DAO for {@link DayCard}
     * @param executor    for multithread exexution
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
     *
     * @return DayCards for the next week
     */
    public synchronized LiveData<List<DayCard>> getDayCardListNextWeek() {
        return dayCardDao.getNextWeek();
    }

    /**
     * Gets  DayCards by a date
     *
     * @param dateString the date to look for
     * @return LiveData of found cards
     */
    public synchronized LiveData<List<DayCard>> getDayCardList(String dateString) {
        return dayCardDao.getAll(dateString);
    }

    /**
     * Gets a dayCard by ID
     *
     * @param id DayCard ID
     * @return LiveData of the DayCard
     */
    public synchronized LiveData<DayCard> getDayCard(int id) {
        return dayCardDao.getById(id);
    }

    /**
     * Get Single DayCard by Date
     *
     * @param dateString the date which to search bu
     * @return LiveData of the found DayCard
     */
    public LiveData<DayCard> getDayCard(String dateString) {
        return dayCardDao.getByDateString(dateString);
    }

    /**
     * Inserts DayCards to DB in a seperate Thread
     *
     * @param cards DayCards to insert
     */
    public synchronized void insertDayCards(DayCard... cards) {
        executor.execute(() -> {
            dayCardDao.insertDayCards(cards);
        });
    }

    /**
     * Updates given Cards that implement {@link Card} interface
     *
     * @param card card to remove
     */
    public synchronized void updateCard(Card card) {
        if (card instanceof DayCard) {
            executor.execute(() -> dayCardDao.updateDayCards((DayCard) card));
        } else if (card instanceof NoteCard) {
            executor.execute(() -> noteCardDao.updateNoteCards((NoteCard) card));
        } else {
            throw new RuntimeException("Card type has not been setup");
        }
    }

    /**
     * Removes the Given card
     *
     * @param card card to remove
     */
    public void removeCard(Card card) {
        if (card instanceof DayCard) {
            executor.execute(() -> dayCardDao.deleteDayCards((DayCard) card));
        } else if (card instanceof NoteCard) {
            executor.execute(() -> noteCardDao.deleteNoteCards((NoteCard) card));
        } else {
            throw new RuntimeException("Card type has not been setup");
        }
    }

    /**
     * Gets all {@link NoteCard} in DB
     *
     * @return LiveData of the NoteCards
     */
    public LiveData<List<NoteCard>> getNoteCards() {
        return noteCardDao.getAll();
    }

    /**
     * Gets a NoteCard by ID
     *
     * @param id the ID
     * @return LiveData of the NoteCard
     */
    public LiveData<NoteCard> getNoteCard(int id) {
        return noteCardDao.getById(id);
    }

    /**
     * Insert NoteCards to DB
     *
     * @param noteCards the cards to be added
     */
    public void insertNoteCards(NoteCard... noteCards) {
        executor.execute(() -> noteCardDao.insertNoteCards(noteCards));
    }

    /**
     * Get a Card by knowing the type and it's ID
     *
     * @param id   the ID
     * @param type The type of the card
     * @return LiveData of the found card
     * @see Card#CARD_TYPE_DAYCARD
     * @see Card#CARD_TYPE_NOTECARD
     */
    public LiveData getGeneric(int id, int type) {
        if (type == Card.CARD_TYPE_DAYCARD) {
            return dayCardDao.getById(id);
        } else if (type == Card.CARD_TYPE_NOTECARD) {
            return noteCardDao.getById(id);
        } else {
            throw new IllegalArgumentException("Illegal card type!");
        }
    }
}
