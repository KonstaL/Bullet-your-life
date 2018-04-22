package fi.konstal.bullet_your_life.view_models;

/**
 * Created by e4klehti on 22.4.2018.
 */
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import fi.konstal.bullet_your_life.data.CardRepository;
import fi.konstal.bullet_your_life.data.DayCard;


public class MonthlyLogViewModel extends ViewModel {
    private LiveData<List<DayCard>> dayCards;
    private MutableLiveData<String> dateString;

    private CardRepository cardRepository;

    public MonthlyLogViewModel() {
        this.dateString = new MutableLiveData<>();
    }

    //TODO: get cards by matching google id?
    public void init(CardRepository cardRepository, String dateString) {
        this.cardRepository = cardRepository;
        if (this.dayCards == null) {
            updateDate(dateString);
            dayCards = Transformations.switchMap(this.dateString, cardRepository::getDayCardList);
        }
    }

    public void updateDate(String dateString) {
        this.dateString.postValue(dateString);
    }

    public LiveData<List<DayCard>> getDayCards() {
        return dayCards;
    }
}
