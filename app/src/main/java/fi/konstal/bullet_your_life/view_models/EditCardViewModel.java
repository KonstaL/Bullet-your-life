package fi.konstal.bullet_your_life.view_models;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import java.util.List;

import fi.konstal.bullet_your_life.data.Card;
import fi.konstal.bullet_your_life.data.CardRepository;
import fi.konstal.bullet_your_life.data.DayCard;
import fi.konstal.bullet_your_life.data.NoteCard;
import fi.konstal.bullet_your_life.edit_recycler_view.CardItemHandler;
import fi.konstal.bullet_your_life.task.CardItem;

/**
 * Created by e4klehti on 14.4.2018.
 */

public class EditCardViewModel extends ViewModel implements CardItemHandler {
    private static final String TAG = "EditCardViewModel";

    private LiveData<Card> dayCard;
    private CardRepository cardRepo;
    private int type;

    public EditCardViewModel() {}

    public void init(CardRepository cardRepo, int cardType, int id) {
        if (dayCard == null) {
            if(cardType != Card.CARD_TYPE_DAYCARD && cardType != Card.CARD_TYPE_NOTECARD){
                throw new IllegalArgumentException("Illegal card value!");
            }
            this.type = cardType;
            this.cardRepo = cardRepo;
            this.dayCard = cardRepo.getGeneric(id, cardType);
        }
    }

    public LiveData<Card> getCard() {
        return dayCard;
    }

    public void addCardItems(CardItem... cardItems) {
        if(dayCard.getValue() instanceof DayCard) {
            DayCard card = (DayCard) dayCard.getValue();
            card.addCardItems(cardItems);
            cardRepo.updateCard(card);
        } else if (dayCard.getValue() instanceof NoteCard) {
            NoteCard card = (NoteCard) dayCard.getValue();
            card.addCardItems(cardItems);
            cardRepo.updateCard(card);
        }
    }


    public void updateCardItems(CardItem... cardItems) {

        Card card = dayCard.getValue();
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
                cardRepo.updateCard(card);

            }

        } else {
            Log.i("ERRROOOOR", "SUN KORTTIS ON NULL");
        }
    }

    @Override
    public void deleteItem(CardItem cardItem) {
        Card card = dayCard.getValue();
        if (card != null) {
            card.getCardItems().remove(cardItem);
            cardRepo.updateCard(card);
        } else {
            throw new NullPointerException("Daycard not initialized!");
        }

    }

    @Override
    public void deleteItem(int i) {
        Card card = dayCard.getValue();
        if (card != null) {
            Log.i(TAG, card.getCardItems().toString());
            Log.i(TAG, "index: " + i);
            card.getCardItems().remove(i);
            Log.i(TAG, card.getCardItems().toString());
            cardRepo.updateCard(card);
        } else {
            throw new NullPointerException("Daycard not initialized!");
        }
    }

    @Override
    public void addItem(CardItem cardItem) {
        Card card = dayCard.getValue();
        if (card != null) {
            card.getCardItems().add(cardItem);
            cardRepo.updateCard(card);
        } else {
            throw new NullPointerException("Daycard not initialized!");
        }
    }

    @Override
    public void updateItem(CardItem cardItem) {
        Card card = dayCard.getValue();
        if (card != null) {

            List<CardItem> items = card.getCardItems();
            for (int i = 0; i < items.size(); i++) {

                if ((items.get(i).getImageUri() != null && items.get(i).getImageUri().equals(cardItem.getImageUri())) || items.get(i).getText().equals(cardItem.getText())) {
                    items.remove(i);
                    items.add(i, cardItem);
                    Log.i(TAG, "Updated cardItem!");
                }
            }
            cardRepo.updateCard(card);
        } else {
            throw new NullPointerException("Daycard not initialized!");
        }
    }
}
