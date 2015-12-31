package com.cretueusebiu.taskmanager.storage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TaskDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "TaskManager.db";


    public TaskDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
            "CREATE TABLE " + TaskContract.Entry.TABLE_NAME + "(" +
                TaskContract.Entry._ID + " INTEGER PRIMARY KEY," +
                TaskContract.Entry.COLUMN_NAME_TITLE + " TEXT," +
                TaskContract.Entry.COLUMN_NAME_NOTES + " TEXT" +
            ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TaskContract.Entry.TABLE_NAME);
        onCreate(db);
    }
}
