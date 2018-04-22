package fi.konstal.bullet_your_life.data;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import fi.konstal.bullet_your_life.task.CardItem;



public class CardItemConverter implements Serializable {

    @TypeConverter // note this annotation
    public synchronized String fromTaskList(CopyOnWriteArrayList<CardItem> cardItemList) {
        synchronized (CardItemConverter.class) {
            if (cardItemList == null) {
                return (null);
            }
            Gson gson = new Gson();

            String json = gson.toJson(cardItemList);
            return json;
        }
    }

    @TypeConverter // note this annotation
    public CopyOnWriteArrayList<CardItem> toTaskList(String itemListString) {
        if (itemListString == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<CopyOnWriteArrayList<CardItem>>() {}.getType();
        CopyOnWriteArrayList<CardItem> cardTaskList =  gson.fromJson(itemListString, type);
        return cardTaskList;
    }

    @TypeConverter
    public Card fromDaycard(DayCard dayCard) {
        Card temp = dayCard;
        return temp;
    }

    @TypeConverter
    public Card fromNoteCard(NoteCard noteCard) {
        Card temp = noteCard;
       return temp;
    }
}


