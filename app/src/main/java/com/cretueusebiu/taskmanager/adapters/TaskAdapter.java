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

public class TaskAdapter extends ArrayAdapter<Task> {
    public TaskAdapter(Context context, ArrayList<Task> tasks) {
        super(context, 0, tasks);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Task task = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.task_item, parent, false);
        }

        TextView titleView = (TextView) convertView.findViewById(R.id.task_item_title);

        String title = task.getTitle();

        if (title.isEmpty()) {
            title = task.getNotes();
        }

        titleView.setText(title);

        return convertView;
    }
}
