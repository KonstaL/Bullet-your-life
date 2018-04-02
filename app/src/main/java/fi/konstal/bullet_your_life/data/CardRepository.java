package fi.konstal.bullet_your_life.data;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.Arrays;

public class CardRepository {

    public static final int DATA_GET = 200;
    public static final int DATA_INSERT = 201;
    public static final int DATA_DELETE = 204;
    public static final int DATA_UPDATE = 204;

    private static CardRepository instance;

    private AppDatabase db;
    private ArrayList<DayCard> dayCards;
    private Context context;  //private LocalBroadcastManager lBroadcast;


    private CardRepository() {
        db = Room.databaseBuilder(context, AppDatabase.class, "database-daycards").build();
        dayCards = new ArrayList<>();
        //lBroadcast = LocalBroadcastManager.getInstance(context);
        getNewData();
    }

    public synchronized static CardRepository getInstance(Context context) {
        if (instance == null) {
            instance = new CardRepository(context);
        }
        return instance;
    }


    public ArrayList<DayCard> getDayCardList() {
        return dayCards;
    }

    public void insertDayCards(DayCard... cards) {
        dayCards.addAll(Arrays.asList(cards));
        new AsyncDatabaseHandler(db, cards).execute("insert");
    }

    public void removeData(DayCard... cards) {
        currentDayCards = cards;
        new AsyncTaskHandler().execute("delete");
        getNewData();
    }

    public void getNewData() {
        new AsyncDatabaseHandler(db).execute("get");
    }

    private static class AsyncDatabaseHandler extends AsyncTask<String, Integer, Integer> {

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
    }
}
