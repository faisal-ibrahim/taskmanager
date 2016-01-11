package com.cretueusebiu.taskmanager.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.cretueusebiu.taskmanager.models.Reminder;

public class ReminderManager {

    protected Context context;
    protected AlarmManager alarmManager;

    public ReminderManager(Context context) {
        this.context = context;
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    }

    public void set(Reminder reminder) {
        int interval = getInterval(reminder);
        PendingIntent intent = getIntent(reminder);
        long triggerAt =  reminder.getCalendar().getTimeInMillis();

        if (reminder.getRepeat() == Reminder.DOES_NOT_REPEAT) {
            alarmManager.set(AlarmManager.RTC_WAKEUP, triggerAt, intent);
        } else {
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, triggerAt, interval, intent);
        }
    }

    public void cancel(Reminder reminder) {
        alarmManager.cancel(getIntent(reminder));
    }

    private int getInterval(Reminder reminder) {
        switch (reminder.getRepeat()) {
            case Reminder.REPEAT_MINUTE:
                return 60000;

            case Reminder.REPEAT_HOUR:
                return 60000 * 60;

            case Reminder.REPEAT_DAY:
                return 60000 * 60 * 60;
        }

        return 0;
    }

    private PendingIntent getIntent(Reminder reminder) {
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra("reminder_id", reminder.getId());
        return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }
}
