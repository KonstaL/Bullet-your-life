package fi.konstal.bullet_your_life.activities;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import fi.konstal.bullet_your_life.Helper;
import fi.konstal.bullet_your_life.ViewPagerAdapter;
import fi.konstal.bullet_your_life.data.CardDataHandler;
import fi.konstal.bullet_your_life.fragment.EditCardInterface;
import fi.konstal.bullet_your_life.fragment.FragmentInterface;
import fi.konstal.bullet_your_life.fragment.FutureLog;
import fi.konstal.bullet_your_life.fragment.MonthlyLog;
import fi.konstal.bullet_your_life.fragment.WeeklyLog;
import fi.konstal.bullet_your_life.recycler_view.CardViewAdapter;
import fi.konstal.bullet_your_life.recycler_view.DayCard;
import fi.konstal.bullet_your_life.R;
import fi.konstal.bullet_your_life.task.Task;

public class LogsActivity extends AppCompatActivity implements FragmentInterface, EditCardInterface {
    private CardDataHandler cardDataHandler;
    private List<DayCard> cardList;
    private RecyclerView recyclerView;


    private FutureLog futureLogFragment;
    private WeeklyLog weeklyLogFragment;
    private MonthlyLog monthlyLogFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       /* getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getSupportActionBar().hide();*/
        setContentView(R.layout.activity_weekly_logs);

        cardDataHandler = new CardDataHandler(this);
        cardList = cardDataHandler.getDayCardList();

        ViewPager pager = findViewById(R.id.viewpager);
        setupViewPager(pager);

        prepareCardData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.collapsing_toolbar_items, menu);
        return true;
    }



    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        weeklyLogFragment = new WeeklyLog(cardDataHandler);
        monthlyLogFragment = new MonthlyLog();
        futureLogFragment = new FutureLog();
        adapter.addFragment(weeklyLogFragment);
        adapter.addFragment(monthlyLogFragment);
        adapter.addFragment(futureLogFragment);

        viewPager.setAdapter(adapter);
    }


    @Override
    public void onCardClicked(DayCard card) {
        Log.d("cardClick", card.toString());
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
    }


    @Override
    public void addTaskToCard(int cardIndex, Task... task) {
        cardList.get(cardIndex).getTasks().addAll(Arrays.asList(task));
    }
}
