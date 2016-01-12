package com.cretueusebiu.taskmanager.backup;

import android.database.sqlite.SQLiteOpenHelper;

import com.cretueusebiu.taskmanager.models.Helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class BackupManager {
    private final String packageName;
    private final SQLiteOpenHelper dbHelper;

    public BackupManager(SQLiteOpenHelper dbHelper, String packageName) {
        this.dbHelper = dbHelper;
        this.packageName = packageName;
    }

    public ArrayList<String> getBackups() {
        ArrayList<String> backups = new ArrayList<String>();

        File dir = new File(getBackupsPath());

        for (final File file : dir.listFiles()) {
            if (!file.isDirectory()) {
                backups.add(file.getName());
            }
        }

        return backups;
    }

    public boolean restore(String filename) throws IOException {
        dbHelper.close();

        String filepath = getBackupsPath() + "/" + filename;

        File newDb = new File(filepath);
        File oldDb = new File(getDbPath());

        if (newDb.exists()) {
            copyFile(new FileInputStream(newDb), new FileOutputStream(oldDb));

            dbHelper.getWritableDatabase().close();

            return true;
        }

        return false;
    }

    public String backup() throws IOException {
        dbHelper.close();

        String filename = System.currentTimeMillis() + "";
        String filepath = getBackupsPath() + "/" + filename;

        File newDb = new File(filepath);
        File oldDb = new File(getDbPath());

        copyFile(new FileInputStream(oldDb), new FileOutputStream(newDb));

        dbHelper.getWritableDatabase().close();

        return filename;
    }

    private String getDbPath() {
        return "/data/data/" + packageName + "/databases/" + Helper.DATABASE_NAME;
    }

    private String getBackupsPath() {
        String path = "/data/data/" + packageName + "/backups";

        File dir = new File(path);

        if (! dir.exists()) {
            dir.mkdir();
        }

        return path;
    }

    private void copyFile(FileInputStream fromFile, FileOutputStream toFile) throws IOException {
        FileChannel fromChannel = null;
        FileChannel toChannel = null;

        try {
            fromChannel = fromFile.getChannel();
            toChannel = toFile.getChannel();
            fromChannel.transferTo(0, fromChannel.size(), toChannel);
        } finally {
            try {
                if (fromChannel != null) {
                    fromChannel.close();
                }
            } finally {
                if (toChannel != null) {
                    toChannel.close();
                }
            }
        }
    }

    public boolean delete(String filename) {
        return new File(getBackupsPath() + "/" + filename).delete();
    }
}
