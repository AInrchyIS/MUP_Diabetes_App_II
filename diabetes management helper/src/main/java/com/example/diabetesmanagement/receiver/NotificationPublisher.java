package com.example.diabetesmanagement.receiver;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.core.app.NotificationManagerCompat;

import com.example.diabetesmanagement.data.Reminder;

//import com.example.diabetesmanagement.data.Reminder;

public class NotificationPublisher extends BroadcastReceiver {

    public static String NOTIFICATION_ID = "notification-id";
    public static String NOTIFICATION = "notification";
    public static String REMINDER = "reminder";
    public static String BUNDLE = "bundle";

    public void onReceive(Context context, Intent intent) {
        Log.d("TESTALARM", "Alarm went off");
        Bundle bundle = intent.getBundleExtra(BUNDLE);
        if (bundle != null) {
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);


            Reminder reminder = (Reminder) bundle.getSerializable(REMINDER);
            Log.d("TESTALARM", "Is reminder null? " + (reminder == null));

            Notification notification = bundle.getParcelable(NOTIFICATION);
            Log.d("TESTALARM", "Is notification null? " + (notification == null));

            int id = bundle.getInt(NOTIFICATION_ID, 0);
            notificationManager.notify(id, notification);
            Log.d("TESTALARM", "NOTIFICATION SHOULD BE OUT");

            reminder.scheduleNotification(context, reminder.getNotification(context));
        }
        else
            Log.d("TESTALARM", "intent has no extras");

    }


}