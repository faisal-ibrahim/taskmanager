package com.cretueusebiu.taskmanager;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.cretueusebiu.taskmanager.models.Reminder;
import com.cretueusebiu.taskmanager.models.Task;

import java.util.Calendar;

public class EditReminderActivity extends AbstractReminderActivity {

    protected Reminder reminder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_reminder);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initialize();

        reminder = (Reminder) getIntent().getParcelableExtra("reminder");
    }

    @Override
    protected boolean save() {
        String text = textText.getText().toString().trim();

        if (text.isEmpty()) {
            reminder.setText(text);
        }

        reminder.setCalendar(calendar);
        reminder.setAllDay(!isTimeVisible());
        reminder.save();

        Intent intent = new Intent();
        intent.putExtra("reminder", reminder);
        setResult(RemindersActivity.RESULT_OK, intent);

        return true;
    }
}
