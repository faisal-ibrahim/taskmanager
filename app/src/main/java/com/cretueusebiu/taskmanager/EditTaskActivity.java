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
import android.widget.TextView;

import com.cretueusebiu.taskmanager.models.Task;

public class EditTaskActivity extends AbstractTaskActivity {

    protected Task task;
    protected boolean display;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        task = (Task) getIntent().getParcelableExtra("task");
        display = getIntent().getBooleanExtra("display", false);

        EditText title = (EditText) findViewById(R.id.edit_task_title);
        EditText notes = (EditText) findViewById(R.id.edit_task_notes);
        title.setText(task.getTitle());
        notes.setText(task.getNotes());
    }

    protected boolean save() {
        EditText titleText = (EditText) findViewById(R.id.edit_task_title);
        EditText notesText = (EditText) findViewById(R.id.edit_task_notes);

        String title = titleText.getText().toString().trim();
        String notes = notesText.getText().toString().trim();

        if (title.isEmpty() && notes.isEmpty()) {
            return false;
        }

        task.setTitle(title);
        task.setNotes(notes);
        task.save();

        Intent intent = new Intent();
        intent.putExtra("task", task);

        if (display) {
            setResult(MainActivity.RESULT_OK, intent);
        } else {
            setResult(DisplayTaskActivity.RESULT_OK, intent);
        }

        return true;
    }
}
