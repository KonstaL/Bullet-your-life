package fi.konstal.bullet_your_life.view_models;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import javax.inject.Inject;

import fi.konstal.bullet_your_life.data.CardRepository;
import fi.konstal.bullet_your_life.data.DayCard;

/**
 * Created by e4klehti on 2.4.2018.
 */

public class WeeklyLogViewModel extends ViewModel {
    private LiveData<List<DayCard>> dayCards;
    private CardRepository cardRepository;

    public WeeklyLogViewModel() {}

    //TODO: get cards by matching google id?
    public void init(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
        if (this.dayCards == null) {
            dayCards = cardRepository.getDayCardListNextWeek();
        }
    }

  public LiveData<List<DayCard>> getDayCards() {
      return dayCards;
  }
}
