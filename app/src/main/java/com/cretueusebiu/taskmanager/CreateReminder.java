package com.cretueusebiu.taskmanager;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import com.cretueusebiu.taskmanager.models.Reminder;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CreateReminder extends AppCompatActivity {

    protected TextView dateText;
    protected TextView timeText;
    protected Switch timeSwitch;
    protected Calendar calendar;

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            case R.id.save:
                if (!save()) {
                    return false;
                }
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save, menu);
        return true;
    }

    protected boolean save() {
        EditText rText = (EditText) findViewById(R.id.create_reminder_text);

        String rtext = rText.getText().toString().trim();

        if (rtext.isEmpty()) {
            return false;
        }

        Reminder reminder = Reminder.create(rtext, calendar, isTimeVisible());

        Intent intent = new Intent();

        intent.putExtra("reminder", reminder);

        setResult(RemindersActivity.RESULT_OK, intent);

        return true;
    }

    protected View.OnClickListener dateListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Calendar cal = Calendar.getInstance();
            int y = cal.get(Calendar.YEAR);
            int m = cal.get(Calendar.MONTH);
            int d = cal.get(Calendar.DAY_OF_MONTH);
            new DatePickerDialog(CreateReminder.this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int y, int m, int d) {
                    calendar.set(Calendar.YEAR, y);
                    calendar.set(Calendar.MONTH, m);
                    calendar.set(Calendar.DAY_OF_MONTH, d);
                    updateDateText();
                }
            }, y, m, d).show();
        }
    };

    protected  View.OnClickListener timeListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int h = calendar.get(Calendar.HOUR_OF_DAY);
            int m = calendar.get(Calendar.MINUTE);
            new TimePickerDialog(CreateReminder.this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int h, int m) {
                    calendar.set(Calendar.HOUR, h);
                    calendar.set(Calendar.MINUTE, m);
                    updateTimeText();
                }
            }, h, m, true).show();
        }
    };

    protected View.OnClickListener switchListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            timeText.setVisibility(isTimeVisible() ? View.GONE : View.VISIBLE);
        }
    };

    protected void updateDateText() {
        dateText.setText(new SimpleDateFormat("EE, MMM d, y").format(calendar.getTime()));
    }

    protected void updateTimeText() {
        timeText.setText(new SimpleDateFormat("kk:mm").format(calendar.getTime()));
    }

    protected boolean isTimeVisible() {
        return timeText.getVisibility() == View.VISIBLE;
    }
}
