package fi.konstal.bullet_your_life.view_models;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import fi.konstal.bullet_your_life.data.CardRepository;
import fi.konstal.bullet_your_life.data.DayCard;

/**
 * The ViewModel that handles the data of the weekly view
 *
 * @author Konsta Lehtinen
 * @version 1.0
 * @see fi.konstal.bullet_your_life.fragment.WeeklyLogFragment
 * @since 1.0
 */
public class WeeklyLogViewModel extends ViewModel {
    private LiveData<List<DayCard>> dayCards;
    private CardRepository cardRepository;

    /**
     * No Args constructor for {@link ViewModel}
     */
    public WeeklyLogViewModel() {
    }

    /**
     * Initialize this livedata holder
     *
     * @param cardRepository LiveData source
     */
    public void init(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
        if (this.dayCards == null) {
            dayCards = cardRepository.getDayCardListNextWeek();
        }
    }

    /**
     * Returns the LiveData of weekly DayCards
     *
     * @return Next weeks DayCards
     */
    public LiveData<List<DayCard>> getDayCards() {
        return dayCards;
    }
}
