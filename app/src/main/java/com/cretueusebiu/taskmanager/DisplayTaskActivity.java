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

import com.cretueusebiu.taskmanager.models.Task;

import org.w3c.dom.Text;

public class DisplayTaskActivity extends AppCompatActivity {

    protected Task task;
    protected int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_task);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        task = (Task) getIntent().getSerializableExtra("task");
        position = getIntent().getIntExtra("position", -1);

        TextView title = (TextView) findViewById(R.id.task_title);
        TextView notes = (TextView) findViewById(R.id.task_notes);
        TextView edited = (TextView) findViewById(R.id.edited_at);

        title.setText(task.getTitle());
        notes.setText(task.getNotes());
        edited.setText(edited.getText().toString() + task.getUpdated());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.display_task, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            case R.id.display_task_delete:
                task.delete();

                Intent intent = new Intent();
                intent.putExtra("position", position);
                setResult(2, intent);

                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}