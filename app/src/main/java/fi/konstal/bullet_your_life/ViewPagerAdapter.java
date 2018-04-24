package fi.konstal.bullet_your_life;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import fi.konstal.bullet_your_life.fragment.MonthlyLogFragment;
import fi.konstal.bullet_your_life.fragment.NotesFragment;
import fi.konstal.bullet_your_life.fragment.WeeklyLogFragment;

/**
 * This adapter holds all the Fragments and manages their movement
 *
 * @author Konsta Lehtinen
 * @version 1.0
 * @see WeeklyLogFragment
 * @see MonthlyLogFragment
 * @see NotesFragment
 * @since 1.0
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragments;


    /**
     * The constructor
     * @param fm The FragmentMangager
     */
    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
        fragments = new ArrayList<>();
    }

    /**
     * The Constructor
     * @param fm FragmentManager
     * @param fragments List of Fragments
     */
    public ViewPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    /**
     * Add a fragment to this Adapter
     * @param fr
     */
    public void addFragment(Fragment fr) {
        fragments.add(fr);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getCount() {
        return fragments.size();
    }
}
