package com.baima.objectivebook.entities;

import org.litepal.crud.LitePalSupport;

public class Objective extends LitePalSupport {

    public static final int TYPE_TODAY=0;
    public static final int TYPE_SHORT=1;
    public static final int TYPE_LONG=2;
    public static final int TYPE_FINAL=3;

    private long id;
    private String title;
    private int objectiveType;
    private boolean finish;
    private long timestamp;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getObjectiveType() {
        return objectiveType;
    }

    public void setObjectiveType(int objectiveType) {
        this.objectiveType = objectiveType;
    }

    public boolean isFinish() {
        return finish;
    }

    public void setFinish(boolean finish) {
        this.finish = finish;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Objective{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", objectiveType=" + objectiveType +
                ", finish=" + finish +
                ", timestamp=" + timestamp +
                '}';
    }
}
