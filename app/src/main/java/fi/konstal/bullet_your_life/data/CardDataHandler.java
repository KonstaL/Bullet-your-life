package fi.konstal.bullet_your_life.data;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;

import java.util.ArrayList;

import fi.konstal.bullet_your_life.recycler_view.DayCard;

public class CardDataHandler {

    public static final int DATA_GET = 200;
    public static final int DATA_INSERT = 201;
    public static final int DATA_DELETE = 204;
    public static final int DATA_UPDATE = 204;

    private AppDatabase db;
    private ArrayList<DayCard> cards;
    private Context context;
    private LocalBroadcastManager lBroadcast;
    private DayCard[] currentDayCards;

    public CardDataHandler(Context context) {
        this.context = context;
        db = Room.databaseBuilder(context, AppDatabase.class, "database-daycards").build();
        cards = new ArrayList<>();
        lBroadcast = LocalBroadcastManager.getInstance(context);
        getNewData();
    }

    public ArrayList<DayCard> getDayCardList() {
        return cards;
    }

    public void insertNewData(DayCard... card) {
        currentDayCards = card;
        new AsyncTaskHandler().execute("insert");

        //TODO: add straight to cards instead of getNewData()
        // refreshes cards arraylist
        getNewData();
    }

    public void removeData(DayCard... cards) {
        currentDayCards = cards;
        new AsyncTaskHandler().execute("delete");
        getNewData();
    }

    public void getNewData() {
        new AsyncTaskHandler().execute("get");
    }

    private class AsyncTaskHandler extends AsyncTask<String, Integer, Integer> {

        @Override
        protected Integer doInBackground(String... actions) {
            int actionCode = 0;
            for(int i = 0; i < actions.length; i++) {
                switch(actions[i]) {
                    case "get":
                        cards.clear();
                        cards.addAll(db.dayCardDao().getAll());
                        actionCode = DATA_GET;
                        break;

                    case "insert":
                        db.dayCardDao().insertDayCards(currentDayCards);
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
            lBroadcast.sendBroadcast(i);
        }
    }
}
