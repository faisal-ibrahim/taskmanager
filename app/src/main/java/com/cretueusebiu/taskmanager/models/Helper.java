package com.cretueusebiu.taskmanager.models;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Helper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "TaskManager";

    public Helper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
            "CREATE TABLE " + Task.Entry.TABLE_NAME + "(" +
                Task.Entry._ID + " INTEGER PRIMARY KEY," +
                Task.Entry.COLUMN_NAME_TITLE + " TEXT," +
                Task.Entry.COLUMN_NAME_NOTES + " TEXT," +
                Task.Entry.COLUMN_NAME_CREATED + " TEXT," +
                Task.Entry.COLUMN_NAME_UPDATED + " TEXT" +
            ")"
        );

        db.execSQL(
            "CREATE TABLE " + Reminder.Entry.TABLE_NAME + "(" +
                Reminder.Entry._ID + " INTEGER PRIMARY KEY," +
                Reminder.Entry.COLUMN_NAME_TEXT + " TEXT," +
                Reminder.Entry.COLUMN_NAME_DATE + " TEXT," +
                Reminder.Entry.COLUMN_NAME_ALLDAY + " INTEGER(1)," +
                Reminder.Entry.COLUMN_NAME_CREATED + " TEXT," +
                Reminder.Entry.COLUMN_NAME_UPDATED + " TEXT" +
            ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Task.Entry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Reminder.Entry.TABLE_NAME);
        onCreate(db);
    }
}
