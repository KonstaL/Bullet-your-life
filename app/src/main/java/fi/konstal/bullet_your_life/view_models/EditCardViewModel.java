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
 * Handles {@link fi.konstal.bullet_your_life.activities.EditCardActivity} data storing
 * and mutating
 *
 * @author Konsta Lehtinen
 * @version 1.0
 * @since 1.0
 */
public class EditCardViewModel extends ViewModel implements CardItemHandler {
    private static final String TAG = "EditCardViewModel";

    private LiveData<Card> dayCard;
    private CardRepository cardRepo;
    private int type;

    /**
     * Default no-args constructor for {@link ViewModel}
     */
    public EditCardViewModel() {
    }

    /**
     * Initializes the ViewModel
     *
     * @param cardRepo The CardRepo from which to get the needed data
     * @param cardType The type of cards to fetch
     * @param id       The id of the card
     */
    public void init(CardRepository cardRepo, int cardType, int id) {
        if (dayCard == null) {
            if (cardType != Card.CARD_TYPE_DAYCARD && cardType != Card.CARD_TYPE_NOTECARD) {
                throw new IllegalArgumentException("Illegal card value!");
            }
            this.type = cardType;
            this.cardRepo = cardRepo;
            this.dayCard = cardRepo.getGeneric(id, cardType);
        }
    }

    /**
     * Returns LiveData of the found Card
     *
     * @return The found Card
     */
    public LiveData<Card> getCard() {
        return dayCard;
    }

    /**
     * Add items to the current card and the the card to DB
     *
     * @param cardItems the card items to add
     */
    public void addCardItems(CardItem... cardItems) {
        if (dayCard.getValue() instanceof DayCard) {
            DayCard card = (DayCard) dayCard.getValue();
            card.addCardItems(cardItems);
            cardRepo.updateCard(card);
        } else if (dayCard.getValue() instanceof NoteCard) {
            NoteCard card = (NoteCard) dayCard.getValue();
            card.addCardItems(cardItems);
            cardRepo.updateCard(card);
        }
    }


    /**
     * Update the given CardItems in the current card and push the modified card to DB
     *
     * @param cardItems the carditems to modify
     */
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

    /**
     * {@inheritDoc}
     */
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

    /**
     * {@inheritDoc}
     */
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

    /**
     * {@inheritDoc}
     */
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

    /**
     * {@inheritDoc}
     */
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
