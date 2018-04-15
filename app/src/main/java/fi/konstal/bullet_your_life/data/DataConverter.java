package fi.konstal.bullet_your_life.data;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.List;

import fi.konstal.bullet_your_life.task.CardItem;



public class DataConverter implements Serializable {

    @TypeConverter // note this annotation
    public String fromTaskList(List<CardItem> cardItemList) {
        if (cardItemList == null) {
            return (null);
        }
        Gson gson = new Gson();

        String json = gson.toJson(cardItemList);
        return json;
    }

    @TypeConverter // note this annotation
    public List<CardItem> toTaskList(String itemListString) {
        if (itemListString == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<CardItem>>() {}.getType();
        List<CardItem> cardTaskList =  gson.fromJson(itemListString, type);
        return cardTaskList;
    }
}


