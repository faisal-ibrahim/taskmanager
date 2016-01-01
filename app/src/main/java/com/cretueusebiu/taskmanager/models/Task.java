package com.cretueusebiu.taskmanager.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Task extends AbstractModel implements Serializable {

    protected String id = null;
    protected String title;
    protected String notes;
    private String created = null;
    private String updated = null;

    public Task(String title, String notes) {
        this.title = title;
        this.notes = notes;
    }

    public Task(String title, String notes, String created, String updated) {
        this.title = title;
        this.notes = notes;
        this.created = created;
        this.updated = updated;
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getNotes() { return notes; }
    public String getUpdated() { return updated; }

    public void setId(String id) {
        this.id = id;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setNotes(String notes) {
        this.notes = notes;
    }

    /**
     * Save the current model.
     */
    @Override
    public void save() {
        ContentValues values = new ContentValues();
        values.put(Entry.COLUMN_NAME_TITLE, this.getTitle());
        values.put(Entry.COLUMN_NAME_NOTES, this.getNotes());

        SimpleDateFormat ft = new SimpleDateFormat("MMMM d y, H:m");
        String now = ft.format(new Date());

        if (created == null) {
            created = now;
            values.put(Entry.COLUMN_NAME_CREATED, created);
        }

        updated = now;
        values.put(Entry.COLUMN_NAME_UPDATED, updated);

        if (id != null) {
            _update(Entry.TABLE_NAME, values, getId());
        } else {
            id = Long.toString(_insert(Entry.TABLE_NAME, values));
        }
    }

    public void delete() {
        _deleteById(Entry.TABLE_NAME, getId());
    }

    /**
     * Create a new task.
     *
     * @param title
     * @param notes
     * @return Task
     */
    public static Task create(String title, String notes) {
        Task task = new Task(title, notes);
        task.save();
        return task;
    }

    public static ArrayList<Task> all(boolean asc) {
        ArrayList<Task> list = new ArrayList<>();

        String[] projection = {
            Entry._ID,
            Entry.COLUMN_NAME_TITLE,
            Entry.COLUMN_NAME_NOTES,
            Entry.COLUMN_NAME_CREATED,
            Entry.COLUMN_NAME_UPDATED
        };

        Cursor cursor = dbHelper.getReadableDatabase().query(
            Entry.TABLE_NAME,
            projection,
            null,
            null,
            null,
            null,
            Entry._ID + (asc ? " ASC" : " DESC")
        );

        Task task;

        if (cursor.moveToFirst()) {
            while (cursor.isAfterLast() == false) {
                task = new Task(
                    getString(cursor, Entry.COLUMN_NAME_TITLE),
                    getString(cursor, Entry.COLUMN_NAME_NOTES),
                    getString(cursor, Entry.COLUMN_NAME_CREATED),
                    getString(cursor, Entry.COLUMN_NAME_UPDATED)
                );

                task.setId(getString(cursor, Entry._ID));

                list.add(task);

                cursor.moveToNext();
            }
        }

        return list;
    }

    public static abstract class Entry implements BaseColumns {
        public static final String TABLE_NAME = "tasks";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_NOTES = "notes";
        public static final String COLUMN_NAME_CREATED = "created_at";
        public static final String COLUMN_NAME_UPDATED = "updated_at";
    }

    public static void init(Context context) {
        dbHelper = new Task.DbHelper(context);
    }

    public static class DbHelper extends SQLiteOpenHelper {
        public DbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(
                "CREATE TABLE " + Entry.TABLE_NAME + "(" +
                     Entry._ID + " INTEGER PRIMARY KEY," +
                     Entry.COLUMN_NAME_TITLE + " TEXT," +
                     Entry.COLUMN_NAME_NOTES + " TEXT," +
                     Entry.COLUMN_NAME_CREATED + " TEXT," +
                     Entry.COLUMN_NAME_UPDATED + " TEXT" +
                ")"
            );
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + Entry.TABLE_NAME);
            onCreate(db);
        }
    }
}
