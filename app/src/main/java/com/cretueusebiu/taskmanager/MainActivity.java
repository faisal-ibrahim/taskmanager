package com.cretueusebiu.taskmanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.cretueusebiu.taskmanager.adapters.TaskAdapter;
import com.cretueusebiu.taskmanager.models.Task;
import com.cretueusebiu.taskmanager.storage.TasksStorage;

import java.util.ArrayList;

public class MainActivity extends BaseActivity {

    private TasksStorage storage;
    private TaskAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialize();

        storage = new TasksStorage(this);

//        storage.add(new Task("title 1", "Notes1..."));
//        storage.add(new Task("title 2", "Notes2..."));

        final ArrayList<Task> tasks = storage.getAll();

        adapter = new TaskAdapter(this, tasks);

        ListView listView = (ListView) findViewById(R.id.tasks_list_view);
        listView.setAdapter(adapter);

        registerForContextMenu(listView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Task task = tasks.get(position);

                Intent intent = new Intent(adapterView.getContext(), DisplayTaskActivity.class);

                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if(resultCode == MainActivity.RESULT_OK){
                Task task = (Task) data.getSerializableExtra("task");
                adapter.add(task);
                storage.add(task);
            }
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

        switch (item.getItemId()) {
            case R.id.task_item_menu_delete:
                Task task = adapter.getItem((int) info.id);
                adapter.remove(task);
                storage.remove(task);
                return true;

            default:
                return super.onContextItemSelected(item);
        }
    }
}
