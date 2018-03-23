package fi.konstal.bullet_your_life.task;

import java.io.Serializable;

/**
 * Created by konka on 22.3.2018.
 */

public class Task implements Serializable {
    private String text;
    private boolean done;
    private int taskIconRef;


    public Task(String text, int taskIconRef) {
        this.text = text;
        this.done = false;
        this.taskIconRef = taskIconRef;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public int getTaskIconRef() {
        return taskIconRef;
    }

    public void setTaskIconRef(int taskIconRef) {
        this.taskIconRef = taskIconRef;
    }
}
