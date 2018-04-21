package fi.konstal.bullet_your_life.view_models;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import fi.konstal.bullet_your_life.data.CardRepository;
import fi.konstal.bullet_your_life.data.NoteCard;

/**
 * Created by e4klehti on 21.4.2018.
 */

public class NotesViewModel extends ViewModel {
    private LiveData<List<NoteCard>> noteCards;
    private CardRepository cardRepository;

    public NotesViewModel() {}


    public void init(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
        if (this.noteCards == null) {
            noteCards = cardRepository.getNoteCards();
        }
    }

    public LiveData<List<NoteCard>> getNoteCards() {
        return noteCards;
    }
}

