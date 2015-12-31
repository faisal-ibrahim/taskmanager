package com.cretueusebiu.taskmanager;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.cretueusebiu.taskmanager.models.Task;
import com.cretueusebiu.taskmanager.storage.TasksStorage;
import com.github.clans.fab.FloatingActionMenu;

public class CreateTask extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            case R.id.save:
                if (!saveTask()) {
                    return false;
                }

                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private boolean saveTask() {
        EditText titleText = (EditText) findViewById(R.id.create_task_title);
        EditText notesText = (EditText) findViewById(R.id.create_task_notes);

        String title = titleText.getText().toString().trim();
        String notes = notesText.getText().toString().trim();

        if (title.isEmpty() && notes.isEmpty()) {
            return false;
        }

        Task task = new Task(title, notes);

        Intent intent = new Intent();

        intent.putExtra("task", task);

        setResult(MainActivity.RESULT_OK, intent);

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save, menu);
        return true;
    }
}
