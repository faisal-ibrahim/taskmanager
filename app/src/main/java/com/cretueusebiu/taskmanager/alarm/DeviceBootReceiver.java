package com.cretueusebiu.taskmanager.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.cretueusebiu.taskmanager.models.Reminder;

import java.util.ArrayList;
import java.util.Calendar;

public class DeviceBootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            ArrayList<Reminder> reminders = Reminder.all();

            ReminderManager reminderManager = new ReminderManager(context);

            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(System.currentTimeMillis());

            for (Reminder reminder : reminders) {
                if (reminder.getCalendar().getTimeInMillis() < cal.getTimeInMillis()) {
                    cal.add(Calendar.MINUTE, 3);
                    reminder.setCalendar(cal);
                    reminder.save();
                }

                reminderManager.set(reminder);
            }
        }
    }
}