package com.example.diabetesmanagement.data;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.diabetesmanagement.MainActivity;
import com.example.diabetesmanagement.R;
import com.example.diabetesmanagement.receiver.GoalReminderPublisher;
import com.example.diabetesmanagement.receiver.MorningReminderPublisher;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class GoalReminder {

    public GoalReminder(){

    }

    public static void scheduleCheck(Context context, ArrayList<Goal> goals){
        Date now = Calendar.getInstance().getTime();
        boolean anyProgress = false;
        ArrayList<Goal> unmetGoals = new ArrayList<>();
        for (Goal g:goals) {
            if (g.resetDailyYet(now))
                g.resetDaily();
            if (g.resetWeeklyYet(now))
                g.resetWeekly();
            if (g.getDailyProgress() > 0
                    || g.getMealsEaten() > 0
                    || g.getSnacksEaten() > 0)
                anyProgress = true;
            if (!g.dailyGoalMet())
                unmetGoals.add(g);
        }
        if (unmetGoals.size() > 0)
            scheduleNotification(context, unmetGoals, anyProgress);
    }

    public static void scheduleNotification(Context context, ArrayList<Goal> goals, Boolean anyProgress) {
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
            notificationManager.notify(1, getNotification(context, goals, anyProgress));
    }

    public static Notification getNotification(Context context, ArrayList<Goal> goals, boolean anyProgress) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, context.getString(R.string.channel_id));
        builder.setContentTitle("Diabetes Helper");
        StringBuilder s;
        if (anyProgress) {
            s = new StringBuilder("Great start towards your goals for the day.  Keep it up!");
        }
        else{
            s = new StringBuilder("Hey, have you made any progress on your goals today?");
        }
        builder.setContentText(s);

        s.append("\nHere's how you're doing on those goals:");
        for (Goal goal:goals){
            s.append("\n");
            s.append(goal.getType());
            s.append(" goal progress: ");
            if (goal.getType().equals(Goal.ACTIVITY)) {
                s.append(goal.getDailyProgress());
                s.append(" of ");
                s.append(goal.getDailyAmount());
                s.append(" minutes");
            }
            else if (goal.getType().contains("Medication")) {
                s.append(goal.getDailyProgress());
                s.append(" of ");
                s.append(goal.getDailyAmount());
                s.append(" doses taken");
            }
            else if (goal.getType().equals(Goal.FOODMEAL)) {
                s.append(goal.getMealsEaten());
                s.append(" of ");
                s.append(goal.getMealAmount());
                s.append(" meals eaten");
            }
            else if (goal.getType().equals(Goal.FOODSNACK)) {
                s.append(goal.getSnacksEaten());
                s.append(" of ");
                s.append(goal.getSnackAmount());
                s.append(" snacks eaten");
            }
            else {
                s.append(goal.getDailyProgress());
                s.append(" of ");
                s.append(goal.getDailyAmount());
                s.append(" log entries");
            }
        }
        builder.setStyle(new NotificationCompat.BigTextStyle().bigText(s.toString()));

        builder.setSmallIcon(R.drawable.ic_notification);
        builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.buddy_icon));
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("reminder", true);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 1, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        builder.setContentIntent(pendingIntent);
        builder.setPriority(NotificationCompat.PRIORITY_HIGH);
        builder.setDefaults(Notification.DEFAULT_VIBRATE);
        builder.setAutoCancel(true);
        return builder.build();
    }

    public static void setGoalAlarm(Context context){
        Intent goalIntent = new Intent(context, GoalReminderPublisher.class);
        Log.d("GOALCHECK", "In setGoalAlarm");
        //if (PendingIntent.getBroadcast(context, 2, goalIntent, PendingIntent.FLAG_NO_CREATE) == null) {
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 2, goalIntent, PendingIntent.FLAG_CANCEL_CURRENT);
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Calendar c = Calendar.getInstance();
            c.set(Calendar.HOUR_OF_DAY, MainActivity.GOAL_ALARM);
            c.set(Calendar.MINUTE, 0);
            if (c.getTimeInMillis() < Calendar.getInstance().getTimeInMillis())
                c.add(Calendar.DATE, 1);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
            else
                alarmManager.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
            Log.d("GOALCHECK", "Goalcheck scheduled for " + (c.get(Calendar.DAY_OF_YEAR) -
                    Calendar.getInstance().get(Calendar.DAY_OF_YEAR)) + " days from now at " + c.get(Calendar.HOUR_OF_DAY)
                    + ":" + c.get(Calendar.MINUTE));
        //}


        /**including setMorningReminder here, since they should both be scheduled at the same time anyways
         * A proper rewrite would have a separate "scheduleAlarms" method that calls each one in the method body
         * along with all the user's manual reminders
         * but I'm lazy and this is the last feature being added
         */
        setMorningReminder(context);
    }

    public static void setMorningReminder(Context context){
        Intent goalIntent = new Intent(context, MorningReminderPublisher.class);
        Log.d("GOALCHECK", "In setMorningAlarm");
        //if (PendingIntent.getBroadcast(context, 10, goalIntent, PendingIntent.FLAG_NO_CREATE) == null) {
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 10, goalIntent, PendingIntent.FLAG_CANCEL_CURRENT);
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Calendar c = Calendar.getInstance();
            c.set(Calendar.HOUR_OF_DAY, MainActivity.MORNING_ALARM);
            c.set(Calendar.MINUTE, 0);
            if (c.getTimeInMillis() < Calendar.getInstance().getTimeInMillis())
                c.add(Calendar.DATE, 1);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
            else
                alarmManager.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
            Log.d("GOALCHECK", "Morning check scheduled for " + (c.get(Calendar.DAY_OF_YEAR) -
                    Calendar.getInstance().get(Calendar.DAY_OF_YEAR)) + " days from now at " + c.get(Calendar.HOUR_OF_DAY)
                    + ":" + c.get(Calendar.MINUTE));
        //}
    }

    public static void showDailyGoalMet(Context context, Goal g){
        if (g.getDailyAmount() > 0 && !g.getType().equals(Goal.ACTIVITY)){
            Log.d("GOALMET", "Daily amount > 0");
            if (g.getDailyProgress() == g.getDailyAmount()){

                NotificationCompat.Builder builder = new NotificationCompat.Builder(context, context.getString(R.string.channel_id));
                builder.setContentTitle("Diabetes Helper");
                builder.setContentText("Daily goal met!");
                builder.setStyle(new NotificationCompat.BigTextStyle().bigText("Great job! You've met your " + g.getType().toLowerCase() + " goal for today!"));
                //builder.setContentText();
                builder.setSmallIcon(R.drawable.ic_notification);
                builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.buddy_icon));
                builder.setAutoCancel(true);
                builder.setPriority(NotificationCompat.PRIORITY_HIGH);
                builder.setDefaults(Notification.DEFAULT_SOUND);
                builder.setVibrate(new long[]{0});

                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
                notificationManager.notify(4, builder.build());
            }
        }
        else if (g.getMealAmount() > 0){
            Log.d("GOALMET", "Meal amount > 0");
            if (g.getMealsEaten() == g.getMealAmount()){
                NotificationCompat.Builder builder = new NotificationCompat.Builder(context, context.getString(R.string.channel_id));
                builder.setContentTitle("Diabetes Helper");
                builder.setContentText("Daily goal met!");
                builder.setStyle(new NotificationCompat.BigTextStyle().bigText("Great job! You've met your " + g.getType().toLowerCase() + " goal for today!"));
                builder.setSmallIcon(R.drawable.ic_notification);
                builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.buddy_icon));
                builder.setAutoCancel(true);
                builder.setPriority(NotificationCompat.PRIORITY_HIGH);
                builder.setDefaults(Notification.DEFAULT_SOUND);
                builder.setVibrate(new long[]{0});

                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
                notificationManager.notify(4, builder.build());
            }
        }
        else if (g.getSnackAmount() > 0){
            Log.d("GOALMET", "Snack amount > 0");
            if (g.getSnacksEaten() == g.getSnackAmount()){
                NotificationCompat.Builder builder = new NotificationCompat.Builder(context, context.getString(R.string.channel_id));
                builder.setContentTitle("Diabetes Helper");
                builder.setContentText("Daily goal met!");
                builder.setStyle(new NotificationCompat.BigTextStyle().bigText("Great job! You've met your " + g.getType().toLowerCase() + " goal for today!"));
                builder.setSmallIcon(R.drawable.ic_notification);
                builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.buddy_icon));
                builder.setAutoCancel(true);
                builder.setPriority(NotificationCompat.PRIORITY_HIGH);
                builder.setDefaults(Notification.DEFAULT_SOUND);
                builder.setVibrate(new long[]{0});

                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
                notificationManager.notify(4, builder.build());
            }
        }
        else if (g.getType().equals(Goal.ACTIVITY)){
            Log.d("GOALMET", "In activity daily notif");
            if (g.dailyGoalMet()) {
                Log.d("GOALMET", "In activity daily notif: goal was met");
                NotificationCompat.Builder builder = new NotificationCompat.Builder(context, context.getString(R.string.channel_id));
                builder.setContentTitle("Diabetes Helper");
                builder.setContentText("Daily goal met!");
                builder.setStyle(new NotificationCompat.BigTextStyle().bigText("Great job! You've met your " + g.getType().toLowerCase() + " goal for today!"));
                builder.setSmallIcon(R.drawable.ic_notification);
                builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.buddy_icon));
                builder.setAutoCancel(true);
                builder.setPriority(NotificationCompat.PRIORITY_HIGH);
                builder.setDefaults(Notification.DEFAULT_SOUND);
                builder.setVibrate(new long[]{0});

                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
                notificationManager.notify(4, builder.build());
            }
        }
        else
            Log.d("GOALMET", "None of the if's were met");
    }

    public static void showWeeklyGoalMet(Context context, Goal g){
        if (g.getWeeklyAmount() > 0 && !g.getType().equals(Goal.FOODMEAL) && !g.getType().equals(Goal.FOODSNACK)){
            if (g.getWeeklyProgress() == g.getWeeklyAmount()){
                NotificationCompat.Builder builder = new NotificationCompat.Builder(context, context.getString(R.string.channel_id));
                builder.setContentTitle("Diabetes Helper");
                builder.setContentText("Weekly goal met!");
                builder.setStyle(new NotificationCompat.BigTextStyle().bigText("You've met your " + g.getType().toLowerCase() + " goal for the week. Fantastic!"));

                //builder.setContentText("You've met your " + g.getType().toLowerCase() + " goal for the week. Fantastic!");
                builder.setSmallIcon(R.drawable.ic_notification);
                builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.buddy_icon));

                /*Intent intent = new Intent(context, MainActivity.class);
                intent.putExtra("reminder", true);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                builder.setContentIntent(pendingIntent);*/
                builder.setAutoCancel(true);
                builder.setPriority(NotificationCompat.PRIORITY_HIGH);
                builder.setDefaults(Notification.DEFAULT_SOUND);
                builder.setVibrate(new long[]{0});

                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
                notificationManager.notify(5, builder.build());
            }
            else if (g.getType().equals(Goal.ACTIVITY)){
                if (g.weeklyGoalMet()) {
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(context, context.getString(R.string.channel_id));
                    builder.setContentTitle("Diabetes Helper");
                    builder.setContentText("Weekly goal met!");
                    builder.setStyle(new NotificationCompat.BigTextStyle().bigText("You've met your " + g.getType().toLowerCase() + " goal for the week. Fantastic!"));

                    //builder.setContentText("You've met your " + g.getType().toLowerCase() + " goal for the week. Fantastic!");
                    builder.setSmallIcon(R.drawable.ic_notification);
                    builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.buddy_icon));

                    builder.setAutoCancel(true);
                    builder.setPriority(NotificationCompat.PRIORITY_HIGH);
                    builder.setDefaults(Notification.DEFAULT_SOUND);
                    builder.setVibrate(new long[]{0});

                    NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
                    notificationManager.notify(5, builder.build());
                }
            }
        }
    }

}
