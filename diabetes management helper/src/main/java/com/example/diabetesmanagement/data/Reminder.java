package com.example.diabetesmanagement.data;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.example.diabetesmanagement.MainActivity;
import com.example.diabetesmanagement.R;
import com.example.diabetesmanagement.receiver.NotificationPublisher;

import java.io.Serializable;
import java.util.Calendar;

import static java.lang.String.valueOf;

public class Reminder implements Serializable {


    public enum Frequency {DAILY, WEEKLY, NONE}

    private String description;
    private String userID;
    private int requestCode;
    private int hour;
    private int minute;
    private boolean sunday;
    private boolean monday;
    private boolean tuesday;
    private boolean wednesday;
    private boolean thursday;
    private boolean friday;
    private boolean saturday;
    //private boolean[] weekday = new boolean[7]; //sunday = 0, true if active that day
    private int calendarDay;
    private int calendarMonth;
    private int calendarYear;
    private Frequency frequency;
    private boolean enabled;
    private boolean isVisible;


    public Reminder(){
        setDescription("");
        setHour(0);
        setMinute(0);
        //boolean[] w = {false,false,false,false,false,false,false};
        //setWeekday(w);
        setSunday(false);
        setMonday(false);
        setTuesday(false);
        setWednesday(false);
        setThursday(false);
        setFriday(false);
        setSaturday(false);
        setCalendarDay(0);
        setFrequency(Frequency.NONE);
        setEnabled(true);
        setVisible(false);
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public int getRequestCode() {
        return requestCode;
    }

    public void setRequestCode(int requestCode) {
        this.requestCode = requestCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    /*public boolean[] getWeekday() {
        return weekday;
    }

    //public void setWeekday(boolean[] weekday) {
        this.weekday = weekday;
    }*/

    public int getCalendarDay() {
        return calendarDay;
    }

    public void setCalendarDay(int calendarDay) {
        this.calendarDay = calendarDay;
    }

    public int getCalendarMonth() {
        return calendarMonth;
    }

    public void setCalendarMonth(int calendarMonth) {
        this.calendarMonth = calendarMonth;
    }

    public int getCalendarYear() {
        return calendarYear;
    }

    public void setCalendarYear(int calendarYear) {
        this.calendarYear = calendarYear;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }

    public Frequency getFrequency() {
        return frequency;
    }

    public void setFrequency(Frequency frequency) {
        this.frequency = frequency;
    }

    public String frequencyString() {
        switch(getFrequency())
        {
            case DAILY:
                return "Daily";
            case WEEKLY:
                return "Weekly";
            case NONE:
                return "Never";
            default:
                return "Never";
        }
    }

    public boolean isSunday() {
        return sunday;
    }

    public void setSunday(boolean sunday) {
        this.sunday = sunday;
    }

    public boolean isMonday() {
        return monday;
    }

    public void setMonday(boolean monday) {
        this.monday = monday;
    }

    public boolean isTuesday() {
        return tuesday;
    }

    public void setTuesday(boolean tuesday) {
        this.tuesday = tuesday;
    }

    public boolean isWednesday() {
        return wednesday;
    }

    public void setWednesday(boolean wednesday) {
        this.wednesday = wednesday;
    }

    public boolean isThursday() {
        return thursday;
    }

    public void setThursday(boolean thursday) {
        this.thursday = thursday;
    }

    public boolean isFriday() {
        return friday;
    }

    public void setFriday(boolean friday) {
        this.friday = friday;
    }

    public boolean isSaturday() {
        return saturday;
    }

    public void setSaturday(boolean saturday) {
        this.saturday = saturday;
    }

    public String toString()
    {
        String s = "";
        s += "Reminder::  Desc: " + getDescription();
        s += "\n\tFrequency: " + frequencyString();
        s += "\n\tHour: " + getHour();
        s += "\n\tMinute: " + getMinute();
        s += "\n\tCalendarDay: " + getCalendarDay();
        s += "\n\tCalendarMonth: " + getCalendarMonth();
        s += "\n\tCalendarYear: " + getCalendarYear();
        s += "\n\tWeekdays: (S/M/T/W/Th/F/Sa) ";
        s+=  isSunday()+ "/";
        s+=  isMonday()+ "/";
        s+=  isTuesday()+ "/";
        s+=  isWednesday()+ "/";
        s+=  isThursday()+ "/";
        s+=  isFriday()+ "/";
        s+=  isSaturday();
        s += "\n\tEnabled: " + String.valueOf(isEnabled());
        s += "\n\tVisible: " + String.valueOf(isVisible());
        return s;
    }



    public void scheduleNotification(Context context, Notification notification) {
        if (isEnabled()) {
            Intent notificationIntent = new Intent(context, NotificationPublisher.class);
            Bundle notificationBundle = new Bundle();
            notificationBundle.putInt(NotificationPublisher.NOTIFICATION_ID, 1);
            notificationBundle.putParcelable(NotificationPublisher.NOTIFICATION, notification);
            notificationBundle.putSerializable(NotificationPublisher.REMINDER, this);
            notificationIntent.putExtra(NotificationPublisher.BUNDLE, notificationBundle);
            //notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, 1);
            //notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification);
            //notificationIntent.putExtra(NotificationPublisher.REMINDER, this);

            PendingIntent pendingIntent;

            //long futureInMillis = SystemClock.elapsedRealtime() + delay;
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Calendar c = Calendar.getInstance();
            Calendar time = Calendar.getInstance();
            switch (getFrequency()) {
                case NONE:
                    pendingIntent = PendingIntent.getBroadcast(context, getRequestCode(), notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);
                    time.set(getCalendarYear(), getCalendarMonth(), getCalendarDay(),
                            getHour(), getMinute(), 0);
                    if (c.getTimeInMillis() < time.getTimeInMillis()) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                            alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, time.getTimeInMillis(), pendingIntent);
                        else
                            alarmManager.set(AlarmManager.RTC_WAKEUP, time.getTimeInMillis(), pendingIntent);
                        android.util.Log.d("TESTALARM", "Alarm " + getDescription() +
                                " set for " + (time.get(Calendar.MONTH) + 1) + "/" +
                                time.get(Calendar.DAY_OF_MONTH) + "/" + time.get(Calendar.YEAR) + " " +
                                time.get(Calendar.HOUR_OF_DAY) + ":" + time.get(Calendar.MINUTE));
                    }
                    break;
                case DAILY:
                    pendingIntent = PendingIntent.getBroadcast(context, getRequestCode(), notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);
                    time.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH),
                           getHour(), getMinute(), 0);
                    if (c.getTimeInMillis() > time.getTimeInMillis())
                        time.add(Calendar.DATE, 1);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                        alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, time.getTimeInMillis(), pendingIntent);
                    else
                        alarmManager.set(AlarmManager.RTC_WAKEUP, time.getTimeInMillis(), pendingIntent);
                    android.util.Log.d("TESTALARM", "Alarm " + getDescription() +
                            " set for " + (time.get(Calendar.MONTH) + 1) + "/" +
                            time.get(Calendar.DAY_OF_MONTH) + "/" + time.get(Calendar.YEAR) + " " +
                            time.get(Calendar.HOUR_OF_DAY) + ":" + time.get(Calendar.MINUTE));

                    break;
                case WEEKLY:
                    for (int i = 0; i < 7; i++) {

                        pendingIntent = PendingIntent.getBroadcast(context, getRequestCode()+i, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);
                        if (isSunday()){
                            time.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH),
                                   getHour(), getMinute(), 0);
                            //noinspection MagicConstant
                            while (time.get(Calendar.DAY_OF_WEEK) != 1)
                                time.add(Calendar.DATE, 1);
                            if (c.getTimeInMillis() > time.getTimeInMillis())
                                time.add(Calendar.DATE, 7);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                                alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, time.getTimeInMillis(), pendingIntent);
                            else
                                alarmManager.set(AlarmManager.RTC_WAKEUP, time.getTimeInMillis(), pendingIntent);

                            Log.d("TESTALARM", "Alarm " + getDescription() +
                                    " set for " + time.get(Calendar.DAY_OF_WEEK) + " day of week, at " +
                                    time.get(Calendar.HOUR_OF_DAY) + ":" + time.get(Calendar.MINUTE));
                        }
                        if (isMonday()){
                            time.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH),
                                    getHour(), getMinute(), 0);
                            //noinspection MagicConstant
                            while (time.get(Calendar.DAY_OF_WEEK) != 2)
                                time.add(Calendar.DATE, 1);
                            if (c.getTimeInMillis() > time.getTimeInMillis())
                                time.add(Calendar.DATE, 7);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                                alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, time.getTimeInMillis(), pendingIntent);
                            else
                                alarmManager.set(AlarmManager.RTC_WAKEUP, time.getTimeInMillis(), pendingIntent);

                            Log.d("TESTALARM", "Alarm " + getDescription() +
                                    " set for " + time.get(Calendar.DAY_OF_WEEK) + " day of week, at " +
                                    time.get(Calendar.HOUR_OF_DAY) + ":" + time.get(Calendar.MINUTE));
                        }
                        if (isTuesday()){
                            time.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH),
                                    getHour(), getMinute(), 0);
                            //noinspection MagicConstant
                            while (time.get(Calendar.DAY_OF_WEEK) != 3)
                                time.add(Calendar.DATE, 1);
                            if (c.getTimeInMillis() > time.getTimeInMillis())
                                time.add(Calendar.DATE, 7);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                                alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, time.getTimeInMillis(), pendingIntent);
                            else
                                alarmManager.set(AlarmManager.RTC_WAKEUP, time.getTimeInMillis(), pendingIntent);

                            Log.d("TESTALARM", "Alarm " + getDescription() +
                                    " set for " + time.get(Calendar.DAY_OF_WEEK) + " day of week, at " +
                                    time.get(Calendar.HOUR_OF_DAY) + ":" + time.get(Calendar.MINUTE));
                        }
                        if (isWednesday()){
                            time.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH),
                                    getHour(), getMinute(), 0);
                            //noinspection MagicConstant
                            while (time.get(Calendar.DAY_OF_WEEK) != 4)
                                time.add(Calendar.DATE, 1);
                            if (c.getTimeInMillis() > time.getTimeInMillis())
                                time.add(Calendar.DATE, 7);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                                alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, time.getTimeInMillis(), pendingIntent);
                            else
                                alarmManager.set(AlarmManager.RTC_WAKEUP, time.getTimeInMillis(), pendingIntent);

                            Log.d("TESTALARM", "Alarm " + getDescription() +
                                    " set for " + time.get(Calendar.DAY_OF_WEEK) + " day of week, at " +
                                    time.get(Calendar.HOUR_OF_DAY) + ":" + time.get(Calendar.MINUTE));
                        }
                        if (isThursday()){
                            time.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH),
                                    getHour(), getMinute(), 0);
                            //noinspection MagicConstant
                            while (time.get(Calendar.DAY_OF_WEEK) != 5)
                                time.add(Calendar.DATE, 1);
                            if (c.getTimeInMillis() > time.getTimeInMillis())
                                time.add(Calendar.DATE, 7);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                                alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, time.getTimeInMillis(), pendingIntent);
                            else
                                alarmManager.set(AlarmManager.RTC_WAKEUP, time.getTimeInMillis(), pendingIntent);

                            Log.d("TESTALARM", "Alarm " + getDescription() +
                                    " set for " + time.get(Calendar.DAY_OF_WEEK) + " day of week, at " +
                                    time.get(Calendar.HOUR_OF_DAY) + ":" + time.get(Calendar.MINUTE));
                        }
                        if (isFriday()){
                            time.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH),
                                    getHour(), getMinute(), 0);
                            //noinspection MagicConstant
                            while (time.get(Calendar.DAY_OF_WEEK) != 6)
                                time.add(Calendar.DATE, 1);
                            if (c.getTimeInMillis() > time.getTimeInMillis())
                                time.add(Calendar.DATE, 7);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                                alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, time.getTimeInMillis(), pendingIntent);
                            else
                                alarmManager.set(AlarmManager.RTC_WAKEUP, time.getTimeInMillis(), pendingIntent);

                            Log.d("TESTALARM", "Alarm " + getDescription() +
                                    " set for " + time.get(Calendar.DAY_OF_WEEK) + " day of week, at " +
                                    time.get(Calendar.HOUR_OF_DAY) + ":" + time.get(Calendar.MINUTE));
                        }
                        if (isSaturday()){
                            time.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH),
                                    getHour(), getMinute(), 0);
                            //noinspection MagicConstant
                            while (time.get(Calendar.DAY_OF_WEEK) != 7)
                                time.add(Calendar.DATE, 1);
                            if (c.getTimeInMillis() > time.getTimeInMillis())
                                time.add(Calendar.DATE, 7);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                                alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, time.getTimeInMillis(), pendingIntent);
                            else
                                alarmManager.set(AlarmManager.RTC_WAKEUP, time.getTimeInMillis(), pendingIntent);

                            Log.d("TESTALARM", "Alarm " + getDescription() +
                                    " set for " + time.get(Calendar.DAY_OF_WEEK) + " day of week, at " +
                                    time.get(Calendar.HOUR_OF_DAY) + ":" + time.get(Calendar.MINUTE));
                        }
                    }
                    break;

            }
        }
    }

    public Notification getNotification(Context context) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, context.getString(R.string.channel_id));
        builder.setContentTitle("Diabetes Helper");
        builder.setContentText(getDescription());
        builder.setSmallIcon(R.drawable.ic_notification);
        //builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.buddy_icon));
        /**
         * DON'T TURN THE ABOVE ON
         * PendingIntent from getBroadcast in scheduleNotification can't handle a notification with the large icon
         * TransactionTooLarge exception and crash
         *
         * Could rewrite alarm handler to build notification (pass all relevant info through bundle, then build)
         * Just make sure not to include another readUser() if possible
         */
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("reminder", true);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        builder.setContentIntent(pendingIntent);
        builder.setPriority(NotificationCompat.PRIORITY_MAX);
        builder.setDefaults(Notification.DEFAULT_VIBRATE);
        builder.setAutoCancel(true);
        return builder.build();
    }

    public void cancelNotification(Context context)
    {
        Log.d("TESTALARM", "Reminder " + getDescription() + " cancelled");
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, NotificationPublisher.class);
        for (int i = 0; i < 7; i++) {
            PendingIntent sender = PendingIntent.getBroadcast(context, getRequestCode() + i, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            if (sender != null && alarmManager != null)
                alarmManager.cancel(sender);
        }
    }
}
