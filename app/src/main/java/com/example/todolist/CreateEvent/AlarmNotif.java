package com.example.todolist.CreateEvent;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import com.example.todolist.R;

public class AlarmNotif extends BroadcastReceiver {
    public static final String ID ="01";
    @Override
    public void onReceive(Context context, Intent intent)
    {
        NotificationCompat.Builder notification = new NotificationCompat.Builder(context, ID)
                .setSmallIcon(R.drawable.ic_stat_event)
                .setContentTitle(CreateEventActivity.title.getText().toString())
                .setContentText(CreateEventActivity.description.getText().toString())
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);;
        notificationManager.notify(001,notification.build());
    }
}
