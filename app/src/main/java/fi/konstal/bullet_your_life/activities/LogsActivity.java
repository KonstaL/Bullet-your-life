package fi.konstal.bullet_your_life.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

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
import fi.konstal.bullet_your_life.recycler_view.DayCard;
import fi.konstal.bullet_your_life.R;
import fi.konstal.bullet_your_life.task.Task;

public class LogsActivity extends BaseActivity implements FragmentInterface, EditCardInterface  {

    private CardDataHandler cardDataHandler;
    private List<DayCard> cardList;
    private RecyclerView recyclerView;
    private Boolean isAuthenticated;



    private FutureLog futureLogFragment;
    private WeeklyLog weeklyLogFragment;
    private MonthlyLog monthlyLogFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       /* getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getSupportActionBar().hide();*/
        setContentView(R.layout.activity_weekly_logs);



        SharedPreferences prefs = getSharedPreferences("bullet_your_life", Context.MODE_PRIVATE);
        // If the the app is has not been started before
        if(!prefs.getBoolean("init_done", false)) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }

        isAuthenticated = prefs.getBoolean("is_auth", false);
        Toast.makeText(this, "is auth: " + isAuthenticated , Toast.LENGTH_SHORT).show();


        cardDataHandler = new CardDataHandler(this);
        cardList = cardDataHandler.getDayCardList();

        ViewPager pager = findViewById(R.id.viewpager);
        setupViewPager(pager);

        prepareCardData();
    }

    @Override
    public void onStart() {
        super.onStart();
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

    @Override
    protected void onDriveClientReady() {
        Toast.makeText(this, "logs on drive ready", Toast.LENGTH_SHORT).show();
    }

 /*   public void tempTest(View v) {
        DriveClient mDriveClient = Drive.getDriveClient(getApplicationContext(), gsa);
        // Build a drive resource client.
        DriveResourceClient mDriveResourceClient =
                Drive.getDriveResourceClient(getApplicationContext(), gsa);
        // Start camera.
        startActivityForResult(
                new Intent(MediaStore.ACTION_IMAGE_CAPTURE), REQUEST_CODE_CAPTURE_IMAGE);

    }
*/


}
