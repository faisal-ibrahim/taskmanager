package com.cretueusebiu.taskmanager.models;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Objects;

abstract public class AbstractModel {

    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "TaskManager4.db";

    protected static SQLiteOpenHelper dbHelper;

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

    protected static String getString(Cursor cursor, String column) {
        return cursor.getString(cursor.getColumnIndex(column));
    }

    protected static long getLong(Cursor cursor, String column) {
        return cursor.getLong(cursor.getColumnIndex(column));
    }
}
