package fi.konstal.bullet_your_life.data;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.List;

import fi.konstal.bullet_your_life.task.CardTask;


public class DataConverter implements Serializable {

    @TypeConverter // note this annotation
    public String fromTaskList(List<CardTask> cardTaskList) {
        if (cardTaskList == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<CardTask>>() {
        }.getType();
        String json = gson.toJson(cardTaskList, type);
        return json;
    }

    @TypeConverter // note this annotation
    public List<CardTask> toTaskList(String tasksListString) {
        if (tasksListString == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<CardTask>>() {
        }.getType();
        List<CardTask> cardTaskList = gson.fromJson(tasksListString, type);
        return cardTaskList;
    }

}