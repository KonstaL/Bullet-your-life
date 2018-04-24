package fi.konstal.bullet_your_life.view_models;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import fi.konstal.bullet_your_life.data.CardRepository;
import fi.konstal.bullet_your_life.data.NoteCard;


/**
 * The ViewModel that handles the {@link NoteCard} date
 *
 * @author Konsta Lehtinen
 * @version 1.0
 * @see fi.konstal.bullet_your_life.fragment.NotesFragment
 * @since 1.0
 */
public class NotesViewModel extends ViewModel {
    private LiveData<List<NoteCard>> noteCards;
    private CardRepository cardRepository;

    /**
     * No args constructor for {@link ViewModel}
     */
    public NotesViewModel() {
    }

    /**
     * Initializes the ViewModel by giving it a LiveData source
     *
     * @param cardRepository The data source
     */
    public void init(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
        if (this.noteCards == null) {
            noteCards = cardRepository.getNoteCards();
        }
    }

    /**
     * Deletes A card from the DB
     *
     * @param position
     */
    public void deleteCard(int position) {
        cardRepository.removeCard(noteCards.getValue().get(position));
    }

    /**
     * Returns a {@link LiveData} {@link List} of the current notecards
     *
     * @return All NoteCards
     */
    public LiveData<List<NoteCard>> getNoteCards() {
        return noteCards;
    }
}

