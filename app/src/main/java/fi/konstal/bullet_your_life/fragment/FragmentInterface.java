package fi.konstal.bullet_your_life.fragment;

import android.net.Uri;

import fi.konstal.bullet_your_life.recycler_view.DayCard;

/**
 * Just a super simple temporary mvp interface to get fragments to work
 */

public interface FragmentInterface {

    void onCardClicked(DayCard card);

}
