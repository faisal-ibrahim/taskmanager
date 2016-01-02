package com.cretueusebiu.taskmanager;

import android.content.Intent;
import android.graphics.LinearGradient;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.cretueusebiu.taskmanager.adapters.TaskAdapter;
import com.cretueusebiu.taskmanager.models.Task;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AbstractActivity {

    private static final int REQUEST_DISPLAY_TASK = 2;
    private static final int REQUEST_EDIT_TASK = 3;
    private static final String TASKS_FILTERED = "tasks_filtered";

    private TaskAdapter adapter;
    private ArrayList<Task> tasks;
    private ArrayList<Task> tasksFiltered;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialize();

        tasks = Task.all(false);

        if (savedInstanceState == null || !isSearchOpened()) {
            tasksFiltered = new ArrayList<Task>(tasks);
        } else {
            tasksFiltered = savedInstanceState.getParcelableArrayList(TASKS_FILTERED);
        }

        adapter = new TaskAdapter(this, tasksFiltered);
        ListView listView = (ListView) findViewById(R.id.tasks_list_view);
        listView.setAdapter(adapter);

        registerForContextMenu(listView);
        listView.setOnItemClickListener(listViewListener);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_DISPLAY_TASK:
                if (resultCode == MainActivity.RESULT_OK){
                    Task task = (Task) data.getParcelableExtra("task");
                    removeTask(task);
                    onSearchTextChanged(searchQuery);
                } else if (resultCode == 2) {
                    Task task = (Task) data.getParcelableExtra("task");
                    replaceTask(task);
                    onSearchTextChanged(searchQuery);
                }
                break;

            case REQUEST_CREATE_TASK:
                if (resultCode == MainActivity.RESULT_OK) {
                    tasks.add(0, (Task) data.getParcelableExtra("task"));
                    onSearchTextChanged("");
                }
                break;

            case REQUEST_EDIT_TASK:
                if (resultCode == MainActivity.RESULT_OK) {
                    Task task = (Task) data.getParcelableExtra("task");
                    replaceTask(task);
                    onSearchTextChanged(searchQuery);
                }
                break;
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Task task = null;

        switch (item.getItemId()) {
            case R.id.task_item_menu_delete:
                task = adapter.getItem((int) info.id);
                task.delete();
                removeTask(task);
                onSearchTextChanged(searchQuery);
                return true;

            case R.id.task_item_menu_edit:
                task = adapter.getItem((int) info.id);
                Intent intent = new Intent(this, EditTaskActivity.class);
                intent.putExtra("task", task);
                startActivityForResult(intent, REQUEST_EDIT_TASK);
                return true;

            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(TASKS_FILTERED, (ArrayList<Task>) tasksFiltered);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onSearchTextChanged(String query) {
        tasksFiltered.clear();

        String[] queryByWords = query.trim().toLowerCase().split("\\s+");

        for (Task task : tasks) {
            String content = (task.getTitle() + " " + task.getNotes()).toLowerCase();

            int numberOfMatches = 0;

            for (String word : queryByWords) {
                if (content.contains(word)) {
                    numberOfMatches++;
                }
            }

            if (numberOfMatches > 0) {
                tasksFiltered.add(task);
            }
        }

        adapter.notifyDataSetChanged();
    }

    protected AdapterView.OnItemClickListener listViewListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            Intent intent = new Intent(adapterView.getContext(), DisplayTaskActivity.class);
            intent.putExtra("task", tasksFiltered.get(position));
            startActivityForResult(intent, REQUEST_DISPLAY_TASK);
        }
    };

    protected void removeTask(Task task) {
        int pos = getTaskPos(task);
        if (pos > -1) {
            tasks.remove(pos);
        }
    }

    protected void replaceTask(Task task) {
        int pos = getTaskPos(task);
        if (pos > -1) {
            tasks.set(pos, task);
        }
    }

    protected int getTaskPos(Task task) {
        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i).getId().equals(task.getId())) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.task_item_menu, menu);
    }
}
