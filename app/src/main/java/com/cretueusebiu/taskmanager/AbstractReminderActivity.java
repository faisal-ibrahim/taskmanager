package com.cretueusebiu.taskmanager;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public abstract class AbstractReminderActivity extends AppCompatActivity {

    protected TextView textText;
    protected TextView dateText;
    protected TextView timeText;
    protected Switch timeSwitch;
    protected Calendar calendar;
    protected TextView repeatText;
    protected AlertDialog.Builder repeatDialog;
    protected int checkedRepeatItem = 0;
    protected String repeatItems[] = {"Does not repeat", "Every day",
            "Every week", "Every month", "Every year"};

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save, menu);
        return true;
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

    protected abstract boolean save();

    protected String getRepeatItem(int pos) {
        return repeatItems[pos];
    }

    protected void initialize() {
        textText = (EditText) findViewById(R.id.create_reminder_text);
        dateText = (TextView) findViewById(R.id.reminder_date);
        timeText = (TextView) findViewById(R.id.reminder_time);
        timeSwitch = (Switch) findViewById(R.id.reminder_time_toggle);
        repeatText = (TextView) findViewById(R.id.reminder_repeat);

        updateTimeText();
        updateDateText();

        dateText.setOnClickListener(dateListener);
        timeText.setOnClickListener(timeListener);
        timeSwitch.setOnClickListener(switchListener);
        repeatText.setOnClickListener(repeatListener);
    }

    protected View.OnClickListener repeatListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            repeatDialog = new AlertDialog.Builder(AbstractReminderActivity.this);
            repeatDialog.setSingleChoiceItems(repeatItems, checkedRepeatItem,
                    new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    checkedRepeatItem = which;
                    repeatText.setText(repeatItems[which]);
                    dialog.dismiss();
                }
            });

            repeatDialog.show();
        }
    };

    protected View.OnClickListener dateListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int y = calendar.get(Calendar.YEAR);
            int m = calendar.get(Calendar.MONTH);
            int d = calendar.get(Calendar.DAY_OF_MONTH);
            new DatePickerDialog(AbstractReminderActivity.this, new DatePickerDialog.OnDateSetListener() {
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
            new TimePickerDialog(AbstractReminderActivity.this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int h, int m) {
                    calendar.set(Calendar.HOUR_OF_DAY, h);
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
