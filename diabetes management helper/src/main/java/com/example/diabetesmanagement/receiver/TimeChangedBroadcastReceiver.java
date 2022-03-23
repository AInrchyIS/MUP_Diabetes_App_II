package com.example.diabetesmanagement.receiver;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Log;

import com.example.diabetesmanagement.R;
import com.example.diabetesmanagement.data.GoalReminder;
import com.example.diabetesmanagement.data.Reminder;
import com.example.diabetesmanagement.data.User;
import com.example.diabetesmanagement.service.FirestoreCallback;
import com.example.diabetesmanagement.service.UserStorage;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;

public class TimeChangedBroadcastReceiver extends BroadcastReceiver {
    public void onReceive(final Context context, Intent intent) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        if (isConnected) {
            if (intent.getAction() != null && (intent.getAction().equals("android.intent.action.TIME_SET") ||
                    intent.getAction().equals("android.intent.action.TIMEZONE_CHANGED")
                || intent.getAction().equals("android.intent.action.DATE_CHANGED"))) {

                createNotificationChannel(context);
                Log.d("TIMECHANGE", "In time change receiver");
                try {
                    UserStorage.readUser(new FirestoreCallback() {
                        @Override
                        public void onCallback(User user) {
                            ArrayList<Reminder> reminders = user.getReminders();
                            for (Reminder r : reminders) {
                                r.scheduleNotification(context, r.getNotification(context));
                                Log.d("TIMECHANGE", "Scheduled " + r.getDescription() + " from time change.");
                            }

                            com.example.diabetesmanagement.data.Log.setPurgeAlarm(context);
                        }
                    });
                } catch (Exception e) {
                    Log.d("TIMECHANGE", "Error in setting reminders in time change: " + e.getMessage());
                }
                try {
                    GoalReminder.setGoalAlarm(context);
                    Log.d("TIMECHANGE", "Set the goal alarm from time change");
                } catch (Exception e) {
                    Log.d("TIMECHANGE", "Error scheduling tasks in time change: " + e.getMessage());
                }
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
