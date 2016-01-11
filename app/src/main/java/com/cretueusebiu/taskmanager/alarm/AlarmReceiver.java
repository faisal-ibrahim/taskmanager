package com.cretueusebiu.taskmanager.alarm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.cretueusebiu.taskmanager.DisplayReminderActivity;
import com.cretueusebiu.taskmanager.R;
import com.cretueusebiu.taskmanager.RemindersActivity;
import com.cretueusebiu.taskmanager.models.Reminder;

import java.util.Calendar;

import static android.media.RingtoneManager.*;

public class AlarmReceiver extends BroadcastReceiver {

    private static final int NOTIFICATION_ID = 1;

    @Override
    public void onReceive(Context context, Intent intent) {
        String id = intent.getStringExtra("reminder_id");

        if (id == null) {
            return;
        }

        Reminder reminder = Reminder.find(id);

        Intent dIntent = new Intent(context, RemindersActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, dIntent, 0);

        NotificationCompat.Builder notification =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_access_time)
                        .setContentTitle("Reminder")
                        .setContentText(reminder.getText())
                        .setContentIntent(contentIntent);

        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(1, notification.build());

//        Intent i = new Intent();
//        i.setAction("DONE");
//        PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
//        notification.addAction(R.drawable.ic_clear, "Done", pi);

        playSound(context);

        Calendar calendar = reminder.getCalendar();

        if (reminder.isAllDay()) {
            calendar.add(Calendar.HOUR_OF_DAY, 3);
        } else {
            switch (reminder.getRepeat()) {
                case Reminder.REPEAT_MINUTE:
                    calendar.add(Calendar.MINUTE, 1);
                    break;

                case Reminder.REPEAT_HOUR:
                    calendar.add(Calendar.HOUR_OF_DAY, 1);
                    break;

                case Reminder.REPEAT_DAY:
                    calendar.add(Calendar.DAY_OF_MONTH, 1);
                    break;
            }
        }

        reminder.setCalendar(calendar);
        reminder.save();
    }

    private void playSound(Context context) {
        try {
            Uri notification = getDefaultUri(TYPE_NOTIFICATION);
            Ringtone r = getRingtone(context, notification);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
