package com.cretueusebiu.taskmanager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.cretueusebiu.taskmanager.models.Task;

public class CreateTask extends AbstractTaskActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    protected boolean save() {
        EditText titleText = (EditText) findViewById(R.id.create_task_title);
        EditText notesText = (EditText) findViewById(R.id.create_task_notes);

        String title = titleText.getText().toString().trim();
        String notes = notesText.getText().toString().trim();

        if (title.isEmpty() && notes.isEmpty()) {
            return false;
        }

        Task task = Task.create(title, notes);

        Intent intent = new Intent();
        intent.putExtra("task", task);
        setResult(MainActivity.RESULT_OK, intent);

        return true;
    }
}
