package com.cretueusebiu.taskmanager.models;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Reminder extends  AbstractModel implements Serializable {

    protected String id = null;
    protected String text;
    private String created = null;
    private String updated = null;

    public Reminder(String text) {
        this.text = text;
    }

    public Reminder(String text, String created, String updated) {
        this.text = text;
        this.created = created;
        this.updated = updated;
    }

    public String getId() { return id; }
    public String getText() { return text; }
    public String getUpdated() { return updated; }

    public void setId(String id) {
        this.id = id;
    }
    public void setText(String text) {
        this.text = text;
    }

    /**
     * Save the current model.
     */
    @Override
    public void save() {
        ContentValues values = new ContentValues();
        values.put(Entry.COLUMN_NAME_TEXT, this.getText());

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
     * Create a new reminder.
     *
     * @param text
     * @return Reminder
     */
    public static Reminder create(String text) {
        Reminder reminder = new Reminder(text);
        reminder.save();
        return reminder;
    }

    public static ArrayList<Reminder> all(boolean asc) {
        ArrayList<Reminder> list = new ArrayList<>();

        String[] projection = {
            Entry._ID,
            Entry.COLUMN_NAME_TEXT,
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

        Reminder reminder;

        if (cursor.moveToFirst()) {
            while (cursor.isAfterLast() == false) {
                reminder = new Reminder(
                    getString(cursor, Entry.COLUMN_NAME_TEXT),
                    getString(cursor, Entry.COLUMN_NAME_CREATED),
                    getString(cursor, Entry.COLUMN_NAME_UPDATED)
                );

                reminder.setId(getString(cursor, Entry._ID));

                list.add(reminder);

                cursor.moveToNext();
            }
        }

        return list;
    }

    public static abstract class Entry implements BaseColumns {
        public static final String TABLE_NAME = "reminders";
        public static final String COLUMN_NAME_TEXT = "rtext";
        public static final String COLUMN_NAME_CREATED = "created_at";
        public static final String COLUMN_NAME_UPDATED = "updated_at";
    }

    public static void init(Context context) {
        dbHelper = new Reminder.DbHelper(context);
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
                    Entry.COLUMN_NAME_TEXT + " TEXT," +
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
