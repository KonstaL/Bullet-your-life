package fi.konstal.bullet_your_life.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import fi.konstal.bullet_your_life.Helper;
import fi.konstal.bullet_your_life.recycler_view.CardViewAdapter;
import fi.konstal.bullet_your_life.recycler_view.DayCard;
import fi.konstal.bullet_your_life.R;
import fi.konstal.bullet_your_life.recycler_view.RecyclerItemClickListener;
import fi.konstal.bullet_your_life.recycler_view.RecyclerViewClickListener;
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



        recyclerView =  findViewById(R.id.recycler_view);
        RecyclerViewClickListener listener = (view, position) -> {
            Toast.makeText(this, "Click on " + position, Toast.LENGTH_SHORT).show();
            Log.d("shit", "click tapahtu");
        };

        cardAdapter= new CardViewAdapter(this, listener,cardList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(cardAdapter);


        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, recyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {

                        DayCard myTest = cardList.get(position);

                        Intent intent = new Intent(getApplicationContext() , EditCardActivity.class);
                        intent.putExtra("dayCard", myTest);

                        startActivity(intent);
                    }

                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );

        prepareCardData();
    }

    private void prepareCardData() {
        Date dt = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(dt);

        // add 7 days of cards and examples
        for(int i = 0; i < 7; i++) {
            dt = c.getTime();
            cardList.add(new DayCard(Helper.weekdayString(this, dt), dt,  new Task(getString(R.string.example_task), R.drawable.ic_task_12dp),
                            new Task(getString(R.string.example_event), R.drawable.ic_hollow_circle_16dp)));

            //move to the next day
            c.add(Calendar.DATE, 1);
        }

        cardAdapter.notifyDataSetChanged();
    }
}
