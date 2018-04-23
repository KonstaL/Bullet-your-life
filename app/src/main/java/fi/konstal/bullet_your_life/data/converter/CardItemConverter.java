package fi.konstal.bullet_your_life.data.converter;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.concurrent.CopyOnWriteArrayList;

import fi.konstal.bullet_your_life.task.CardItem;

/**
 * Converted that converts CardItem Lists to GSON and back
 *
 * @author Konsta Lehtinen
 * @version 1.0
 * @since 1.0
 */
public class CardItemConverter implements Serializable {

    /**
     * Convert CardItemList to GSON
     *
     * @param cardItemList The CardItem List
     * @return String GSON value
     */
    @TypeConverter
    public synchronized String fromTaskList(CopyOnWriteArrayList<CardItem> cardItemList) {
        synchronized (CardItemConverter.class) {
            if (cardItemList == null) return null;
            Gson gson = new Gson();

            String json = gson.toJson(cardItemList);
            return json;
        }
    }

    /**
     * Convert {@link Gson} String to CardItem List
     *
     * @param itemListString GSON as String
     * @return
     */
    @TypeConverter
    public CopyOnWriteArrayList<CardItem> toTaskList(String itemListString) {
        if (itemListString == null) return null;

        Gson gson = new Gson();
        Type type = new TypeToken<CopyOnWriteArrayList<CardItem>>() {
        }.getType();
        CopyOnWriteArrayList<CardItem> cardTaskList = gson.fromJson(itemListString, type);
        return cardTaskList;
    }
}


