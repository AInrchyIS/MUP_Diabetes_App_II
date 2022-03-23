package com.example.diabetesmanagement.receiver;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.example.diabetesmanagement.MainActivity;
import com.example.diabetesmanagement.R;
import com.example.diabetesmanagement.data.GoalReminder;
import com.example.diabetesmanagement.data.Reminder;
import com.example.diabetesmanagement.data.User;
import com.example.diabetesmanagement.service.FirestoreCallback;
import com.example.diabetesmanagement.service.UserStorage;
import com.google.firebase.auth.FirebaseAuth;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Calendar;

public class BootReceiver extends BroadcastReceiver {
    public void onReceive(final Context context, Intent intent) {

        if (intent.getAction() != null && intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            createNotificationChannel(context);
            Log.d("TESTALARM", "In boot receiver");
            try {
                ArrayList<Reminder> reminders;
                FileInputStream reminderFile = context.openFileInput("reminderList");
                ObjectInputStream objStream = new ObjectInputStream(reminderFile);
                //if (objStream.getClass().isInstanceOf(ArrayList<Reminder>.class)){
                //Object obj = objStream.readObject();
                //if (obj instanceof ArrayList)
                //    if(((ArrayList)obj).get(0) != null && ((ArrayList)obj).get(0) instanceof Reminder)
                        reminders = (ArrayList<Reminder>) objStream.readObject();
                Log.d("TESTALARM", "reminders exist, size " + reminders.size());

                //}
                for (Reminder r:reminders)
                {
                    r.scheduleNotification(context, r.getNotification(context));
                    Log.d("TESTALARM", "Scheduled " + r.getDescription() + " from boot.");
                }
            }
            catch (Exception e)
            {
                Log.d("TESTFILE", "Error in boot reading from file: " + e.getMessage());
            }
            try {
               GoalReminder.setGoalAlarm(context);
               Log.d("BOOTSCHEDULER", "Set the goal alarm in boot");
            }
            catch (Exception e) {
                Log.d("BOOTSCHEDULER", "Error scheduling tasks in boot: " + e.getMessage());
            }
            try {
                UserStorage.readUser(new FirestoreCallback() {
                    @Override
                    public void onCallback(User user) {
                        com.example.diabetesmanagement.data.Log.setPurgeAlarm(context);
                    }
                });
                Log.d("BOOTSCHEDULER", "Set the purge alarm in boot");
            }
            catch (Exception e) {
                Log.d("BOOTSCHEDULER", "Error scheduling purge in boot: " + e.getMessage());
            }
        }
    }

    private void createNotificationChannel(Context context) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = context.getString(R.string.channel_name);
            String description = context.getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(context.getString(R.string.channel_id), name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
