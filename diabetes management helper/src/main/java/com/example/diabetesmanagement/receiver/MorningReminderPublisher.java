package com.example.diabetesmanagement.receiver;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.diabetesmanagement.MainActivity;
import com.example.diabetesmanagement.R;
import com.example.diabetesmanagement.data.Goal;
import com.example.diabetesmanagement.data.GoalReminder;
import com.example.diabetesmanagement.data.User;
import com.example.diabetesmanagement.service.FirestoreCallback;
import com.example.diabetesmanagement.service.UserStorage;

import java.util.ArrayList;

public class MorningReminderPublisher extends BroadcastReceiver {

    public void onReceive(final Context context, Intent intent) {
        Log.d("GOALCHECK", "In MorningReminderPublisher");
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        if (isConnected){
            UserStorage.readUser(new FirestoreCallback() {
                @Override
                public void onCallback(User user) {
                    ArrayList<Goal> goals = user.getGoals();

                    NotificationCompat.Builder builder = new NotificationCompat.Builder(context, context.getString(R.string.channel_id));
                    builder.setContentTitle("Diabetes Helper");
                    StringBuilder s = new StringBuilder();
                    s.append("Good morning! Here's a reminder about today's goals.");
                    builder.setContentText(s);
                    String med = "";
                    for (Goal goal:goals){
                        if (goal.getDailyAmount() > 0
                                || goal.getSnackAmount() > 0
                                || goal.getMealAmount() > 0) {
                            s.append("\n");
                            s.append(goal.getType());
                            s.append(": ");
                            if (goal.getType().equals(Goal.ACTIVITY)) {
                                s.append("get ");
                                s.append(goal.getDailyAmount());
                                if (goal.getDailyAmount() == 1)
                                    s.append(" minute of exercise");
                                else
                                    s.append(" minutes of exercise");

                            } else if (goal.getType().contains("Medication")) {
                                med = goal.getType();
                                med = med.substring(med.indexOf(" ")+1);
                                s.append("take ");
                                s.append(goal.getDailyAmount());
                                if (goal.getDailyAmount() == 1)
                                {
                                    s.append(" dose of ");
                                    s.append(med);
                                }
                                else {
                                    s.append(" doses of ");
                                    s.append(med);
                                }
                            } else if (goal.getType().equals(Goal.FOODMEAL)) {
                                s.append("eat ");
                                s.append(goal.getMealAmount());
                                if (goal.getMealAmount() == 1)
                                    s.append(" meal");
                                else
                                    s.append(" meals");
                            } else if (goal.getType().equals(Goal.FOODSNACK)) {
                                s.append("eat ");
                                s.append(goal.getSnackAmount());
                                if (goal.getSnackAmount() == 1)
                                    s.append(" snack");
                                else
                                    s.append(" snacks");
                            } else {
                                s.append("enter ");
                                s.append(goal.getDailyAmount());
                                if (goal.getDailyAmount() == 1)
                                    s.append(" log");
                                else
                                    s.append(" logs");
                            }
                        }
                    }
                    builder.setStyle(new NotificationCompat.BigTextStyle().bigText(s.toString()));

                    builder.setSmallIcon(R.drawable.ic_notification);
                    builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.buddy_icon));
                    Intent intent = new Intent(context, MainActivity.class);
                    intent.putExtra("reminder", true);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    PendingIntent pendingIntent = PendingIntent.getActivity(context, 12, intent, PendingIntent.FLAG_CANCEL_CURRENT);
                    builder.setContentIntent(pendingIntent);
                    builder.setPriority(NotificationCompat.PRIORITY_HIGH);
                    builder.setDefaults(Notification.DEFAULT_VIBRATE);
                    builder.setAutoCancel(true);

                    NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
                    notificationManager.notify(11, builder.build());

                    Log.d("GOALCHECK", "Goals checked and morning notifications sent out.");

                }
            });
        }
        //UserStorage.storeActivity(UserStorage.MORNING);
        GoalReminder.setMorningReminder(context);
    }
}
