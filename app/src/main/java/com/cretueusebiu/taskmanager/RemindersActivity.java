package com.cretueusebiu.taskmanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.cretueusebiu.taskmanager.adapters.ReminderAdapter;
import com.cretueusebiu.taskmanager.models.Reminder;

import java.util.ArrayList;

public class RemindersActivity extends BaseActivity {

    private ReminderAdapter adapter;
    private ArrayList<Reminder> reminders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminders);

        initialize();

        Reminder.init(this);

        reminders = Reminder.all(false);

        adapter = new ReminderAdapter(this, reminders);

        ListView listView = (ListView) findViewById(R.id.reminders_list_view);
        listView.setAdapter(adapter);

        registerForContextMenu(listView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
//                Task task = tasks.get(position);
//                Intent intent = new Intent(adapterView.getContext(), DisplayTaskActivity.class);
//                intent.putExtra("task", task);
//                intent.putExtra("position", position);
//                startActivityForResult(intent, REQUEST_DISPLAY_TASK);
            }
        });
    }

}
