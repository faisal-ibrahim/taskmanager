package com.cretueusebiu.taskmanager.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.cretueusebiu.taskmanager.R;
import com.cretueusebiu.taskmanager.models.Task;

import java.util.ArrayList;
import java.util.List;

public class TaskAdapter extends ArrayAdapter<Task> {
    public TaskAdapter(Context context, List<Task> tasks) {
        super(context, 0, tasks);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Task task = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.task_item, parent, false);
        }

        TextView titleView = (TextView) convertView.findViewById(R.id.task_item_title);
        TextView notesView = (TextView) convertView.findViewById(R.id.task_item_notes);

        String title = task.getTitle();
        String notes = task.getNotes();

        if (title.length() > 40) {
            title = title.substring(0, 40) + "...";
        }

        if (notes.length() > 100) {
            notes = notes.substring(0, 100) + "...";
        }

        titleView.setText(title);
        notesView.setText(notes);

        return convertView;
    }
}
