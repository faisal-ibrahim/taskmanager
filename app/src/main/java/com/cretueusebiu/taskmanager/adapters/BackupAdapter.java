package com.cretueusebiu.taskmanager.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.cretueusebiu.taskmanager.R;

import java.util.List;

public class BackupAdapter extends ArrayAdapter<String> {

    public BackupAdapter(Context context, List<String> backups) {
        super(context, 0, backups);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String backup = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.backup_item, parent, false);
        }

        TextView nameView = (TextView) convertView.findViewById(R.id.backup_item_name);

        nameView.setText(backup);

        return convertView;
    }
}
