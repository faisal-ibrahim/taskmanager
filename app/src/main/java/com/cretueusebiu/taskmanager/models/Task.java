package com.cretueusebiu.taskmanager.models;

import android.provider.BaseColumns;

import java.io.Serializable;

public class Task extends AbstractModel implements Serializable {
    private String title;
    private String notes;
    private Integer id = null;

    public static abstract class Entry implements BaseColumns {
        public static final String TABLE_NAME = "tasks";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_NOTES = "notes";
    }

    public Task(String title, String notes) {
        this.title = title;
        this.notes = notes;
    }

    public static Task create(String title, String notes) {
        Task task = new Task(title, notes);
        task.save();

        return task;
    }

    public String getTitle() {
        return title;
    }

    public String getNotes() {
        return notes;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    @Override
    public void save() {

    }
}
