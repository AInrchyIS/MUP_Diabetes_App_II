package com.example.diabetesmanagement.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.example.diabetesmanagement.MainActivity;
import com.example.diabetesmanagement.data.LogActivity;
import com.example.diabetesmanagement.data.LogBloodSugar;
import com.example.diabetesmanagement.data.LogFood;
import com.example.diabetesmanagement.data.LogInsulin;
import com.example.diabetesmanagement.data.LogMedication;
import com.example.diabetesmanagement.data.LogMood;
import com.example.diabetesmanagement.data.LogWeight;
import com.example.diabetesmanagement.data.User;
import com.example.diabetesmanagement.service.FirestoreCallback;
import com.example.diabetesmanagement.service.UserStorage;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

public class LogPurgePublisher extends BroadcastReceiver {

    public void onReceive(final Context context, Intent intent) {
        Log.d("LOGPURGE", "In LogPurgePublisher");
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        if (isConnected) {
            UserStorage.readUser(new FirestoreCallback() {
                @Override
                public void onCallback(User user) {

                    Calendar bloodSugarCutoff = Calendar.getInstance();
                    bloodSugarCutoff.set(Calendar.HOUR_OF_DAY, 1);
                    bloodSugarCutoff.add(Calendar.WEEK_OF_YEAR, -1 * MainActivity.BLOOD_SUGAR_CUTOFF);
                    Calendar logCutoff = Calendar.getInstance();
                    logCutoff.set(Calendar.HOUR_OF_DAY, 1);
                    logCutoff.add(Calendar.WEEK_OF_YEAR, -1 * MainActivity.LOG_CUTOFF);


                    // save old blood sugar logs and then remove them
                    ArrayList<LogBloodSugar> bloodSugarLogs = new ArrayList<>(user.getLogsBloodSugar());
                    ArrayList<LogBloodSugar> bloodArchive = new ArrayList<>();
                    Iterator itr = bloodSugarLogs.iterator();
                    while (itr.hasNext()) {
                        LogBloodSugar log = (LogBloodSugar) itr.next();
                        if (bloodSugarCutoff.getTimeInMillis() > log.getDateTime().getTime()) {
                            bloodArchive.add(log);
                            itr.remove();
                        }
                    }
                    user.setLogsBloodSugar(bloodSugarLogs);
                    if (bloodArchive.size() > 0)
                        UserStorage.storeBloodSugarLogs(bloodArchive);


                    // save old insulin logs and then remove them
                    ArrayList<LogInsulin> insulinLogs = user.getLogsInsulin();
                    ArrayList<LogInsulin> insulinArchive = new ArrayList<>();
                    itr = insulinLogs.iterator();
                    while (itr.hasNext()) {
                        LogInsulin log = (LogInsulin) itr.next();
                        if (logCutoff.getTimeInMillis() > log.getDateTime().getTime()) {
                            insulinArchive.add(log);
                            itr.remove();
                        }
                    }
                    user.setLogsInsulin(insulinLogs);
                    if (insulinArchive.size() > 0)
                        UserStorage.storeInsulinLogs(insulinArchive);

                    // save old food logs and then remove them
                    ArrayList<LogFood> foodLogs = user.getLogsFood();
                    ArrayList<LogFood> foodArchive = new ArrayList<>();
                    itr = foodLogs.iterator();
                    while (itr.hasNext()) {
                        LogFood log = (LogFood) itr.next();
                        if (logCutoff.getTimeInMillis() > log.getDateTime().getTime()) {
                            foodArchive.add(log);
                            itr.remove();
                        }
                    }
                    user.setLogsFood(foodLogs);
                    if (foodArchive.size() > 0)
                        UserStorage.storeFoodLogs(foodArchive);

                    // save old medication logs and then remove them
                    ArrayList<LogMedication> medicationLogs = user.getLogsMedication();
                    ArrayList<LogMedication> medicationArchive = new ArrayList<>();
                    itr = medicationLogs.iterator();
                    while (itr.hasNext()) {
                        LogMedication log = (LogMedication) itr.next();
                        if (logCutoff.getTimeInMillis() > log.getDateTime().getTime()) {
                            medicationArchive.add(log);
                            itr.remove();
                        }
                    }
                    user.setLogsMedication(medicationLogs);
                    if (medicationArchive.size() > 0)
                        UserStorage.storeMedicationLogs(medicationArchive);

                    // save old activity logs and then remove them
                    ArrayList<LogActivity> activityLogs = user.getLogsActivity();
                    ArrayList<LogActivity> activityArchive = new ArrayList<>();
                    itr = activityLogs.iterator();
                    while (itr.hasNext()) {
                        LogActivity log = (LogActivity) itr.next();
                        if (logCutoff.getTimeInMillis() > log.getDateTime().getTime()) {
                            activityArchive.add(log);
                            itr.remove();
                        }
                    }
                    user.setLogsActivity(activityLogs);
                    if (activityArchive.size() > 0)
                        UserStorage.storeActivityLogs(activityArchive);

                    // save old weight logs and then remove them
                    ArrayList<LogWeight> weightLogs = user.getLogsWeight();
                    ArrayList<LogWeight> weightArchive = new ArrayList<>();
                    itr = weightLogs.iterator();
                    while (itr.hasNext()) {
                        LogWeight log = (LogWeight) itr.next();
                        if (logCutoff.getTimeInMillis() > log.getDateTime().getTime()) {
                            weightArchive.add(log);
                            itr.remove();
                        }
                    }
                    user.setLogsWeight(weightLogs);
                    if (weightArchive.size() > 0)
                        UserStorage.storeWeightLogs(weightArchive);

                    // save old mood logs and then remove them
                    ArrayList<LogMood> moodLogs = user.getLogsMood();
                    ArrayList<LogMood> moodArchive = new ArrayList<>();
                    itr = moodLogs.iterator();
                    while (itr.hasNext()) {
                        LogMood log = (LogMood) itr.next();
                        if (logCutoff.getTimeInMillis() > log.getDateTime().getTime()) {
                            moodArchive.add(log);
                            itr.remove();
                        }
                    }
                    user.setLogsMood(moodLogs);
                    if (moodArchive.size() > 0)
                        UserStorage.storeMoodLogs(moodArchive);

                    UserStorage.storeActivity(UserStorage.PURGE);
                    UserStorage.saveUser(user);
                    //com.example.diabetesmanagement.data.Log.setPurgeAlarm(context);
                }
            });
        }
    }
}
