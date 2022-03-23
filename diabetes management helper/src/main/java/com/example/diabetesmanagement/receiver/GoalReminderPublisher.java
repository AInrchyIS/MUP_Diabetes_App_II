package com.example.diabetesmanagement.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.example.diabetesmanagement.data.GoalReminder;
import com.example.diabetesmanagement.data.User;
import com.example.diabetesmanagement.service.FirestoreCallback;
import com.example.diabetesmanagement.service.UserStorage;

public class GoalReminderPublisher extends BroadcastReceiver {

    public void onReceive(final Context context, Intent intent) {
        Log.d("GOALCHECK", "In GoalReminderPublisher");
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        if (isConnected) {
            UserStorage.readUser(new FirestoreCallback() {
                @Override
                public void onCallback(User user) {
                    GoalReminder.scheduleCheck(context, user.getGoals());
                    UserStorage.saveUser(user);
                    Log.d("GOALCHECK", "Goals checked and notifications sent out.");

                }
            });
        }
        //UserStorage.storeActivity(UserStorage.EVENING);
        GoalReminder.setGoalAlarm(context);
    }
}
