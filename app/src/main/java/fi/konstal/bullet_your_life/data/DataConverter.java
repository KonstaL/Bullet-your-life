package fi.konstal.bullet_your_life.data;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.List;

import fi.konstal.bullet_your_life.task.Task;


public class DataConverter implements Serializable {

    @TypeConverter // note this annotation
    public String fromTaskList(List<Task> taskList) {
        if (taskList == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<Task>>() {
        }.getType();
        String json = gson.toJson(taskList, type);
        return json;
    }

    @TypeConverter // note this annotation
    public List<Task> toTaskList(String tasksListString) {
        if (tasksListString == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<Task>>() {
        }.getType();
        List<Task> taskList = gson.fromJson(tasksListString, type);
        return taskList;
    }

}