package com.cretueusebiu.taskmanager.models;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

abstract public class AbstractModel {

    protected static SQLiteOpenHelper dbHelper;
    protected static SimpleDateFormat dateFormat;

    public static void setContext(Context context) {
        dbHelper = new Helper(context);
        dateFormat = new SimpleDateFormat("MM/dd/yyyy kk:mm");
    }

    public static SQLiteOpenHelper getDbHelper() {
        return dbHelper;
    }

    public abstract void save();

    protected long _insert(String table, ContentValues values) {
        return dbHelper.getWritableDatabase().insert(table, null, values);
    }

    protected int _update(String table, ContentValues values, String id) {
        String whereClause = "_id = ?";
        String[] whereArgs = { id };
        return dbHelper.getWritableDatabase().update(table, values, whereClause, whereArgs);
    }

    protected int _delete(String table, String whereClause, String[] whereArgs) {
        return dbHelper.getWritableDatabase().delete(table, whereClause, whereArgs);
    }

    protected int _deleteById(String table, String id) {
        String[] whereArgs = { id };
        return _delete(table, "_id = ?", whereArgs);
    }

    protected static int getInt(Cursor cursor, String column) {
        return cursor.getInt(cursor.getColumnIndex(column));
    }

    protected static String getString(Cursor cursor, String column) {
        return cursor.getString(cursor.getColumnIndex(column));
    }

    protected static boolean getBoolean(Cursor cursor, String column) {
        return getInt(cursor, column) == 1;
    }

    protected static long getLong(Cursor cursor, String column) {
        return cursor.getLong(cursor.getColumnIndex(column));
    }

    protected static Calendar getCalendar(Cursor cursor, String column) {
        return stringToCalendar(getString(cursor, column));
    }

    protected static Calendar stringToCalendar(String str) {
        Calendar calendar = Calendar.getInstance();

        try {
            calendar.setTime(dateFormat.parse(str));
        } catch (ParseException e) {
            // e.printStackTrace();
        }

        return calendar;
    }
}
