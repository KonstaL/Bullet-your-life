package fi.konstal.bullet_your_life.view_models;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import java.util.List;

import fi.konstal.bullet_your_life.data.CardRepository;
import fi.konstal.bullet_your_life.data.DayCard;
import fi.konstal.bullet_your_life.edit_recycler_view.CardItemHandler;
import fi.konstal.bullet_your_life.task.CardItem;

/**
 * Created by e4klehti on 14.4.2018.
 */

public class EditCardViewModel extends ViewModel implements CardItemHandler {
    private static final String TAG = "EditCardViewModel";
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

    /*    public void updateCardItems(CardItem... cardItems) {
            DayCard card = dayCard.getValue();
            if(card!= null) {
                List<CardItem> items = card.getCardItems();
                for (CardItem cardItem : cardItems) {
                    int i = items.indexOf(cardItem);
                    if(i != -1) {
                        items.remove(i);
                        items.add(i, cardItem);
                    }
                    Log.i("EERROR", "listasta ei löytyny vastaavia Card Itemeitä");
                    Log.i("EERROR", cardItem.toString());
                    Log.i("EERROR", cardItem.getImageUri().toString());
                }
                cardRepo.updateCards(card);
            } else {
                Log.i("ERRROOOOR", "SUN KORTTIS ON NULL");
            }
        }*/
    public void updateCardItems(CardItem... cardItems) {

        DayCard card = dayCard.getValue();
        if (card != null) {
            List<CardItem> items = card.getCardItems();
            for (int i = 0; i < items.size(); i++) {

                for (CardItem cardItem : cardItems) {
                    Log.i("moi", cardItem.getImageUri().toString());
                    if ((items.get(i).getImageUri() != null && items.get(i).getImageUri().equals(cardItem.getImageUri())) || items.get(i).getText().equals(cardItem.getText())) {
                        items.remove(i);
                        items.add(i, cardItem);

                        Log.i("SUCCESS", "SE ONNISTU!");
                    } else {
                        Log.i("EERROR", "listasta ei löytyny vastaavia Card Itemeitä");
                    }
                }
                cardRepo.updateCards(card);

            }

        } else {
            Log.i("ERRROOOOR", "SUN KORTTIS ON NULL");
        }
    }

    @Override
    public void deleteItem(CardItem cardItem) {
        DayCard card = dayCard.getValue();
        if (card != null) {
            card.getCardItems().remove(cardItem);
            cardRepo.updateCards(card);
        } else {
            throw new NullPointerException("Daycard not initialized!");
        }

    }

    @Override
    public void deleteItem(int i) {
        DayCard card = dayCard.getValue();
        if (card != null) {
            Log.i(TAG, card.getCardItems().toString());
            Log.i(TAG, "index: " + i);
            card.getCardItems().remove(i);
            Log.i(TAG, card.getCardItems().toString());
            cardRepo.updateCards(card);
        } else {
            throw new NullPointerException("Daycard not initialized!");
        }
    }

    @Override
    public void addItem(CardItem cardItem) {
        DayCard card = dayCard.getValue();
        if (card != null) {
            card.getCardItems().add(cardItem);
            cardRepo.updateCards(card);
        } else {
            throw new NullPointerException("Daycard not initialized!");
        }
    }

    @Override
    public void updateItem(CardItem cardItem) {
        DayCard card = dayCard.getValue();
        if (card != null) {

            List<CardItem> items = card.getCardItems();
            for (int i = 0; i < items.size(); i++) {

                if ((items.get(i).getImageUri() != null && items.get(i).getImageUri().equals(cardItem.getImageUri())) || items.get(i).getText().equals(cardItem.getText())) {
                    items.remove(i);
                    items.add(i, cardItem);
                    Log.i(TAG, "Updated cardItem!");
                }
            }
            cardRepo.updateCards(card);
        } else {
            throw new NullPointerException("Daycard not initialized!");
        }
    }
}
