package fi.konstal.bullet_your_life;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import fi.konstal.bullet_your_life.fragment.WeeklyLog;

/**
 * Created by e4klehti on 23.3.2018.
 */

public class ViewPagerAdapter extends FragmentPagerAdapter {
    private static int NUM_ITEMS = 3;

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch(position) {
            case 0: return WeeklyLog.newInstance("frament 1", "");

        }


        return null;
    }

    @Override
    public int getCount() {
        return NUM_ITEMS;
    }
}
