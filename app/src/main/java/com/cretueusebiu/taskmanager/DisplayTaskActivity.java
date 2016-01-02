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
    protected boolean edited = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_task);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTask((Task) getIntent().getParcelableExtra("task"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.display_task, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = null;

        switch (item.getItemId()) {
            case android.R.id.home:
                if (edited) {
                    intent = new Intent();
                    intent.putExtra("task", task);
                    setResult(2, intent);
                }
                onBackPressed();
                return true;

            case R.id.display_task_delete:
                task.delete();
                intent = new Intent();
                intent.putExtra("task", task);
                setResult(MainActivity.RESULT_OK, intent);
                onBackPressed();
                return true;

            case R.id.display_task_edit:
                intent = new Intent(this, EditTaskActivity.class);
                intent.putExtra("task", task);
                intent.putExtra("display", true);
                startActivityForResult(intent, 1);
                return false;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == DisplayTaskActivity.RESULT_OK) {
            setTask((Task) data.getParcelableExtra("task"));
        }
    }

    protected void setTask(Task _task) {
        task = _task;
        edited = true;

        TextView title = (TextView) findViewById(R.id.task_title);
        TextView notes = (TextView) findViewById(R.id.task_notes);
        TextView edited = (TextView) findViewById(R.id.edited_at);

        title.setText(task.getTitle());
        notes.setText(task.getNotes());
        edited.setText("Edited: " + task.getUpdated());
    }
}
