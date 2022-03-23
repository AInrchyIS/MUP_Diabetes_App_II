package com.example.diabetesmanagement.data;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.example.diabetesmanagement.receiver.LogPurgePublisher;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Log implements Serializable {

    private Date dateTime;
    private Date dateTimeEntered;
    private String userID;
    //private String notes;

    public Log()
    {
        setDateTime(Calendar.getInstance().getTime());
        //setNotes("");
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public Date getDateTimeEntered() {
        return dateTimeEntered;
    }

    public void setDateTimeEntered(Date dateTimeEntered) {
        this.dateTimeEntered = dateTimeEntered;
    }

    /*public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }*/

    public String logType()
    {
        return "none";
    }

    public static void setPurgeAlarm(Context context){
        Intent purgeIntent = new Intent(context, LogPurgePublisher.class);
        android.util.Log.d("LOGPURGE", "In setPurgeAlarm");

        if (PendingIntent.getBroadcast(context, 7, purgeIntent, PendingIntent.FLAG_NO_CREATE) == null) {
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 7, purgeIntent, PendingIntent.FLAG_CANCEL_CURRENT);
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

            Calendar c = Calendar.getInstance();
            c.set(Calendar.HOUR_OF_DAY, 3);
            int modifier = 10;
            if (FirebaseAuth.getInstance().getCurrentUser() != null)
            {
                int hash = FirebaseAuth.getInstance().getCurrentUser().getUid().hashCode();
                modifier = hash % 10;
            }
            c.set(Calendar.MINUTE, 5*modifier);
            while (c.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY){
                c.add(Calendar.DATE, 1);
            }
            c.add(Calendar.DATE, (int)Math.ceil(modifier/2.0));


            if (c.getTimeInMillis() > Calendar.getInstance().getTimeInMillis()){
                if (Build.VERSION.SDK_INT == Build.VERSION_CODES.M)
                    alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
                else
                    alarmManager.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
            }


            DateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.US);
            DateFormat timeFormat = new SimpleDateFormat("hh:mm a", Locale.US);

            android.util.Log.d("LOGPURGE", "Next purge on " + dateFormat.format(c.getTime())
                    + " at " + timeFormat.format(c.getTime()));
        }
        else {
            PendingIntent intent = PendingIntent.getBroadcast(context, 7, purgeIntent, PendingIntent.FLAG_NO_CREATE);
            android.util.Log.d("LOGPURGE", "Found intent: " + intent.toString());
        }

    }

}
