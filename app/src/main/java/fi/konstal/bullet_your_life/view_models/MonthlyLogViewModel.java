package fi.konstal.bullet_your_life.view_models;


import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import fi.konstal.bullet_your_life.data.CardRepository;
import fi.konstal.bullet_your_life.data.DayCard;
import fi.konstal.bullet_your_life.fragment.MonthlyLogFragment;

/**
 * This {@link ViewModel} holds data for the currently selected {@link java.util.Date}
 *
 * @author Konsta Lehtinen
 * @version 1.0
 * @see MonthlyLogFragment
 * @since 1.0
 */
public class MonthlyLogViewModel extends ViewModel {
    private LiveData<List<DayCard>> dayCards;
    private MutableLiveData<String> dateString;

    /**
     * No-args constructor for {@link ViewModel}
     */
    public MonthlyLogViewModel() {
        this.dateString = new MutableLiveData<>();
    }

    /**
     * Initializes the Viewmodel by giving it a LiveData source and dateString by which to find Cards
     *
     * @param cardRepository The LiveData source
     * @param dateString     DateString by which to search cards
     */
    public void init(CardRepository cardRepository, String dateString) {
        if (this.dayCards == null) {
            updateDate(dateString);
            dayCards = Transformations.switchMap(this.dateString, cardRepository::getDayCardList);
        }
    }

    /**
     * Updates the currently viewed day by updating the LiveData source
     *
     * @param dateString new Date to view
     */
    public void updateDate(String dateString) {
        this.dateString.postValue(dateString);
    }

    /**
     * Get all DayCards that have been found with the current DateString
     *
     * @return all found DayCards
     */
    public LiveData<List<DayCard>> getDayCards() {
        return dayCards;
    }
}
