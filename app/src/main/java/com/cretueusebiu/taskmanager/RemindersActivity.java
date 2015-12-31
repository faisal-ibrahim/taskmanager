package com.cretueusebiu.taskmanager;

import android.os.Bundle;

public class RemindersActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminders);

        initialize();
    }

}
