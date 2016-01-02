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
import java.util.Calendar;
import java.util.Date;

public class Reminder extends  AbstractModel implements Serializable {

    protected String id = null;
    protected Calendar calendar;
    protected boolean allDay;
    protected String text;
    protected String created = null;
    protected String updated = null;

    public Reminder(String text, Calendar calendar, boolean allDay) {
        this.text = text;
        this.calendar = calendar;
        this.allDay = allDay;
    }

    public Reminder(String text, Calendar calendar, boolean allDay, String created, String updated) {
        this.text = text;
        this.calendar = calendar;
        this.allDay = allDay;
        this.created = created;
        this.updated = updated;
    }

    public String getId() { return id; }
    public String getText() { return text; }
    public Calendar getCalendar() {
        return calendar;
    }
    public boolean isAllDay() {
        return allDay;
    }
    public String getUpdated() { return updated; }
    public String getCalendarString() {
       return dateFormat.format(calendar.getTime());
    }

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
        values.put(Entry.COLUMN_NAME_DATE, this.getCalendarString());
        values.put(Entry.COLUMN_NAME_ALLDAY, this.isAllDay() ? 1 : 0);

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
     * @return Reminder
     */
    public static Reminder create(String text, Calendar calendar, boolean allDay) {
        Reminder reminder = new Reminder(text, calendar, allDay);
        reminder.save();
        return reminder;
    }

    public static ArrayList<Reminder> all(boolean asc) {
        ArrayList<Reminder> list = new ArrayList<>();

        String[] projection = {
            Entry._ID,
            Entry.COLUMN_NAME_TEXT,
            Entry.COLUMN_NAME_DATE,
            Entry.COLUMN_NAME_ALLDAY,
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
                    getCalendar(cursor, Entry.COLUMN_NAME_DATE),
                    getBoolean(cursor, Entry.COLUMN_NAME_ALLDAY),
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
        public static final String COLUMN_NAME_DATE = "rdate";
        public static final String COLUMN_NAME_ALLDAY = "allday";
        public static final String COLUMN_NAME_CREATED = "created_at";
        public static final String COLUMN_NAME_UPDATED = "updated_at";
    }
}
