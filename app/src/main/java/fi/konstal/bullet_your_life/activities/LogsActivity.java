package fi.konstal.bullet_your_life.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.drive.DriveClient;

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
import fi.konstal.bullet_your_life.task.CardTask;

public class LogsActivity extends BaseActivity implements FragmentInterface, EditCardInterface  {

    private Boolean isAuthenticated;
    private DriveClient driveClient;

    private List<DayCard> cardList;

    private FutureLog futureLogFragment;
    private WeeklyLog weeklyLogFragment;
    private MonthlyLog monthlyLogFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weekly_logs);

        SharedPreferences prefs = getSharedPreferences("bullet_your_life", Context.MODE_PRIVATE);
        // If the the app is has not been started before
        if(!prefs.getBoolean("init_done", false)) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }

        isAuthenticated = prefs.getBoolean("is_auth", false);
        Toast.makeText(this, "is auth: " + isAuthenticated , Toast.LENGTH_SHORT).show();


        ViewPager pager = findViewById(R.id.viewpager);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener((item)-> {
            switch (item.getItemId()) {
                case R.id.menu_weekly:
                    pager.setCurrentItem(0);
                    break;
                case R.id.menu_monthly:
                    pager.setCurrentItem(1);
                    break;
                case R.id.menu_future:
                    pager.setCurrentItem(3);
            }
            return true;
        });

        setupViewPager(pager, navigation);


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

    private void setupViewPager(ViewPager viewPager, BottomNavigationView navigation) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        weeklyLogFragment = WeeklyLog.newInstance();
        monthlyLogFragment = new MonthlyLog();
        futureLogFragment = new FutureLog();
        adapter.addFragment(weeklyLogFragment);
        adapter.addFragment(monthlyLogFragment);
        adapter.addFragment(futureLogFragment);

        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {}
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}
            public void onPageSelected(int position) {
                navigation.getMenu().getItem(position).setChecked(true);
            }
        });
    }


    @Override
    public void onCardClicked(DayCard card) {
        Log.d("cardClick", card.toString());
    }



    @Override
    public void addTaskToCard(int cardIndex, CardTask... cardTask) {
        cardList.get(cardIndex).getCardItems().addAll(Arrays.asList(cardTask));
    }

    @Override
    protected void onDriveClientReady() {
        this.driveClient = super.getDriveClient();
    }


    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_popup, popup.getMenu());
        popup.show();
        popup.setOnMenuItemClickListener((item -> {
            SharedPreferences preferences = getSharedPreferences("bullet_your_life", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();

            switch (item.getItemId()) {
                case R.id.popup_logout:
                    editor.putBoolean("init_done", false);
                    editor.commit();
                    break;
                case R.id.popup_login:

                    editor.putBoolean("init_done", false);
                    editor.putBoolean("is_auth", false);
                    editor.commit();
                    break;
            }

            return false;
        }));
    }

    public void setCardList(List<DayCard> dayCards) {
        this.cardList = dayCards;
    }


}
