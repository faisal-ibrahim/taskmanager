package com.cretueusebiu.taskmanager;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.cretueusebiu.taskmanager.adapters.ReminderAdapter;
import com.cretueusebiu.taskmanager.alarm.ReminderManager;
import com.cretueusebiu.taskmanager.models.Reminder;
import com.cretueusebiu.taskmanager.models.Task;

import java.util.ArrayList;

public class RemindersActivity extends AbstractActivity {

    private static final String REMINDERS_FILTERED = "reminders_filtered";
    private static final int REQUEST_EDIT_REMINDER = 2;
    private static final int REQUEST_DISPLAY_REMINDER = 6;
    private ReminderAdapter adapter;
    private ArrayList<Reminder> reminders;
    private ArrayList<Reminder> remindersFiltered;
    private ReminderManager reminderManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminders);

        initialize();

        reminders = Reminder.all();

        reminderManager = new ReminderManager(this);

        if (savedInstanceState == null || !isSearchOpened()) {
            remindersFiltered = new ArrayList<Reminder>(reminders);
        } else {
            remindersFiltered = savedInstanceState.getParcelableArrayList(REMINDERS_FILTERED);
        }

        adapter = new ReminderAdapter(this, remindersFiltered);

        ListView listView = (ListView) findViewById(R.id.reminders_list_view);
        listView.setAdapter(adapter);

        registerForContextMenu(listView);
        listView.setOnItemClickListener(listViewListener);
    }

    protected AdapterView.OnItemClickListener listViewListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
//            Reminder reminder = remindersFiltered.get(position);
            Intent intent = new Intent(adapterView.getContext(), DisplayReminderActivity.class);
            intent.putExtra("reminder_id", remindersFiltered.get(position).getId());
            startActivityForResult(intent, REQUEST_DISPLAY_REMINDER);
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_DISPLAY_REMINDER:
                if (resultCode == RemindersActivity.RESULT_OK){
                    Reminder reminder = (Reminder) data.getParcelableExtra("reminder");
                    removeReminder(reminder);
                    reminderManager.cancel(reminder);
                    onSearchTextChanged(searchQuery);
                } else if (resultCode == 2) {
                    Reminder reminder = (Reminder) data.getParcelableExtra("reminder");
                    replaceReminder(reminder);
                    onSearchTextChanged(searchQuery);
                }
                break;

            case REQUEST_CREATE_REMINDER:
                if (resultCode == RemindersActivity.RESULT_OK) {
                    reminders = Reminder.all();
                    onSearchTextChanged("");
                }
                break;

            case REQUEST_EDIT_REMINDER:
                if (resultCode == RemindersActivity.RESULT_OK) {
                    Reminder reminder = (Reminder) data.getParcelableExtra("reminder");
                    replaceReminder(reminder);
                    reminderManager.set(reminder);
                    onSearchTextChanged(searchQuery);
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

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Reminder reminder = adapter.getItem((int) info.id);

        switch (item.getItemId()) {
            case R.id.reminder_item_menu_delete:
                reminder.delete();
                removeReminder(reminder);
                reminderManager.cancel(reminder);
                onSearchTextChanged(searchQuery);
                return true;

            case R.id.reminder_item_menu_edit:
                Intent intent = new Intent(this, EditReminderActivity.class);
                intent.putExtra("reminder", reminder);
                startActivityForResult(intent, REQUEST_EDIT_REMINDER);
                return true;

            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.reminder_item_menu, menu);
    }

    protected void removeReminder(Reminder reminder) {
        int pos = getReminderPos(reminder);
        if (pos > -1) {
            reminders.remove(pos);
        }
    }

    protected void replaceReminder(Reminder reminder) {
        int pos = getReminderPos(reminder);
        if (pos > -1) {
            reminders.set(pos, reminder);
        }
    }

    protected int getReminderPos(Reminder reminder) {
        for (int i = 0; i < reminders.size(); i++) {
            if (reminders.get(i).getId().equals(reminder.getId())) {
                return i;
            }
        }
        return -1;
    }
}
