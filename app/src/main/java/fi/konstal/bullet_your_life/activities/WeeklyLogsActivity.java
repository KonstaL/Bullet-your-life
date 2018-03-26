package fi.konstal.bullet_your_life.activities;

import android.content.Intent;
import android.net.Uri;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import fi.konstal.bullet_your_life.Helper;
import fi.konstal.bullet_your_life.ViewPagerAdapter;
import fi.konstal.bullet_your_life.fragment.FragmentInterface;
import fi.konstal.bullet_your_life.fragment.FutureLog;
import fi.konstal.bullet_your_life.fragment.MonthlyLog;
import fi.konstal.bullet_your_life.fragment.WeeklyLog;
import fi.konstal.bullet_your_life.recycler_view.CardViewAdapter;
import fi.konstal.bullet_your_life.recycler_view.DayCard;
import fi.konstal.bullet_your_life.R;
import fi.konstal.bullet_your_life.recycler_view.RecyclerItemClickListener;
import fi.konstal.bullet_your_life.recycler_view.RecyclerViewClickListener;
import fi.konstal.bullet_your_life.task.Task;

public class WeeklyLogsActivity extends AppCompatActivity implements FragmentInterface {
    private List<DayCard> cardList= new ArrayList<>();
    private RecyclerView recyclerView;
    private CardViewAdapter cardAdapter;

    private FutureLog futureLogFragment;
    private WeeklyLog weeklyLogFragment;
    private MonthlyLog monthlyLogFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


       /* getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getSupportActionBar().hide();*/
        setContentView(R.layout.activity_weekly_logs);



        ViewPager pager = findViewById(R.id.viewpager);
        setupViewPager(pager);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.collapsing_toolbar_items, menu);
        Log.d("debug", "oncreatemenu");
        return true;
    }



    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        weeklyLogFragment = new WeeklyLog();
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
}
