package com.cretueusebiu.taskmanager.storage;

import android.provider.BaseColumns;

public class TaskContract {

    public TaskContract() {

    }

    public static abstract class Entry implements BaseColumns {
        public static final String TABLE_NAME = "tasks";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_NOTES = "notes";
    }
}
