package com.cretueusebiu.taskmanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.cretueusebiu.taskmanager.adapters.ReminderAdapter;
import com.cretueusebiu.taskmanager.models.Reminder;
import com.cretueusebiu.taskmanager.models.Task;

import java.util.ArrayList;

public class RemindersActivity extends AbstractActivity {

    private static final String REMINDERS_FILTERED = "reminders_filtered";
    private ReminderAdapter adapter;
    private ArrayList<Reminder> reminders;
    private ArrayList<Reminder> remindersFiltered;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminders);

        initialize();

        reminders = Reminder.all();

        if (savedInstanceState == null || !isSearchOpened()) {
            remindersFiltered = new ArrayList<Reminder>(reminders);
        } else {
            remindersFiltered = savedInstanceState.getParcelableArrayList(REMINDERS_FILTERED);
        }

        adapter = new ReminderAdapter(this, remindersFiltered);

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CREATE_REMINDER:
                if (resultCode == RemindersActivity.RESULT_OK) {
                    reminders = Reminder.all();
                    onSearchTextChanged("");
                }
                break;
        }
    }

    @Override
    protected void onSearchTextChanged(String query) {
        remindersFiltered.clear();

        String[] queryByWords = query.trim().toLowerCase().split("\\s+");

        for (Reminder reminder : reminders) {
            String content = (reminder.getText()).toLowerCase();

            int numberOfMatches = 0;

            for (String word : queryByWords) {
                if (content.contains(word)) {
                    numberOfMatches++;
                }
            }

            if (numberOfMatches > 0) {
                remindersFiltered.add(reminder);
            }
        }

        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(REMINDERS_FILTERED, (ArrayList<Reminder>) remindersFiltered);
        super.onSaveInstanceState(outState);
    }
}
