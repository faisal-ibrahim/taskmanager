package com.cretueusebiu.taskmanager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.cretueusebiu.taskmanager.adapters.TaskAdapter;
import com.cretueusebiu.taskmanager.models.Reminder;
import com.cretueusebiu.taskmanager.models.Task;

import java.util.ArrayList;

public class MainActivity extends BaseActivity {

    private static final int REQUEST_DISPLAY_TASK = 2;
    private static final int REQUEST_EDIT_TASK = 3;
    private TaskAdapter adapter;
    private ArrayList<Task> tasks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialize();

        Task.init(this);

        adapter = new TaskAdapter(this, tasks);

        ListView listView = (ListView) findViewById(R.id.tasks_list_view);
        listView.setAdapter(adapter);

        registerForContextMenu(listView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Task task = tasks.get(position);
                Intent intent = new Intent(adapterView.getContext(), DisplayTaskActivity.class);
                intent.putExtra("task", task);
                intent.putExtra("position", position);
                startActivityForResult(intent, REQUEST_DISPLAY_TASK);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CREATE_TASK:
                if (resultCode == MainActivity.RESULT_OK){
                    adapter.insert((Task) data.getSerializableExtra("task"), 0);
                }
                break;

            case REQUEST_DISPLAY_TASK:
                if (resultCode == 2) {
                    int position = data.getIntExtra("position", -1);
                    if (position > -1) {
                        adapter.remove(tasks.get(position));
                    }
                }
                break;

            case REQUEST_EDIT_TASK:
                if (resultCode == 2) {
                    int position = data.getIntExtra("position", -1);
                    Task task = (Task) data.getSerializableExtra("task");
                    tasks.set(position, task);
                    adapter.notifyDataSetChanged();
                }
                break;
        }

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.task_item_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Task task = null;

        switch (item.getItemId()) {
            case R.id.task_item_menu_delete:
                task = adapter.getItem((int) info.id);
                adapter.remove(task);
                task.delete();
                return true;

            case R.id.task_item_menu_edit:
                task = adapter.getItem((int) info.id);

                Intent intent = new Intent(this, EditTaskActivity.class);
                intent.putExtra("task", task);
                intent.putExtra("position", (int) info.id);
                startActivityForResult(intent, REQUEST_EDIT_TASK);

                return true;

            default:
                return super.onContextItemSelected(item);
        }
    }
}
