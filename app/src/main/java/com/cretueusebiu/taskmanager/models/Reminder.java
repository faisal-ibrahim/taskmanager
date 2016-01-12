package com.cretueusebiu.taskmanager.models;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.BaseColumns;
import android.util.Log;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Reminder extends  AbstractModel implements Parcelable {

    protected String id = null;
    protected Calendar calendar;
    protected boolean allDay;
    protected int repeat;
    protected String text;
    protected String created = null;
    protected String updated = null;

    public static final int DOES_NOT_REPEAT = 0;
    public static final int REPEAT_MINUTE = 1;
    public static final int REPEAT_HOUR = 2;
    public static final int REPEAT_DAY = 3;
    public static final int REPEAT_WEEK = 4;
    public static final int REPEAT_MONTH = 5;
    public static final int REPEAT_YEAR = 6;

    public Reminder(String text, Calendar calendar, boolean allDay, int repeat) {
        this.text = text;
        this.calendar = calendar;
        this.allDay = allDay;
        this.repeat = repeat;
    }

    public Reminder(String text, Calendar calendar, boolean allDay, int repeat, String created, String updated) {
        this.text = text;
        this.calendar = calendar;
        this.allDay = allDay;
        this.repeat = repeat;
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
    public int getRepeat() { return repeat; }

    public void setId(String id) {
        this.id = id;
    }
    public void setText(String text) {
        this.text = text;
    }
    public void setRepeat(int repeat) {
        this.repeat = repeat;
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
        values.put(Entry.COLUMN_NAME_REPEAT, this.getRepeat());

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
    public static Reminder create(String text, Calendar calendar, boolean allDay, int repeat) {
        Reminder reminder = new Reminder(text, calendar, allDay, repeat);
        reminder.save();
        return reminder;
    }

    public static Reminder find(String id) {
        String [] selectionArgs = { id };

        Cursor cursor = dbHelper.getReadableDatabase().query(
                Entry.TABLE_NAME,
                getColumns(),
                Entry._ID + "= ?",
                selectionArgs,
                null,
                null,
                null
        );

        Reminder reminder;

        if (cursor.moveToFirst()) {
            while (cursor.isAfterLast() == false) {
                reminder = new Reminder(
                        getString(cursor, Entry.COLUMN_NAME_TEXT),
                        getCalendar(cursor, Entry.COLUMN_NAME_DATE),
                        getBoolean(cursor, Entry.COLUMN_NAME_ALLDAY),
                        getInt(cursor, Entry.COLUMN_NAME_REPEAT),
                        getString(cursor, Entry.COLUMN_NAME_CREATED),
                        getString(cursor, Entry.COLUMN_NAME_UPDATED)
                );

                reminder.setId(getString(cursor, Entry._ID));

                cursor.moveToNext();

                return reminder;
            }
        }

        return null;
    }

    private static String[] getColumns() {
        String[] columns = {
                Entry._ID,
                Entry.COLUMN_NAME_TEXT,
                Entry.COLUMN_NAME_DATE,
                Entry.COLUMN_NAME_ALLDAY,
                Entry.COLUMN_NAME_REPEAT,
                Entry.COLUMN_NAME_CREATED,
                Entry.COLUMN_NAME_UPDATED
        };

        return columns;
    }

    public static ArrayList<Reminder> all() {
        ArrayList<Reminder> list = new ArrayList<>();

        Cursor cursor = dbHelper.getReadableDatabase().query(
            Entry.TABLE_NAME,
            getColumns(),
            null,
            null,
            null,
            null,
            Entry.COLUMN_NAME_DATE + " ASC"
        );

        Reminder reminder;

        if (cursor.moveToFirst()) {
            while (cursor.isAfterLast() == false) {
                reminder = new Reminder(
                    getString(cursor, Entry.COLUMN_NAME_TEXT),
                    getCalendar(cursor, Entry.COLUMN_NAME_DATE),
                    getBoolean(cursor, Entry.COLUMN_NAME_ALLDAY),
                    getInt(cursor, Entry.COLUMN_NAME_REPEAT),
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

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }

    public void setAllDay(boolean allDay) {
        this.allDay = allDay;
    }

    public static abstract class Entry implements BaseColumns {
        public static final String TABLE_NAME = "reminders";
        public static final String COLUMN_NAME_TEXT = "rtext";
        public static final String COLUMN_NAME_DATE = "rdate";
        public static final String COLUMN_NAME_ALLDAY = "allday";
        public static final String COLUMN_NAME_REPEAT = "repeat";
        public static final String COLUMN_NAME_CREATED = "created_at";
        public static final String COLUMN_NAME_UPDATED = "updated_at";
    }

    public Reminder (Parcel in) {
        String[] data = new String[7];
        in.readStringArray(data);

        id = data[0];
        text = data[1];
        calendar = stringToCalendar(data[2]);
        allDay = data[3].equals("1");
        repeat = Integer.parseInt(data[4]);
        created = data[5];
        updated = data[6];
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[] {
                id, text, getCalendarString(), isAllDay() ? "1" : "0",
                Integer.toString(repeat), created, updated
        });
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Reminder createFromParcel(Parcel in) {
            return new Reminder(in);
        }

        public Reminder[] newArray(int size) {
            return new Reminder[size];
        }
    };
}
