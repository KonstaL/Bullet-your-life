package fi.konstal.bullet_your_life.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.view.Menu;

import com.google.android.gms.drive.DriveClient;

import butterknife.ButterKnife;
import fi.konstal.bullet_your_life.R;
import fi.konstal.bullet_your_life.ViewPagerAdapter;
import fi.konstal.bullet_your_life.dagger.component.DaggerAppComponent;
import fi.konstal.bullet_your_life.dagger.module.AppModule;
import fi.konstal.bullet_your_life.dagger.module.RoomModule;
import fi.konstal.bullet_your_life.fragment.MonthlyLogFragment;
import fi.konstal.bullet_your_life.fragment.NotesFragment;
import fi.konstal.bullet_your_life.fragment.WeeklyLogFragment;

/**
 * Activity that displays cards in different fragments
 *
 * @author Konsta Lehtinen
 * @version 1.0
 * @see WeeklyLogFragment
 * @see MonthlyLogFragment
 * @see NotesFragment
 * @since 1.0
 */
public class LogsActivity extends BaseActivity {
    private DriveClient driveClient;

    private NotesFragment notesFragmentFragment;
    private WeeklyLogFragment weeklyLogFragmentFragment;
    private MonthlyLogFragment monthlyLogFragmentFragment;


    /**
     * {@inheritDoc}
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logs);

        SharedPreferences prefs = getSharedPreferences("bullet_your_life", Context.MODE_PRIVATE);
        // If the the app is has not been started before
        if (!prefs.getBoolean("init_done", false)) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }

        //initialize Dagger2
        DaggerAppComponent.builder()
                .appModule(new AppModule(getApplication()))
                .roomModule(new RoomModule(getApplication()))
                .build()
                .inject(this);

        // Initialize ButterKnife
        ButterKnife.bind(this);

        ViewPager pager = findViewById(R.id.viewpager);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener((item) -> {
            switch (item.getItemId()) {
                case R.id.menu_weekly:
                    pager.setCurrentItem(0);
                    break;
                case R.id.menu_monthly:
                    pager.setCurrentItem(1);
                    break;
                case R.id.menu_notes:
                    pager.setCurrentItem(3);
            }
            return true;
        });
        setupViewPager(pager, navigation);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.collapsing_toolbar_items, menu);
        return true;
    }

    /**
     * Setup the activitys {@link ViewPager}
     *
     * @param viewPager  viewpages to be setup
     * @param navigation {@link BottomNavigationView} to link to
     */
    private void setupViewPager(ViewPager viewPager, BottomNavigationView navigation) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        weeklyLogFragmentFragment = WeeklyLogFragment.newInstance();
        monthlyLogFragmentFragment = new MonthlyLogFragment();
        notesFragmentFragment = new NotesFragment();
        adapter.addFragment(weeklyLogFragmentFragment);
        adapter.addFragment(monthlyLogFragmentFragment);
        adapter.addFragment(notesFragmentFragment);

        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {
            }

            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            public void onPageSelected(int position) {
                navigation.getMenu().getItem(position).setChecked(true);
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onDriveClientReady() {
        this.driveClient = super.getDriveClient();
    }
}
