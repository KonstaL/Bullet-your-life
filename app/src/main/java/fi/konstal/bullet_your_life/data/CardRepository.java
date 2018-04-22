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

@Singleton
public class CardRepository {

    public static final int DATA_GET = 200;
    public static final int DATA_INSERT = 201;
    public static final int DATA_DELETE = 204;
    public static final int DATA_UPDATE = 204;

    private NoteCardDao noteCardDao;
    private DayCardDao dayCardDao;
    private Executor executor;


    @Inject
    //@Named("SingleThread")
    public CardRepository(NoteCardDao noteCardDao,
                          DayCardDao dayCardDao,
                           Executor executor) {
        this.noteCardDao = noteCardDao;
       this.dayCardDao = dayCardDao;
       this.executor = executor;
    }

    public synchronized LiveData<List<DayCard>> getDayCardList() {
            return dayCardDao.getAll();
    }
/*
    public synchronized LiveData<List<DayCard>> getDayCardSize() {
        return executor.execute(()-> dayCardDao.getSize());
    }*/


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



  /*  private static class AsyncDatabaseHandler extends AsyncTask<String, Integer, Integer> {

        //DayCards to use in the SQL queries
        DayCard[] dayCards;
        AppDatabase db;

        public AsyncDatabaseHandler(AppDatabase db, DayCard... dayCards) {
            this.dayCards = dayCards;
            this.db = db;
        }

        @Override
        protected Integer doInBackground(String... actions) {
            int actionCode = 0;
            for (int i = 0; i < actions.length; i++) {
                switch (actions[i]) {
                    case "get":
                        dayCards.clear();
                        dayCards.addAll(db.dayCardDao().getAll());
                        actionCode = DATA_GET;
                        break;

                    case "insert":
                        db.dayCardDao().insertDayCards(dayCards);
                        actionCode = DATA_INSERT;
                        break;

                    case "delete":
                        db.dayCardDao().deleteDayCards(currentDayCards);
                        actionCode = DATA_DELETE;
                        break;

                    case "update":
                        db.dayCardDao().updateDayCards(currentDayCards);
                        actionCode = DATA_UPDATE;
                        break;
                }
            }
            return actionCode;
        }

        @Override
        protected void onPostExecute(Integer code) {
            Intent i = new Intent("appointment_handler");
            i.putExtra("action", code);
            //lBroadcast.sendBroadcast(i);
        }
    }*/
}
