package com.cretueusebiu.taskmanager;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

import com.cretueusebiu.taskmanager.adapters.BackupAdapter;
import com.cretueusebiu.taskmanager.adapters.TaskAdapter;

import java.util.ArrayList;

public class BackupActivity extends AbstractActivity {

    private BackupAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backup);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        floatingMenuEnabled = false;

        initialize();

        ArrayList<String> backups = new ArrayList<String>();
        backups.add("/sd/backups/backup1");
        backups.add("/sd/backups/backup2");
        backups.add("/sd/backups/backup3");

        adapter = new BackupAdapter(this, backups);
        ListView listView = (ListView) findViewById(R.id.backup_list);
        listView.setAdapter(adapter);
    }
}
