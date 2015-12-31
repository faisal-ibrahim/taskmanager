package com.cretueusebiu.taskmanager.storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.cretueusebiu.taskmanager.models.Task;

import java.util.ArrayList;

public class TasksStorage {
    protected TaskDbHelper dbHelper = null;

    public TasksStorage(Context context) {
        dbHelper = new TaskDbHelper(context);
    }

    public ArrayList<Task> getAll() {
        ArrayList<Task> list = new ArrayList<>();

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                TaskContract.Entry._ID,
                TaskContract.Entry.COLUMN_NAME_TITLE,
                TaskContract.Entry.COLUMN_NAME_NOTES,
        };

        Cursor cursor = db.query(
                TaskContract.Entry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                TaskContract.Entry._ID + " ASC"
        );

        Task task;

        if (cursor.moveToFirst()) {
            while (cursor.isAfterLast() == false) {
                task = new Task(
                    getString(cursor, TaskContract.Entry.COLUMN_NAME_TITLE),
                    getString(cursor, TaskContract.Entry.COLUMN_NAME_NOTES)
                );

                task.setId(getInt(cursor, TaskContract.Entry._ID));

                list.add(task);

                cursor.moveToNext();
            }
        }

        return list;
    }

    private String getString(Cursor cursor, String column) {
        return cursor.getString(cursor.getColumnIndex(column));
    }

    private Integer getInt(Cursor cursor, String column) {
        return cursor.getInt(cursor.getColumnIndex(column));
    }

    public void add(Task task) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TaskContract.Entry.COLUMN_NAME_TITLE, task.getTitle());
        values.put(TaskContract.Entry.COLUMN_NAME_NOTES, task.getNotes());

        db.insert(TaskContract.Entry.TABLE_NAME, null, values);
    }

    public void remove(Task task) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String selection = TaskContract.Entry._ID + " = ?";

        String[] selectionArgs = { task.getId().toString() };

        db.delete(TaskContract.Entry.TABLE_NAME, selection, selectionArgs);
    }
}
