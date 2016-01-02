package com.cretueusebiu.taskmanager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

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

        dateText = (TextView) findViewById(R.id.reminder_date);
        timeText = (TextView) findViewById(R.id.reminder_time);
        timeSwitch = (Switch) findViewById(R.id.reminder_time_toggle);

        calendar = Calendar.getInstance();

        updateTimeText();
        updateDateText();

        dateText.setOnClickListener(dateListener);
        timeText.setOnClickListener(timeListener);
        timeSwitch.setOnClickListener(switchListener);
    }

    protected boolean save() {
        EditText rtext = (EditText) findViewById(R.id.create_reminder_text);

        String text = rtext.getText().toString().trim();

        if (text.isEmpty()) {
            return false;
        }

        Reminder reminder = Reminder.create(text, calendar, !isTimeVisible());

        Intent intent = new Intent();

        intent.putExtra("reminder", reminder);

        setResult(RemindersActivity.RESULT_OK, intent);

        return true;
    }
}
