package com.cretueusebiu.taskmanager;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.cretueusebiu.taskmanager.adapters.BackupAdapter;
import com.cretueusebiu.taskmanager.backup.BackupManager;
import com.cretueusebiu.taskmanager.models.AbstractModel;

import java.io.IOException;
import java.util.ArrayList;

public class BackupActivity extends AbstractActivity {

    private static String packageName;
    private BackupAdapter adapter;
    private BackupManager backupManager;
    private ArrayList<String> backups;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backup);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        packageName = getApplicationContext().getPackageName();

        floatingMenuEnabled = false;

        initialize();

        backupManager = new BackupManager(AbstractModel.getDbHelper(), packageName);

        backups = backupManager.getBackups();

        adapter = new BackupAdapter(this, backups);
        ListView listView = (ListView) findViewById(R.id.backup_list);
        listView.setAdapter(adapter);

        Button backupBtn = (Button) findViewById(R.id.create_backup);
        backupBtn.setOnClickListener(backupBtnListener);

        registerForContextMenu(listView);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.backup_item_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        String filename = adapter.getItem((int) info.id);

        switch (item.getItemId()) {
            case R.id.backup_item_menu_restore:
                try {
                    backupManager.restore(filename);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return true;

            case R.id.backup_item_menu_delete:
                backups.remove(filename);
                adapter.notifyDataSetChanged();
                backupManager.delete(filename);
                return true;

            default:
                return super.onContextItemSelected(item);
        }
    }

    private View.OnClickListener backupBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try {
                backups.add(0, backupManager.backup());
                adapter.notifyDataSetChanged();
                Toast.makeText(v.getContext(), "Backup created!", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };
}
