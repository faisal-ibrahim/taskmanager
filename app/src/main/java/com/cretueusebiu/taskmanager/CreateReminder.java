package com.cretueusebiu.taskmanager;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.cretueusebiu.taskmanager.alarm.AlarmReceiver;
import com.cretueusebiu.taskmanager.alarm.ReminderManager;
import com.cretueusebiu.taskmanager.models.Reminder;

import java.util.Calendar;

public class CreateReminder extends AbstractReminderActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_reminder);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, 1);

        initialize();
    }

    protected boolean save() {
        String text = textText.getText().toString().trim();

        if (text.isEmpty()) {
            return false;
        }

        Reminder reminder = Reminder.create(text, calendar, !isTimeVisible(), checkedRepeatItem);

        new ReminderManager(this).set(reminder);

        setResult(RemindersActivity.RESULT_OK, new Intent());

        return true;
    }
}
