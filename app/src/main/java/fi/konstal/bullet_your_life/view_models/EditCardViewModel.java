package fi.konstal.bullet_your_life.view_models;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import fi.konstal.bullet_your_life.data.CardRepository;
import fi.konstal.bullet_your_life.data.DayCard;
import fi.konstal.bullet_your_life.task.CardItem;

/**
 * Created by e4klehti on 14.4.2018.
 */

public class EditCardViewModel extends ViewModel {
    private LiveData<DayCard> dayCard;
    private CardRepository cardRepo;

    public EditCardViewModel() {
    }


    public void init(CardRepository cardRepo, int id) {
        if (dayCard == null) {
            this.cardRepo = cardRepo;
            this.dayCard = cardRepo.getDayCard(id);
        }
    }

    public LiveData<DayCard> getDayCard() {
        return dayCard;
    }

    public void addCardItems(CardItem... cardItems) {
        DayCard card = dayCard.getValue();
        if (card != null) {
            card.addCardItems(cardItems);
            cardRepo.updateCards(card);
        }
    }
}
