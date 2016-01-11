package com.cretueusebiu.taskmanager;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.cretueusebiu.taskmanager.models.Reminder;
import com.cretueusebiu.taskmanager.models.Task;

public class DisplayReminderActivity extends AppCompatActivity {

    protected Reminder reminder;
    protected boolean edited = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_reminder);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String id = getIntent().getStringExtra("reminder_id");
        setTask(Reminder.find(id));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.display_reminder, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = null;

        switch (item.getItemId()) {
            case android.R.id.home:
                if (edited) {
                    intent = new Intent();
                    intent.putExtra("reminder", reminder);
                    setResult(2, intent);
                }
                onBackPressed();
                return true;

            case R.id.display_reminder_delete:
                reminder.delete();
                intent = new Intent();
                intent.putExtra("reminder", reminder);
                setResult(RemindersActivity.RESULT_OK, intent);
                onBackPressed();
                return true;

            case R.id.display_reminder_edit:
                intent = new Intent(this, EditReminderActivity.class);
                intent.putExtra("reminder", reminder);
                intent.putExtra("display", true);
                startActivityForResult(intent, 1);
                return false;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == DisplayReminderActivity.RESULT_OK) {
            setTask((Reminder) data.getParcelableExtra("reminder"));
        }
    }

    protected void setTask(Reminder _reminder) {
        reminder = _reminder;
        edited = true;

        TextView text = (TextView) findViewById(R.id.reminder_text);

        text.setText(reminder.getText());
    }

}
