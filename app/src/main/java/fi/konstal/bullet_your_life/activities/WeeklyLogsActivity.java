package fi.konstal.bullet_your_life.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Window;

import java.util.ArrayList;
import java.util.List;

import fi.konstal.bullet_your_life.recycler_view.CardViewAdapter;
import fi.konstal.bullet_your_life.recycler_view.DayCard;
import fi.konstal.bullet_your_life.R;
import fi.konstal.bullet_your_life.task.Task;

public class WeeklyLogsActivity extends AppCompatActivity {
    private List<DayCard> cardList= new ArrayList<>();
    private RecyclerView recyclerView;
    private CardViewAdapter cardAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_weekly_logs);



        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        cardAdapter= new CardViewAdapter(this, cardList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(cardAdapter);

        prepareCardData();
    }

    private void prepareCardData() {

        DayCard card = new DayCard("Maanantai", new Task("putsaa auto", R.drawable.ic_add_circle_black_24dp), new Task("moi", R.drawable.ic_mode_edit_black_24dp));
        cardList.add(card);

        DayCard card2 = new DayCard("Tiistai", new Task("putsaa auto", R.drawable.ic_add_circle_black_24dp), new Task("moi", R.drawable.ic_mode_edit_black_24dp));
        cardList.add(card2);

        card = new DayCard("Keskiviikko", new Task("putsaa auto", R.drawable.ic_add_circle_black_24dp), new Task("moi", R.drawable.ic_mode_edit_black_24dp));
        cardList.add(card);

        card = new DayCard("Torstai", new Task("putsaa auto", R.drawable.ic_add_circle_black_24dp), new Task("moi", R.drawable.ic_mode_edit_black_24dp));
        cardList.add(card);

        card = new DayCard("Perjantai", new Task("putsaa auto", R.drawable.ic_add_circle_black_24dp), new Task("moi", R.drawable.ic_mode_edit_black_24dp));
        cardList.add(card);

        card = new DayCard("Lauantain", new Task("putsaa auto", R.drawable.ic_add_circle_black_24dp), new Task("moi", R.drawable.ic_mode_edit_black_24dp));
        cardList.add(card);

        card = new DayCard("Sunnuntai", new Task("putsaa auto", R.drawable.ic_add_circle_black_24dp), new Task("moi", R.drawable.ic_mode_edit_black_24dp));
        cardList.add(card);



        cardAdapter.notifyDataSetChanged();
    }
}
