package com.cretueusebiu.taskmanager;

import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

public abstract class AbstractTaskActivity extends AppCompatActivity {

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            case R.id.save:
                if (!save()) {
                    return false;
                }

                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save, menu);
        return true;
    }

    protected abstract boolean save();
}
