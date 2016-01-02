package com.cretueusebiu.taskmanager.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.cretueusebiu.taskmanager.R;
import com.cretueusebiu.taskmanager.models.Reminder;
import com.cretueusebiu.taskmanager.models.Task;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ReminderAdapter extends ArrayAdapter<Reminder> {
    private Reminder reminder = null;

    public ReminderAdapter(Context context, ArrayList<Reminder> reminders) {
        super(context, 0, reminders);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        reminder = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                                        .inflate(R.layout.reminder_item, parent, false);
        }

        TextView textView = (TextView) convertView.findViewById(R.id.reminder_text);
        TextView timeView = (TextView) convertView.findViewById(R.id.reminder_time);
        TextView dayView = (TextView) convertView.findViewById(R.id.reminder_day);
        TextView monthView = (TextView) convertView.findViewById(R.id.reminder_month);
        TextView yearView = (TextView) convertView.findViewById(R.id.reminder_year);

        textView.setText(getText());
        dayView.setText(getDay());
        monthView.setText(getMonth());
        yearView.setText(getYear());

        if (reminder.isAllDay()) {
            timeView.setVisibility(View.GONE);
        } else {
            timeView.setText(getTime());
        }

        return convertView;
    }

    private String getText() {
        String text = reminder.getText();
        if (text.length() > 80) {
            text = text.substring(0, 80) + "...";
        }
        return text;
    }

    private String getTime() {
        return reminder.getCalendar().get(Calendar.HOUR) + ":" +
                reminder.getCalendar().get(Calendar.MINUTE);
    }

    private String getDay() {
        return Integer.toString(reminder.getCalendar().get(Calendar.DAY_OF_MONTH));
    }

    private String getMonth() {
        return reminder.getCalendar()
                .getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault());
    }

    private String getYear() {
        return Integer.toString(reminder.getCalendar().get(Calendar.YEAR));
    }
}
