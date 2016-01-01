package com.cretueusebiu.taskmanager.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.cretueusebiu.taskmanager.R;
import com.cretueusebiu.taskmanager.models.Reminder;
import com.cretueusebiu.taskmanager.models.Task;

import java.util.ArrayList;

public class ReminderAdapter extends ArrayAdapter<Reminder> {
    public ReminderAdapter(Context context, ArrayList<Reminder> reminders) {
        super(context, 0, reminders);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Reminder reminder = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.reminder_item, parent, false);
        }

        TextView textView = (TextView) convertView.findViewById(R.id.reminder_item_text);

        String text = reminder.getText();

        if (text.length() > 150) {
            text = text.substring(0, 150) + "...";
        }

        textView.setText(text);

        return convertView;
    }
}
