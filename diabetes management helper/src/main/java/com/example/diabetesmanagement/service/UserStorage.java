package com.example.diabetesmanagement.service;


import android.util.Log;

import androidx.annotation.NonNull;

import com.example.diabetesmanagement.data.Goal;
import com.example.diabetesmanagement.data.LogActivity;
import com.example.diabetesmanagement.data.LogBloodSugar;
import com.example.diabetesmanagement.data.LogFood;
import com.example.diabetesmanagement.data.LogInsulin;
import com.example.diabetesmanagement.data.LogMedication;
import com.example.diabetesmanagement.data.LogMood;
import com.example.diabetesmanagement.data.LogWeight;
import com.example.diabetesmanagement.data.User;
import com.example.diabetesmanagement.data.UserActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class UserStorage {

    public final static String LOGIN = "LOGIN";
    public final static String LAUNCH = "LAUNCH";
    public final static String NOTIFICATION = "NOTIFICATION LAUNCH";
    public final static String LOG = "SAVE LOG";
    public final static String LINK = "LINK CLICKED";
    public final static String MANUAL = "MANUAL OPENED";
    public final static String PURGE = "OLD LOGS MOVED";
    public final static String MORNING = "MORNING NOTIFICATION SENT";
    public final static String EVENING = "EVENING NOTIFICATION SENT";

    //private Context context;
    //private Activity activity;
    public UserStorage()
    {

    }
    /*
    public UserStorage(Context context, Activity activity)
    {
        setActivity(activity);
        setContext(context);
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }
    */
    public static void saveUser(User user)
    {
        /*
        if (user != null && user.getReminders() != null) {
            String userfile = user.getUserName() + userHash;
            FileOutputStream outputStream;
            try {
                outputStream = context.openFileOutput(userfile, Context.MODE_PRIVATE);
                ObjectOutputStream objectOut = new ObjectOutputStream(outputStream);
                objectOut.writeObject(user);
                objectOut.close();
                Log.d("TESTFILE", "Finished writing user to file");
            } catch (Exception e) {
                Log.d("TESTFILE", e.getMessage());
            }
        }
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("userName", user.getUserName());
        editor.putString("userHash", userHash);
        editor.commit();
        */
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null)
        {
            String id = firebaseUser.getUid();
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("users").document(id).set(user);
            Log.d("FBDEBUG", "SAVING USER: " + user.toString());
        }
    }

    public static void storeActivity(String s)
    {

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null)
        {
            UserActivity ua = new UserActivity();
            ua.setActivity(s);
            ua.setTimestamp(new Timestamp(Calendar.getInstance().getTime()));
            String id = firebaseUser.getUid();
            ua.setUid(id);
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("userActivity").document(id).collection(s).document( " " + Calendar.getInstance().getTime()).set(ua);

        }
    }
    public static void storeGoalProgress(Goal goal)
    {

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null)
        {
            String id = firebaseUser.getUid();
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("goalRecords").document(id).collection(goal.getType()).document( "" + Calendar.getInstance().getTime()).set(goal);
            Log.d("FBDEBUG", "SAVING GOAL: " + goal.getType());
        }
    }
    public static void storeBloodSugarLogs(ArrayList<LogBloodSugar> logs)
    {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null)
        {
            String id = firebaseUser.getUid();
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            LogBloodSugar log = logs.get(0);
            Calendar c = Calendar.getInstance();
            //c.setTime(log.getDateTime());
            c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
            c.add(Calendar.MILLISECOND, 1);
            DateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.US);
            Map<String, ArrayList<LogBloodSugar>> logMap = Collections.singletonMap(firebaseUser.getUid(), logs);

            db.collection("userLogs").document(id).collection(log.logType()).document("Logs purged on "  + dateFormat.format(c.getTime())).set(logMap);
            Log.d("FBDEBUG", "SAVING LOG: " + log.logType());
        }
    }

    public static void storeInsulinLogs(ArrayList<LogInsulin> logs)
    {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null)
        {
            String id = firebaseUser.getUid();
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            LogInsulin log = logs.get(0);
            Calendar c = Calendar.getInstance();
            //c.setTime(log.getDateTime());
            c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
            c.add(Calendar.MILLISECOND, 1);
            DateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.US);
            Map<String, ArrayList<LogInsulin>> logMap = Collections.singletonMap(firebaseUser.getUid(), logs);

            db.collection("userLogs").document(id).collection(log.logType()).document("Logs purged on "  + dateFormat.format(c.getTime())).set(logMap);
            Log.d("FBDEBUG", "SAVING LOG: " + log.logType());
        }
    }

    public static void storeFoodLogs(ArrayList<LogFood> logs)
    {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null)
        {
            String id = firebaseUser.getUid();
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            LogFood log = logs.get(0);
            Calendar c = Calendar.getInstance();
            //c.setTime(log.getDateTime());
            c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
            c.add(Calendar.MILLISECOND, 1);
            DateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.US);
            Map<String, ArrayList<LogFood>> logMap = Collections.singletonMap(firebaseUser.getUid(), logs);

            db.collection("userLogs").document(id).collection(log.logType()).document("Logs purged on "  + dateFormat.format(c.getTime())).set(logMap);
            Log.d("FBDEBUG", "SAVING LOG: " + log.logType());
        }
    }

    public static void storeMedicationLogs(ArrayList<LogMedication> logs)
    {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null)
        {
            String id = firebaseUser.getUid();
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            LogMedication log = logs.get(0);
            Calendar c = Calendar.getInstance();
            //c.setTime(log.getDateTime());
            c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
            c.add(Calendar.MILLISECOND, 1);
            DateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.US);
            Map<String, ArrayList<LogMedication>> logMap = Collections.singletonMap(firebaseUser.getUid(), logs);

            db.collection("userLogs").document(id).collection(log.logType()).document("Logs purged on "  + dateFormat.format(c.getTime())).set(logMap);
            Log.d("FBDEBUG", "SAVING LOG: " + log.logType());
        }
    }

    public static void storeActivityLogs(ArrayList<LogActivity> logs)
    {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null)
        {
            String id = firebaseUser.getUid();
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            LogActivity log = logs.get(0);
            Calendar c = Calendar.getInstance();
            c.setTime(log.getDateTime());
            c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
            c.add(Calendar.MILLISECOND, 1);
            DateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.US);
            Map<String, ArrayList<LogActivity>> logMap = Collections.singletonMap(firebaseUser.getUid(), logs);

            db.collection("userLogs").document(id).collection(log.logType()).document("Logs purged on "  + dateFormat.format(c.getTime())).set(logMap);
            Log.d("FBDEBUG", "SAVING LOG: " + log.logType());
        }
    }

    public static void storeWeightLogs(ArrayList<LogWeight> logs)
    {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null)
        {
            String id = firebaseUser.getUid();
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            LogWeight log = logs.get(0);
            Calendar c = Calendar.getInstance();
            //c.setTime(log.getDateTime());
            c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
            c.add(Calendar.MILLISECOND, 1);
            DateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.US);
            Map<String, ArrayList<LogWeight>> logMap = Collections.singletonMap(firebaseUser.getUid(), logs);

            db.collection("userLogs").document(id)
                    .collection(log.logType()).document("Logs purged on "  +
                    dateFormat.format(c.getTime())).set(logMap);
            Log.d("FBDEBUG", "SAVING LOG: " + log.logType());
        }
    }

    public static void storeMoodLogs(ArrayList<LogMood> logs)
    {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null)
        {
            String id = firebaseUser.getUid();
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            LogMood log = logs.get(0);
            Calendar c = Calendar.getInstance();
            //c.setTime(log.getDateTime());
            c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
            c.add(Calendar.MILLISECOND, 1);
            DateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.US);
            Map<String, ArrayList<LogMood>> logMap = Collections.singletonMap(firebaseUser.getUid(), logs);

            db.collection("userLogs").document(id).collection(log.logType()).document("Logs purged on "  + dateFormat.format(c.getTime())).set(logMap);
            Log.d("FBDEBUG", "SAVING LOG: " + log.logType());
        }
    }
    /*
    public static void readLogs(final String logType, final long startdate, final long enddate, final FirestoreLogCallback myCallBack)
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null)
        {
            String id = user.getUid();
            final CollectionReference collectionReference = db.collection("userLogs").document(id).collection(logType);
            collectionReference
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                ArrayList<com.example.diabetesmanagement.data.Log> logs = new ArrayList<>();
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    if (logType.equals("Activity"))
                                        logs.add(document.toObject(LogActivity.class));
                                    else if (logType.equals("BloodSugar"))
                                        logs.add(document.toObject(LogBloodSugar.class));
                                    else if (logType.equals("Insulin"))
                                        logs.add(document.toObject(LogInsulin.class));
                                    else if (logType.equals("Food"))
                                        logs.add(document.toObject(LogFood.class));
                                    else if (logType.equals("Medication"))
                                        logs.add(document.toObject(LogMedication.class));
                                    else if (logType.equals("Weight"))
                                        logs.add(document.toObject(LogWeight.class));
                                    else if (logType.equals("Mood"))
                                        logs.add(document.toObject(LogMood.class));
                                    //Log.d(TAG, document.getId() + " => " + document.getData());
                                }
                                myCallBack.onCallback(logs, logType, startdate, enddate);
                            } else {
                                //Log.d(TAG, "Error getting documents: ", task.getException());
                            }
                        }
                    });
        }
    }*/
    /*
    public User loadUser(String username, String userHash)
    {
        User user = null;
        String[] fileList = context.fileList();
        boolean fileExists = false;
        for (String s:fileList)
        {
            if (s.equals(username + userHash))
                fileExists = true;
        }
        if (fileExists){
            try {
                FileInputStream userFile = context.openFileInput(username + userHash);
                ObjectInputStream objStream = new ObjectInputStream(userFile);
                user = (User) objStream.readObject();
                Log.d("TESTALARM", "User exists");
            }
            catch (Exception e)
            {
                Log.d("TESTFILE", "Error in boot reading from file: " + e.getMessage());
            }
        }
        return user;
    }

    public void saveUserList(String username, String userHash)
    {
        try {
            FileOutputStream outputStream;
            boolean fileExists = false;
            for (String s:context.fileList())
            {
                if (s.equals(context.getString(R.string.userListFile)))
                    fileExists = true;
            }
            if (!fileExists) {
                    outputStream = context.openFileOutput(context.getString(R.string.userListFile), Context.MODE_PRIVATE);
                    outputStream.write((username + userHash).getBytes());
                    outputStream.close();
                    Log.d("TESTFILE", "Created userList file.");
            }
            else {
                outputStream = context.openFileOutput(context.getString(R.string.userListFile), Context.MODE_APPEND);
                BufferedWriter out = new BufferedWriter(
                        new OutputStreamWriter(outputStream));
                out.newLine();
                out.write(username + userHash);
                Log.d("TESTFILE", "Wrote " + username + userHash);
                out.close();
                Log.d("TESTFILE", "Finished writing user to hash list");
            }
        } catch (Exception e) {
            Log.d("TESTFILE", "Error saving user list: " + e.getMessage());
        }
    }

    public boolean existingUser(String username, String userHash)
    {
        try {
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(context.openFileInput(context.getString(R.string.userListFile))));

            String nameHash;
            while ((nameHash = in.readLine()) != null) {
                Log.d("FILEREAD", "File line: " + nameHash);
                if (nameHash.equals(username + userHash))
                    return true;
            }
        }
        catch (Exception e) {
            Log.d("TESTFILE", "Error checking for existing user " + e.getMessage());
        }
        return false;
    }

    public boolean existingUsername(String username)
    {
        try {
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(context.openFileInput(context.getString(R.string.userListFile))));

            String nameHash;
            while ((nameHash = in.readLine()) != null) {
                Log.d("FILEREAD", "File line: " + nameHash);
                if (nameHash.contains(username))
                    return true;
            }
        }
        catch (Exception e) {
            Log.d("TESTFILE", "Error checking for existing username " + e.getMessage());
        }
        return false;
    }
    */
    public static void readUser(final FirestoreCallback myCallBack) {
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null)
        {
            final String id = user.getUid();
            final DocumentReference docRef = db.collection("users").document(id);
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            /*Calendar c = Calendar.getInstance();
                            HashMap<String, String> errorMap = new HashMap<>();
                            errorMap.put(c.getTime().toString(), "Read successfully");
                            db.collection("readSuccess").document(id).collection((c.get(Calendar.MONTH) + 1) + "-" + c.get(Calendar.DATE)).document(c.getTime().toString()).set(errorMap);
                            */
                            Log.d("FIREBASELOGIN", "DocumentSnapshot data: " + document.getData());
                           User user1 = document.toObject(User.class);
                           myCallBack.onCallback(user1);
                        } else {
                            Log.d("FIREBASELOGIN", "No such document");
                        }
                    } else {
                        Log.d("FIREBASELOGIN", "get failed with ", task.getException());
                        Calendar c = Calendar.getInstance();
                        HashMap<String, String> errorMap = new HashMap<>();
                        errorMap.put(c.getTime().toString(), task.getException().toString());
                        db.collection("readFailure").document(id).collection((c.get(Calendar.MONTH) + 1) + "-" + c.get(Calendar.DATE)).document(c.getTime().toString()).set(errorMap);
                    }
                }
            });
        }
    }

    //public void scheduleGoalReset(String username)
}
