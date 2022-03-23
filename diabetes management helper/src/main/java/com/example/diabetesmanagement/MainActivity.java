package com.example.diabetesmanagement;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.Voice;
import android.util.Log;
import android.view.SoundEffectConstants;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.example.diabetesmanagement.constatntClass.ConstantClass;
//import com.example.diabetesmanagement.constatntClass.ProductionModelClass;
import com.example.diabetesmanagement.data.Goal;
import com.example.diabetesmanagement.data.GoalReminder;
import com.example.diabetesmanagement.data.LogActivity;
import com.example.diabetesmanagement.data.LogBloodSugar;
import com.example.diabetesmanagement.data.LogFood;
import com.example.diabetesmanagement.data.LogInsulin;
import com.example.diabetesmanagement.data.LogMedication;
import com.example.diabetesmanagement.data.LogMood;
import com.example.diabetesmanagement.data.LogWeight;
import com.example.diabetesmanagement.data.Medication;
import com.example.diabetesmanagement.data.Reminder;
import com.example.diabetesmanagement.data.User;
import com.example.diabetesmanagement.service.ActivityAlertFragment;
import com.example.diabetesmanagement.service.AlertFragment;
import com.example.diabetesmanagement.service.FirestoreCallback;
import com.example.diabetesmanagement.service.UserStorage;
import com.example.diabetesmanagement.ui.home.HomeFragment;
import com.example.diabetesmanagement.ui.home.LevelFragment;
import com.example.diabetesmanagement.ui.home.LogFragment;
import com.example.diabetesmanagement.ui.home.ResourceFragment;
import com.example.diabetesmanagement.ui.home.SettingsFragment;
import com.example.diabetesmanagement.ui.home.log.entry.ActivityFragment;
import com.example.diabetesmanagement.ui.home.log.entry.BloodSugarFragment;
import com.example.diabetesmanagement.ui.home.log.entry.FoodFragment;
import com.example.diabetesmanagement.ui.home.log.entry.InsulinFragment;
import com.example.diabetesmanagement.ui.home.log.entry.LogBaseline;
import com.example.diabetesmanagement.ui.home.log.entry.MedicineFragment;
import com.example.diabetesmanagement.ui.home.log.entry.MoodFragment;
import com.example.diabetesmanagement.ui.home.log.entry.WeightFragment;
import com.example.diabetesmanagement.ui.home.user.data.management.GoalFragment;
import com.example.diabetesmanagement.ui.home.user.data.management.GoalSetupFragment;
import com.example.diabetesmanagement.ui.home.user.data.management.MedicationFragment;
import com.example.diabetesmanagement.ui.home.user.data.management.MedicationSetupFragment;
import com.example.diabetesmanagement.ui.home.user.data.management.NotificationFragment;
import com.example.diabetesmanagement.ui.home.user.data.management.NotificationSetupFragment;
import com.example.diabetesmanagement.ui.home.user.data.management.PersonalInfoFragment;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements
        HomeFragment.OnFragmentInteractionListener, SettingsFragment.OnFragmentInteractionListener,
        LogBaseline.OnFragmentInteractionListener, PersonalInfoFragment.OnFragmentInteractionListener,
        NotificationFragment.OnFragmentInteractionListener, NotificationSetupFragment.OnFragmentInteractionListener,
        GoalFragment.OnFragmentInteractionListener, GoalSetupFragment.OnFragmentInteractionListener,
        MedicationFragment.OnFragmentInteractionListener, MedicationSetupFragment.OnFragmentInteractionListener,
        ResourceFragment.OnFragmentInteractionListener, LevelFragment.OnFragmentInteractionListener {


    private static final String TAG = "Main Activity";
    //private UserStorage storage;
    private TextView buddyMessage;
    private ImageView buddyIcon;
    private ImageView ttsIndicator;
    private TextToSpeech tts;
    private LinearLayout navigation;
    private String message;
    private String previousMessage;
    private boolean registration = false;
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;
    private boolean ttsOn = true;
    private FirebaseAnalytics mFirebaseAnalytics;
    public final static int GOAL_ALARM = 20; //what hour of day goal reminders are checked and fired
    public final static int MORNING_ALARM = 8; // what hour of day morning reminders are sent
    public final static int BLOOD_SUGAR_CUTOFF = 5; //how many weeks of blood sugar logs to keep saved to user file versus archiving separately
    public final static int LOG_CUTOFF = 2; // how weeks of non blood sugar logs to keep saved to user file


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build();
        firestore.setFirestoreSettings(settings);
        sharedPref = MainActivity.this.getPreferences(Context.MODE_PRIVATE);
        editor = sharedPref.edit();
        if (sharedPref.getBoolean("tts", true))
            ttsOn = true;
        else
            ttsOn = false;

        createNotificationChannel();
        tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    tts.setLanguage(Locale.US);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        tts.setVoice(new Voice(tts.getVoice().getName(), tts.getVoice().getLocale(),
                                Voice.QUALITY_HIGH, Voice.LATENCY_NORMAL, tts.getVoice().isNetworkConnectionRequired(),
                                tts.getVoice().getFeatures()));
                    }
                }
            }
        });
        GoalReminder.setGoalAlarm(getApplicationContext());
        com.example.diabetesmanagement.data.Log.setPurgeAlarm(getApplicationContext());
        //storage = new UserStorage(this, MainActivity.this);
        message = "home";
        setContentView(R.layout.main_activity);
        if (FirebaseAuth.getInstance() != null) {
            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            if (getIntent().getExtras() != null) {
                Bundle b = getIntent().getExtras();
                boolean cameFromNotification = b.getBoolean("reminder", false);
                Log.d("NOTIFICATION", "launch from notification: " + cameFromNotification);
                if (cameFromNotification)
                    UserStorage.storeActivity(UserStorage.NOTIFICATION);
            } else
                Log.d("NOTIFICATION", "No extras found, not a notification launch");

            UserStorage.storeActivity(UserStorage.LAUNCH);
            if (firebaseUser != null && savedInstanceState == null) {
                UserStorage.readUser(new FirestoreCallback() {
                    @Override
                    public void onCallback(User user) {
                        if (!(user.getGender() == null) && !user.getGender().isEmpty()) {
                            Log.d("FBDEBUG", "onCreate: user was initialized already, load main");
                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.main_fragment, HomeFragment.newInstance(user))
                                    .commitNow();
                        } else {
                            Log.d("FBDEBUG", "onCreate: user exists but has not been initialized, load registration");
                            registration = true;
                            navigation.setVisibility(View.GONE);
                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.main_fragment, PersonalInfoFragment.newInstance(user, registration), "pInfo")
                                    .commitNow();

                            setMessage("pInfoSetup");
                        }
                    }
                });

            }
            /*else
            {
                signOut();
            }*/
        } else {
            Log.d("FBDEBUG", "onCreate: FirebaseAuth instance was null");
            Intent intent = new Intent(MainActivity.this, FirebaseAuthActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

        navigation = findViewById(R.id.layout_navigation);

        LinearLayout logGroup = findViewById(R.id.group_log_button);
        ImageButton logButton = findViewById(R.id.imageButton_log);
        logButton.setOnClickListener(logNavListener);
        logGroup.setOnClickListener(logNavListener);
        LinearLayout homeGroup = findViewById(R.id.group_home_button);
        ImageButton homeButton = findViewById(R.id.imageButton_home);
        homeButton.setOnClickListener(homeNavListener);
        homeGroup.setOnClickListener(homeNavListener);
        LinearLayout settingsGroup = findViewById(R.id.group_settings_button);
        ImageButton settingsButton = findViewById(R.id.imageButton_settings);
        settingsButton.setOnClickListener(settingsNavListener);
        settingsGroup.setOnClickListener(settingsNavListener);

        buddyMessage = findViewById(R.id.buddy_message);
        buddyMessage.setOnClickListener(buddyMessageListener);
        buddyIcon = findViewById(R.id.buddy_icon);
        buddyIcon.setOnClickListener(buddyIconListener);
        ttsIndicator = findViewById(R.id.tts_indicator);
        if (!ttsOn)
            ttsIndicator.setImageResource(R.drawable.ic_volume_off_black_24dp);

        setMessage("help");

//        level_recyclerview = findViewById(R.id.level_recyclerView_id);
//
//        //initialize level arrays
//        level_str = getResources().getStringArray(R.array.levels_name);
//        RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(this, level_str, level_image_icon);
//        level_recyclerview.setAdapter(recyclerViewAdapter);
//        level_recyclerview.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onResume() {
        super.onResume();

        //sharedPref = MainActivity.this.getPreferences(Context.MODE_PRIVATE);
        //editor = sharedPref.edit();
        //Log.d("LOGINCHECK", "Opened editor");


        final FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
        if (fUser == null) {
            //No user signed in
            Log.d("FBDEBUG", "onResume: Firebase user null, start login");
            Intent intent = new Intent(MainActivity.this, FirebaseAuthActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else {
            Log.d("FBDEBUG", "meta data not null");
            Log.d("FBDEBUG", "onResume: Firebase user exists");
            FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
            DocumentReference docIdRef = rootRef.collection("users").document(fUser.getUid());
            ConstantClass.setAuthUserId(fUser.getUid());
            docIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            UserStorage.readUser(new FirestoreCallback() {
                                @Override
                                public void onCallback(User user) {
                                    if (user.getGender() == null || user.getGender().equals("")) {
                                        Log.d("FBDEBUG", "onResume: Firebase user exists, existing user, incomplete registration");
                                        registration = true;
                                        navigation.setVisibility(View.GONE);
                                        getSupportFragmentManager().beginTransaction()
                                                .replace(R.id.main_fragment, PersonalInfoFragment.newInstance(user, registration), "pInfo")
                                                .commitNow();

                                        setMessage("pInfoSetup");
                                    } else {
                                        Log.d("FBDEBUG", "onResume: Firebase user exists, existing user, loading reminders.");
                                        for (Reminder r : user.getReminders()) {
                                            r.cancelNotification(getApplicationContext());
                                            r.scheduleNotification(getApplicationContext(), r.getNotification(getApplicationContext()));
                                        }
                                    }
                                }
                            });
                        } else {
                            Log.d("FBDEBUG", "onResume: Firebase exists, new user according to doc lookup");
                            User newUser = new User(fUser.getUid());
                            //newUser.setUserName(fUser.getEmail());
                            UserStorage.saveUser(newUser);
                            UserStorage.readUser(new FirestoreCallback() {
                                @Override
                                public void onCallback(User user) {
                                    if (user.getGender() == null || user.getGender().equals("")) {
                                        registration = true;
                                        navigation.setVisibility(View.GONE);
                                        getSupportFragmentManager().beginTransaction()
                                                .replace(R.id.main_fragment, PersonalInfoFragment.newInstance(user, registration), "pInfo")
                                                .commitNow();

                                        setMessage("pInfoSetup");
                                    }
                                }
                            });
                        }
                    } else {
                        Log.d("FBDEBUG", "Failed with: ", task.getException());
                    }
                }
            });
        }

    }


    @Override
    public void messageFromHome(final Uri uri) {
        Log.i("TAG", "received communication from child fragment HomeFragment");
        Log.d("DEBUGGING", "At messageFromHome, uri: " + uri.toString());
        UserStorage.readUser(new FirestoreCallback() {
            @Override
            public void onCallback(User user) {
                switch (uri.toString()) {
                    case "bloodSugar":
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.main_fragment, LogBaseline.newInstance("bloodSugar", user), "logEntry")
                                .commitNow();
                        setMessage("bloodSugar");
                        break;
                    case "insulin":
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.main_fragment, LogBaseline.newInstance("insulin", user), "logEntry")
                                .commitNow();
                        setMessage("insulin");
                        break;
                    case "medicine":
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.main_fragment, LogBaseline.newInstance("medicine", user), "logEntry")
                                .commitNow();
                        setMessage("medicine");
                        break;
                    case "food":
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.main_fragment, LogBaseline.newInstance("food", user), "logEntry")
                                .commitNow();
                        setMessage("food");
                        break;
                    case "weight":
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.main_fragment, LogBaseline.newInstance("weight", user), "logEntry")
                                .commitNow();
                        setMessage("weight");
                        break;
                    case "mood":
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.main_fragment, LogBaseline.newInstance("mood", user), "logEntry")
                                .commitNow();
                        setMessage("mood");
                        break;
                    case "activity":
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.main_fragment, LogBaseline.newInstance("activity", user), "logEntry")
                                .commitNow();
                        setMessage("activity");
                        break;
                    case "resources":
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.main_fragment, ResourceFragment.newInstance(), "logEntry")
                                .commitNow();
                        setMessage("resources");
                        break;
                }
            }
        });
    }

    @Override
    public void messageFromResources(Uri uri) {
        if (uri.toString().equals("back")) {
            UserStorage.readUser(new FirestoreCallback() {
                @Override
                public void onCallback(User user) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.main_fragment, HomeFragment.newInstance(user))
                            .commitNow();
                    setMessage("home");
                }
            });
        }
    }

    @Override
    public void messageFromSettings(final Bundle bundle) {
        Log.i("TAG", "received communication from child fragment SettingsFragment");
        String uri = bundle.getString("uri");
        Log.d("DEBUGGING", "At messageFromSettings, uri: " + uri);
        switch (uri) {
            case "Logout":
                signOut();
                /*editor.putLong("lastLogin", 0);
                Log.d("LOGINCHECK", "Setting lastLogin to 0 in Logout");
                editor.commit();
                UserStorage.saveUser(user, userHash);
                user = null;

                Intent intent = new Intent(MainActivity.this, FirebaseAuthActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);*/
                break;
//            case "pInfo":
//                UserStorage.readUser(new FirestoreCallback() {
//                    @Override
//                    public void onCallback(User user) {
//                        getSupportFragmentManager().beginTransaction()
//                                .replace(R.id.main_fragment, LevelFragment.newInstance(user, registration), "pInfo")
//                                .commitNow();
//                        setMessage("pInfoSetup");
//
//                    }
//                });
//                break;

            case "levels":
                UserStorage.readUser(new FirestoreCallback() {
                    @Override
                    public void onCallback(User user) {
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.main_fragment, LevelFragment.newInstance(user, registration), "levels")
                                .addToBackStack(null)
                                .commit();
                        setMessage("levelsSetup");
                    }
                });
                break;
            case "notifications":
                UserStorage.readUser(new FirestoreCallback() {
                    @Override
                    public void onCallback(User user) {
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.main_fragment, NotificationFragment.newInstance("", user, registration), "notifications")
                                .commitNow();
                        setMessage("notificationSetup");
                    }
                });
                break;
            case "goals":
                UserStorage.readUser(new FirestoreCallback() {
                    @Override
                    public void onCallback(User user) {
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.main_fragment, GoalFragment.newInstance("", user, registration), "goals")

                                .commitNow();
                        setMessage("goalsSetup");
                    }
                });
                break;
            case "medications":
                registration = false;
                UserStorage.readUser(new FirestoreCallback() {
                    @Override
                    public void onCallback(User user) {
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.main_fragment, MedicationFragment.newInstance("", user, registration), "notifications")
                                .commitNow();
                        setMessage("medicationList");
                    }
                });
                break;

        }
    }

    @Override
    public void messageFromPersonalInfoFragment(final Bundle bundle) {
        UserStorage.readUser(new FirestoreCallback() {
            @Override
            public void onCallback(User user) {
                user.setAge(bundle.getInt("age"));
                user.setDiabetesType(bundle.getString("diabetesType"));
                if (bundle.getString("gender").equals("Male"))
                    user.setGender(User.MALE);
                else
                    user.setGender(User.FEMALE);

                UserStorage.saveUser(user);
                Log.d("USERSETUP", user.toString());
                if (!registration) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.main_fragment, HomeFragment.newInstance(user))
                            .commitNow();
                    setMessage("pInfoUpdate");
                } else {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.main_fragment, MedicationFragment.newInstance("", user, registration), "notifications")
                            .commitNow();
                    setMessage("medicationList");
                }
            }
        });
        //user.setfName(bundle.getString("fName"));
        //user.setlName(bundle.getString("lName"));

    }

    @Override
    public void messageFromNotificationFragment(final Bundle bundle) {
        UserStorage.readUser(new FirestoreCallback() {
            @Override
            public void onCallback(User user) {
                boolean newNotification = bundle.getBoolean("newNotification");
                if (bundle.getString("button") != null && bundle.getString("button").equals("next")) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.main_fragment, GoalFragment.newInstance("", user, registration), "goals")
                            .commitNow();
                    setMessage("goalsSetup");
                } else if (bundle.getString("button") != null && bundle.getString("button").equals("back")) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.main_fragment, MedicationFragment.newInstance("", user, registration), "notifications")
                            .commitNow();
                    setMessage("medicationList");
                } else {
                    if (newNotification)
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.main_fragment, NotificationSetupFragment.newInstance(true, user,
                                        null, registration)).commitNow();
                    else
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.main_fragment, NotificationSetupFragment.newInstance(false, user,
                                        (Reminder) bundle.getSerializable("notification"), registration)).commitNow();
                }
            }
        });

    }

    @Override
    public void messageFromNotificationSetupFragment(final Bundle bundle) {

        String button = bundle.getString("button");
        DialogFragment newFragment;
        Log.d("DEBUGGING", "Button pressed: notification " + button);
        switch (button) {
            case "time":
                newFragment = new NotificationSetupFragment.TimePickerFragment();
                newFragment.show(getSupportFragmentManager(), "timePicker");
                break;
            case "date":
                newFragment = new NotificationSetupFragment.DatePickerFragment();
                newFragment.show(getSupportFragmentManager(), "datePicker");
                break;
            case "cancel":
                UserStorage.readUser(new FirestoreCallback() {
                    @Override
                    public void onCallback(User user) {
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.main_fragment, NotificationFragment.newInstance("", user, registration), "notifications")
                                .commitNow();
                        setMessage("notificationSetup");
                    }
                });
                break;
            case "save":
                UserStorage.readUser(new FirestoreCallback() {
                    @Override
                    public void onCallback(User user) {
                        Reminder reminder = (Reminder) bundle.getSerializable("reminder");

                        if (bundle.getBoolean("newNotification")) {
                            reminder.setRequestCode(user.getReminderRequestCodes());
                            user.setReminderRequestCodes(user.getReminderRequestCodes() + 7);
                            user.addReminder(reminder);
                            reminder.scheduleNotification(MainActivity.this, reminder.getNotification(MainActivity.this));
                        } else {
                            reminder.cancelNotification(getApplicationContext());
                            int i = user.removeReminder(bundle.getString("oldDesc"));
                            if (i != -1)
                                user.addReminder(reminder, i);
                            reminder.scheduleNotification(MainActivity.this, reminder.getNotification(MainActivity.this));
                        }
                        UserStorage.saveUser(user);
                        Log.d("DEBUGGING", reminder.toString());
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.main_fragment, NotificationFragment.newInstance("", user, registration), "notifications")
                                .commitNow();
                        setMessage("notificationSetup");
                    }
                });
                break;
        }
    }


    @Override
    public void messageFromLevelFragment(final Bundle bundle) {
        UserStorage.readUser(new FirestoreCallback() {
            @Override
            public void onCallback(User user) {
                if (bundle.getString("button") != null) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.main_fragment, LevelFragment.newInstance(user, registration), "levels")
                            .addToBackStack(null)
                            .commit();
                    setMessage("levelsSetup");
                }
            }
        });
    }

    @Override
    public void messageFromGoalFragment(final Bundle bundle) {
        UserStorage.readUser(new FirestoreCallback() {
            @Override
            public void onCallback(User user) {
                if (bundle.getString("button") != null && bundle.getString("button").equals("next")) {
                    registration = false;
                    navigation.setVisibility(View.VISIBLE);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.main_fragment, HomeFragment.newInstance(user))
                            .commitNow();

                    UserStorage.saveUser(user);
                    setMessage("home");
                } else if (bundle.getString("button") != null && bundle.getString("button").equals("back")) {
                    registration = true;
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.main_fragment, NotificationFragment.newInstance("", user, registration), "notifications")
                            .commitNow();
                    setMessage("notificationSetup");
                } else {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.main_fragment, GoalSetupFragment.newInstance(user,
                                    (Goal) bundle.getSerializable("goal"), registration)).commitNow();
                    setMessage("goalsSetup");
                }
            }
        });

    }

    @Override
    public void messageFromGoalSetupFragment(final Bundle bundle) {
        UserStorage.readUser(new FirestoreCallback() {
            @Override
            public void onCallback(User user) {
                Goal g = (Goal) bundle.getSerializable("goal");
                if (bundle.getString("button").equals("save")) {
                    int i = user.removeGoal(g.getType());
                    if (i != -1)
                        user.addGoal(g, i);
                    UserStorage.saveUser(user);
                    setMessage("goalsChanged");
                } else {
                    setMessage("goalsSetup");
                }
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.main_fragment, GoalFragment.newInstance("", user, registration), "goals")
                        .commitNow();
            }
        });

    }

    @Override
    public void messageFromMedicationFragment(final Bundle bundle) {
        UserStorage.readUser(new FirestoreCallback() {
            @Override
            public void onCallback(User user) {
                if (bundle.getString("button") != null && bundle.getString("button").equals("next")) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.main_fragment, NotificationFragment.newInstance("", user, registration), "notifications")
                            .commitNow();
                    setMessage("notificationSetup");
                } else if (bundle.getString("button") != null && bundle.getString("button").equals("back")) {
                    registration = true;
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.main_fragment, PersonalInfoFragment.newInstance(user, registration), "pInfo")
                            .commitNow();
                    setMessage("pInfoSetup");
                } else {
                    if (bundle.getBoolean("newMedication")) {
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.main_fragment, MedicationSetupFragment.newInstance(user,
                                        null, registration)).commitNow();
                    } else {
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.main_fragment, MedicationSetupFragment.newInstance(user,
                                        (Medication) bundle.getSerializable("medication"), registration)).commitNow();
                    }
                    setMessage("medicationSetup");
                }
            }
        });

    }

    @Override
    public void messageFromMedicationSetupFragment(final Bundle bundle) {
        UserStorage.readUser(new FirestoreCallback() {
            @Override
            public void onCallback(User user) {
                String button = bundle.getString("button");
                if (button.equals("cancel")) {
                    setMessage("medicationList");
                } else if (button.equals("save")) {

                    boolean newMedication = bundle.getBoolean("newMed");
                    Medication medication = (Medication) bundle.getSerializable("med");
                    Log.d("LISTTEST", "new med: " + newMedication + " name: " + medication.getName());
                    if (newMedication) {
                        user.addMedication(medication);
                        user.addMedicationGoal(medication);
                    } else {
                        Goal g = user.getGoal("Medication: " + bundle.getString("oldMed"));
                        int i = user.removeMedication(bundle.getString("oldMed"));
                        Log.d("LISTTEST", " Old med: " + bundle.getSerializable("oldMed") + " i = " + i);
                        if (i != -1) {
                            user.addMedication(medication, i);
                        }
                        g.setDailyAmount(medication.getDailyDoses());
                        g.setType("Medication: " + medication.getName());
                    }
                    UserStorage.saveUser(user);
                    setMessage("medicationSave");
                }
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.main_fragment, MedicationFragment.newInstance("", user, registration), "goals")
                        .commitNow();
            }
        });

    }

    @Override
    public void messageFromLogBaseline(final Bundle bundle) {

        Log.i("TAG", "received communication from child fragment LogBaseline");
        if (bundle.getString("button") != null) {
            String buttonPressed = bundle.getString("button");
            Log.d("DEBUGGING", "Uri: " + buttonPressed);
            DialogFragment newFragment;
            switch (buttonPressed) {
                case "time":
                    newFragment = new LogBaseline.TimePickerFragment();
                    newFragment.show(getSupportFragmentManager(), "timePicker");
                    break;
                case "date":
                    newFragment = new LogBaseline.DatePickerFragment();
                    newFragment.show(getSupportFragmentManager(), "datePicker");
                    break;
                case "cancel":
                    UserStorage.readUser(new FirestoreCallback() {
                        @Override
                        public void onCallback(User user) {
                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.main_fragment, HomeFragment.newInstance(user))
                                    .commitNow();
                            setMessage("home");
                        }
                    });
                    break;
                case "submit":
                    if (bundle.getBoolean("validInput")) {
                        UserStorage.readUser(new FirestoreCallback() {
                            @Override
                            public void onCallback(final User user) {
                                final com.example.diabetesmanagement.data.Log[] newLog = {null};
                                FirebaseFirestore.getInstance().collection("users")
                                        .document(user.getId()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            User userData = documentSnapshot.toObject(User.class);
                                            boolean inputSuccess = false;
                                            String type = bundle.getString("type");
                                            // looks up the fragment contained within the base log entry fragment, since it contains the type-specific data
                                            Fragment fragment = getSupportFragmentManager().findFragmentByTag("logEntry").getChildFragmentManager().findFragmentByTag(type);

                                            Calendar cal = Calendar.getInstance();
                                            cal.set(bundle.getInt("year"), bundle.getInt("month"), bundle.getInt("day"),
                                                    bundle.getInt("hour"), bundle.getInt("minute"));
                                            Date c = cal.getTime();
                                            Log.d("LOGTYPE", type);

                                            switch (type) {
                                                case "bloodSugar":
                                                    //put all the type-specific data into the main log entry bundle
                                                    bundle.putAll(((BloodSugarFragment) fragment).getLogData());
                                                    if (bundle.getBoolean("validInput")) {
                                                        newLog[0] = new LogBloodSugar();
                                                        newLog[0].setUserID(user.getId());
                                                        newLog[0].setDateTime(c);
                                                        //get the type specific data
                                                        ((LogBloodSugar) newLog[0]).setBloodSugar(bundle.getInt("bloodSugar"));
                                                        ((LogBloodSugar) newLog[0]).setBeforeMeal(bundle.getBoolean("beforeMeal"));
                                                        ((LogBloodSugar) newLog[0]).setAfterMeal(bundle.getBoolean("afterMeal"));
                                                        ((LogBloodSugar) newLog[0]).setBeforeBed(bundle.getBoolean("beforeBed"));
                                                        Log.d("DEBUGGING", "Blood sugar: " + ((LogBloodSugar) newLog[0]).getBloodSugar() +
                                                                "before/after/before: " + ((LogBloodSugar) newLog[0]).isBeforeMeal()
                                                                + " " + ((LogBloodSugar) newLog[0]).isAfterMeal()
                                                                + " " + ((LogBloodSugar) newLog[0]).isBeforeBed());
                                                        user.setCurrentBloodSugar(bundle.getInt("bloodSugar"));
                                                        getSupportFragmentManager().beginTransaction()
                                                                .replace(R.id.main_fragment, HomeFragment.newInstance(user))
                                                                .commitNow();
                                                        inputSuccess = true;
                                                        if (user.getCurrentBloodSugar() > 180 || user.getCurrentBloodSugar() < 70) {
                                                            AlertFragment alert = new AlertFragment(user.getCurrentBloodSugar());
                                                            alert.show(getSupportFragmentManager(), "bloodsugaralert");
                                                        }
                                                    }
                                                    break;
                                                case "insulin":
                                                    bundle.putAll(((InsulinFragment) fragment).getLogData());
                                                    if (bundle.getBoolean("validInput")) {
                                                        newLog[0] = new LogInsulin();
                                                        newLog[0].setUserID(user.getId());
                                                        newLog[0].setDateTime(c);
                                                        //get the type specific data
                                                        ((LogInsulin) newLog[0]).setAmount(bundle.getInt("insulin"));
                                                        //((LogInsulin) newLog).setFood(bundle.getBoolean("food"));
                                                        //((LogInsulin) newLog).setCorrective(bundle.getBoolean("corrective"));
                                                        ((LogInsulin) newLog[0]).setType(bundle.getString("insulinType"));
                                                        Log.d("DEBUGGING", "Insulin: " + ((LogInsulin) newLog[0]).getAmount() +
                                                                //" food/corrective/type: " + ((LogInsulin) newLog).isFood()
                                                                //+ " " + ((LogInsulin) newLog).isCorrective()
                                                                " " + ((LogInsulin) newLog[0]).getType());
                                                        getSupportFragmentManager().beginTransaction()
                                                                .replace(R.id.main_fragment, HomeFragment.newInstance(user))
                                                                .commitNow();
                                                        inputSuccess = true;
                                                    }
                                                    break;
                                                case "medicine":
                                                    bundle.putAll(((MedicineFragment) fragment).getLogData());
                                                    if (bundle.getBoolean("validInput")) {
                                                        newLog[0] = new LogMedication();
                                                        newLog[0].setUserID(user.getId());
                                                        newLog[0].setDateTime(c);
                                                        //get the type specific data
                                                        ((LogMedication) newLog[0]).setMedication(user.getMedication(bundle.getString("medication")));
                                                        Log.d("DEBUGGING", "Med: " + ((LogMedication) newLog[0]).getMedication().getName());
                                                        getSupportFragmentManager().beginTransaction()
                                                                .replace(R.id.main_fragment, HomeFragment.newInstance(user))
                                                                .commitNow();
                                                        inputSuccess = true;
                                                    }
                                                    break;
                                                case "food":
                                                    bundle.putAll(((FoodFragment) fragment).getLogData());
                                                    if (bundle.getBoolean("validInput")) {
                                                        newLog[0] = new LogFood();
                                                        newLog[0].setUserID(user.getId());
                                                        newLog[0].setDateTime(c);
                                                        //get the type specific data
                                                        ((LogFood) newLog[0]).setItemsEaten(bundle.getString("itemsEaten"));
                                                        ((LogFood) newLog[0]).setMeal(bundle.getBoolean("meal"));
                                                        ((LogFood) newLog[0]).setSnack(bundle.getBoolean("snack"));

                                                        //Log.d("DEBUGGING","Food: " + ((LogFood) newLog).getCarbs() +
                                                        //        " items eaten: " + ((LogFood) newLog).getItemsEaten());
                                                        getSupportFragmentManager().beginTransaction()
                                                                .replace(R.id.main_fragment, HomeFragment.newInstance(user))
                                                                .commitNow();
                                                        inputSuccess = true;
                                                    }
                                                    break;
                                                case "activity":
                                                    bundle.putAll(((ActivityFragment) fragment).getLogData());
                                                    if (bundle.getInt("minutesActivity" )< (user.getGoal(Goal.ACTIVITY).getDailyAmount())){
                                                        ActivityAlertFragment alert = new ActivityAlertFragment();
                                                        alert.show(getSupportFragmentManager(), "Activityalert");
                                                    }
                                                    else {
                                                        newLog[0] = new LogActivity();
                                                        newLog[0].setUserID(user.getId());
                                                        newLog[0].setDateTime(c);
                                                        //get the type specific data1
                                                        ((LogActivity) newLog[0]).setDuration(bundle.getInt("minutesActivity"));
                                                        ((LogActivity) newLog[0]).setDescription(bundle.getString("activityDesc"));
                                                        Log.d("DEBUGGING", "Duration: " + ((LogActivity) newLog[0]).getDuration()
                                                                + " Desc: " + ((LogActivity) newLog[0]).getDescription());
                                                        getSupportFragmentManager().beginTransaction()
                                                                .replace(R.id.main_fragment, HomeFragment.newInstance(user))
                                                                .commitNow();
                                                        inputSuccess = true;
                                                    }
                                                    break;
                                                case "weight":
                                                    bundle.putAll(((WeightFragment) fragment).getLogData());
                                                    if (bundle.getBoolean("validInput")) {
                                                        newLog[0] = new LogWeight();
                                                        newLog[0].setUserID(user.getId());
                                                        newLog[0].setDateTime(c);
                                                        //get the type specific data
                                                        ((LogWeight) newLog[0]).setWeight(bundle.getInt("weight"));
                                                        user.setWeight(bundle.getInt("weight"));
                                                        Log.d("DEBUGGING", "Weight: " + ((LogWeight) newLog[0]).getWeight());
                                                        getSupportFragmentManager().beginTransaction()
                                                                .replace(R.id.main_fragment, HomeFragment.newInstance(user))
                                                                .commitNow();
                                                        inputSuccess = true;
                                                    }
                                                    break;
                                                case "mood":
                                                    bundle.putAll(((MoodFragment) fragment).getLogData());
                                                    if (bundle.getBoolean("validInput")) {
                                                        newLog[0] = new LogMood();
                                                        newLog[0].setUserID(user.getId());
                                                        newLog[0].setDateTime(c);
                                                        //get the type specific data
                                                        ((LogMood) newLog[0]).setMood(bundle.getString("mood"));
                                                        Log.d("DEBUGGING", "mood: " + ((LogMood) newLog[0]).getMood());
                                                        getSupportFragmentManager().beginTransaction()
                                                                .replace(R.id.main_fragment, HomeFragment.newInstance(user))
                                                                .commitNow();
                                                        inputSuccess = true;
                                                    }
                                                    break;

                                            }

                                            if (inputSuccess) {
                                                newLog[0].setDateTimeEntered(Calendar.getInstance().getTime());
                                                Goal g;
                                                ArrayList<Goal> goals = user.getGoals();
                                                for (Goal goal : goals) {
                                                    if (goal.resetWeeklyYet(Calendar.getInstance().getTime()))
                                                        goal.resetWeekly();
                                                    if (goal.resetDailyYet(Calendar.getInstance().getTime()))
                                                        goal.resetDaily();
                                                }
                                                Calendar today = Calendar.getInstance();
                                                Calendar thisWeek = Calendar.getInstance();
                                                today.set(Calendar.HOUR_OF_DAY, 0);
                                                today.set(Calendar.MINUTE, 0);
                                                today.set(Calendar.MILLISECOND, 0);
                                                today.add(Calendar.MILLISECOND, -1);
                                                thisWeek.set(Calendar.HOUR_OF_DAY, 0);
                                                thisWeek.set(Calendar.MINUTE, 0);
                                                thisWeek.set(Calendar.MILLISECOND, 0);
                                                while (thisWeek.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY)
                                                    thisWeek.add(Calendar.DATE, -1);
                                                thisWeek.add(Calendar.MILLISECOND, -1);
                                                switch (newLog[0].logType()) {
                                                    case "Activity":
                                                        user.addLogActivity((LogActivity) newLog[0]);
                                                        int minutes = ((LogActivity) newLog[0]).getDuration();
                                                        g = user.getGoal(Goal.ACTIVITY);
                                                        boolean dailyMet = g.dailyGoalMet();
                                                        boolean weeklyMet = g.weeklyGoalMet();
                                                        if (today.getTimeInMillis() < newLog[0].getDateTime().getTime())
                                                            g.addDailyProgress(minutes);
                                                        if (thisWeek.getTimeInMillis() < newLog[0].getDateTime().getTime())
                                                            g.addWeeklyProgress(minutes);
                                                        if (!dailyMet && g.dailyGoalMet())
                                                            GoalReminder.showDailyGoalMet(getApplicationContext(), g);
                                                        if (!weeklyMet && g.weeklyGoalMet())
                                                            GoalReminder.showWeeklyGoalMet(getApplicationContext(), g);

                                                        Log.d("GOALMET", "oldDailyMet: " + dailyMet
                                                                + " oldWeeklyMet: " + weeklyMet + " nowDailyMet: "
                                                                + g.dailyGoalMet() + " nowWeeklyMet " + g.weeklyGoalMet());
                                                        break;
                                                    case "BloodSugar":
                                                        user.addLogBloodSugar((LogBloodSugar) newLog[0]);
                                                        g = user.getGoal(Goal.BLOODSUGAR);
                                                        if (today.getTimeInMillis() < newLog[0].getDateTime().getTime())
                                                            g.addDailyProgress(1);
                                                        if (thisWeek.getTimeInMillis() < newLog[0].getDateTime().getTime())
                                                            g.addWeeklyProgress(1);
                                                        GoalReminder.showDailyGoalMet(getApplicationContext(), g);
                                                        GoalReminder.showWeeklyGoalMet(getApplicationContext(), g);
                                                        break;
                                                    case "Insulin":
                                                        user.addLogInsulin((LogInsulin) newLog[0]);
                                                        g = user.getGoal(Goal.INSULIN);
                                                        if (today.getTimeInMillis() < newLog[0].getDateTime().getTime())
                                                            g.addDailyProgress(1);
                                                        if (thisWeek.getTimeInMillis() < newLog[0].getDateTime().getTime())
                                                            g.addWeeklyProgress(1);
                                                        GoalReminder.showDailyGoalMet(getApplicationContext(), g);
                                                        GoalReminder.showWeeklyGoalMet(getApplicationContext(), g);
                                                        break;
                                                    case "Food":
                                                        user.addLogFood((LogFood) newLog[0]);
                                                        if (((LogFood) newLog[0]).isMeal()) {
                                                            g = user.getGoal(Goal.FOODMEAL);
                                                            boolean alreadyMet = g.dailyGoalMet();
                                                            if (today.getTimeInMillis() < newLog[0].getDateTime().getTime())
                                                                g.setMealsEaten(g.getMealsEaten() + 1);
                                                            GoalReminder.showDailyGoalMet(getApplicationContext(), g);
                                                            if (g.dailyGoalMet() && !alreadyMet)
                                                                g.setWeeklyProgress(g.getWeeklyProgress() + 1);
                                                        }
                                                        if (((LogFood) newLog[0]).isSnack()) {
                                                            g = user.getGoal(Goal.FOODSNACK);
                                                            boolean alreadyMet = g.dailyGoalMet();
                                                            if (today.getTimeInMillis() < newLog[0].getDateTime().getTime())
                                                                g.setSnacksEaten(g.getSnacksEaten() + 1);
                                                            GoalReminder.showDailyGoalMet(getApplicationContext(), g);
                                                            if (g.dailyGoalMet() && !alreadyMet)
                                                                g.setWeeklyProgress(g.getWeeklyProgress() + 1);
                                                        }

                                                        break;
                                                    case "Weight":
                                                        user.addLogWeight((LogWeight) newLog[0]);
                                                        g = user.getGoal(Goal.WEIGHT);
                                                        if (today.getTimeInMillis() < newLog[0].getDateTime().getTime())
                                                            g.addDailyProgress(1);
                                                        if (thisWeek.getTimeInMillis() < newLog[0].getDateTime().getTime())
                                                            g.addWeeklyProgress(1);
                                                        GoalReminder.showDailyGoalMet(getApplicationContext(), g);
                                                        GoalReminder.showWeeklyGoalMet(getApplicationContext(), g);
                                                        break;
                                                    case "Mood":
                                                        user.addLogMood((LogMood) newLog[0]);
                                                        g = user.getGoal(Goal.MOOD);
                                                        if (today.getTimeInMillis() < newLog[0].getDateTime().getTime())
                                                            g.addDailyProgress(1);
                                                        if (thisWeek.getTimeInMillis() < newLog[0].getDateTime().getTime())
                                                            g.addWeeklyProgress(1);
                                                        GoalReminder.showDailyGoalMet(getApplicationContext(), g);
                                                        GoalReminder.showWeeklyGoalMet(getApplicationContext(), g);
                                                        break;
                                                }
                                                if (newLog[0].logType().equals("Medication")) {
                                                    user.addLogMedication((LogMedication) newLog[0]);
                                                    Medication m = ((LogMedication) newLog[0]).getMedication();
                                                    g = user.getGoal("Medication: " + m.getName());
                                                    boolean alreadyMet = g.dailyGoalMet();
                                                    if (today.getTimeInMillis() < newLog[0].getDateTime().getTime())
                                                        g.addDailyProgress(1);
                                                    GoalReminder.showDailyGoalMet(getApplicationContext(), g);
                                                    if (g.dailyGoalMet() && !alreadyMet)
                                                        g.addWeeklyProgress(1);
                                                }
                                                Log.d("DEBUGGING", user.toString());
                                                setMessage("logAdded");
                                                UserStorage.saveUser(user);
                                                UserStorage.storeActivity(UserStorage.LOG);
                                            }
                                            //wrote l code here

                                        }
                                });


                            }
                        });


                    }
            }
        }
    }

    /*
    @Override
    public void messageFromRegistrationFragment(Bundle bundle)
    {
        user.setUserName(bundle.getString("username"));
        userHash = bundle.getString("passwordHash");

        Log.d("REGISTRATION", user.getUserName() + " " + userHash);

        if (!storage.existingUsername(user.getUserName())) {
            storage.saveUserList(user.getUserName(), userHash);
        }
        else
            user = storage.loadUser(user.getUserName(), userHash);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_fragment, PersonalInfoFragment.newInstance(user, registration), "pInfo")
                .commitNow();

        setMessage("pInfoSetup");
    }
    */
    public String getMessage(String caller) {
        String s = caller;
        switch (s) {
            case "home":
                s = "Welcome to the home screen! You can enter logs or find helpful links here.";
                break;
            case "log":
                s = "Welcome to the tracker screen! You can see how you're doing on your goals and logs here.";
                break;
            case "settings":
                s = "Welcome to settings! You can view your progress levels and change your reminders, goals, or medications here.";
                break;
            case "bloodSugar":
                s = "Here you can enter in your blood sugar. Tap the number wheel if you'd rather type in the number.";
                break;
            case "insulin":
                s = "Here you can enter how much insulin you've taken, and if it's short or long-acting.";
                break;
            case "medicine":
                s = "Here you can pick which of your medications you've taken.";
                break;
            case "food":
                s = "Here you can enter what you've eaten, and if it was a meal or a snack.";//  If you just want to record a meal or a snack, you can leave the text box empty.";
                break;
            case "weight":
                s = "Here you can record your weight.";
                break;
            case "mood":
                s = "Here you can record how you're feeling right now.";
                break;
            case "activity":
                s = "Here you can record how many minutes of exercise you've done.";
                break;
            case "futureLogError":
                s = "Please make sure the time and date are correct.";
                break;
            case "bloodSugarError":
                s = "Please type in your blood sugar reading.";
                break;
            case "insulinError":
                s = "Please type in how much insulin you've taken.";
                break;
            case "foodError":
                s = "Please type in what you ate recently.";
                break;
            case "weightError":
                s = "Please type in your weight.";
                break;
            case "moodError":
                s = "Please choose the face that best shows how you're feeling right now.";
                break;
            case "activityError":
                s = "Please type in how many minutes of exercise you've done, and what type.";
                break;
            case "logAdded":
                s = "Your log has been saved!";
                break;
            case "pInfoSetup":
                s = "Set your age and gender here.";
                break;
            case "levelsSetup":
                s = "Welcome to clubs. You can view your club membership here.";
                break;
            case "notificationSetup":
                s = "Create, change, or turn off your reminders here.";
                break;
            case "notificationSetupError":
                s = "Make sure to enter a description for your reminder.";
                break;
            case "notificationDuplicateError":
                s = "You already have a reminder with that description.";
                break;
            case "notificationSetupTimeError":
                s = "Make sure you set your alarm for a future date.";
                break;
            case "goalsSetup":
                s = "Change your daily and weekly goals here.";
                break;
            case "goalsChanged":
                s = "Your goal targets were changed.";
                break;
            case "medicationList":
                s = "Add any medicine other than insulin that you're taking right now.";
                break;
            case "medicationSetup":
                s = "Enter the name of the medicine, and the number of times a day you take it.";
                break;
            case "medicationError":
                s = "Please make sure to enter the name of the medicine.";
                break;
            case "medicationInsulinError":
                s = "You don't need to add your insulin as a specific medication you take.";
                break;
            case "medicationDosageError":
                s = "Please make sure to enter the number of times you take this medicine.";
                break;
            case "medicationDoseError":
                s = "Please enter how many times you take this medicine per day.";
                break;
            case "medicationDuplicateError":
                s = "You already have a medicine with that same name.";
                break;
            case "medicationSave":
                s = "Medicine added successfully.";
                break;
            case "pInfoError":
                s = "Make sure to enter your age.";
                break;
            case "pInfoAgeError":
                s = "Make sure you typed in your age correctly.";
                break;
            case "pInfoTypeError":
                s = "Make sure you picked your diabetes type from the drop down menu.";
                break;
            case "pInfoGenderError":
                s = "Make sure you picked your gender from the drop down menu.";
                break;
            case "pInfoUpdate":
                s = "Your personal information has been changed.";
                break;
            case "registrationSetup":
                s = "Please enter in your email and the password you will use.";
                break;
            case "registrationEmptyError":
                s = "Make sure not leave anything blank for email and password.";
                break;
            case "passwordLength":
                s = "Password must be between 8 and 16 characters.";
                break;
            case "passwordRequirements":
                s = "Password must have an uppercase and lowercase letter, and a number.";
                break;
            case "help":
                s = "Hi! I'm your diabetes buddy, and I'll help you with whatever screen you're on.";
                break;
            case "resources":
                s = "Here's a list of helpful links on managing diabetes!";
                break;
            default:
                s = "I'm not sure what screen you're on.";
                break;
        }

        return s;
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();

        Fragment f = getSupportFragmentManager().findFragmentById(R.id.main_fragment);
        if (f instanceof SettingsFragment || f instanceof LogFragment) {
            UserStorage.readUser(new FirestoreCallback() {
                @Override
                public void onCallback(User user) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.main_fragment, HomeFragment.newInstance(user))
                            .commitNow();
                    setMessage("home");
                }
            });
        } else if (f instanceof MedicationSetupFragment) {
            UserStorage.readUser(new FirestoreCallback() {
                @Override
                public void onCallback(User user) {
                    setMessage("medicationList");
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.main_fragment, MedicationFragment.newInstance("", user, registration), "goals")
                            .commitNow();
                }
            });
        } else if (f instanceof NotificationSetupFragment) {
            UserStorage.readUser(new FirestoreCallback() {
                @Override
                public void onCallback(User user) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.main_fragment, NotificationFragment.newInstance("", user, registration), "notifications")
                            .commitNow();
                    setMessage("notificationSetup");
                }
            });
        } else if (f instanceof GoalSetupFragment) {
            UserStorage.readUser(new FirestoreCallback() {
                @Override
                public void onCallback(User user) {
                    setMessage("goalsSetup");
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.main_fragment, GoalFragment.newInstance("", user, registration), "goals")
                            .commitNow();
                }
            });
        } else if (f instanceof LogBaseline) {
            UserStorage.readUser(new FirestoreCallback() {
                @Override
                public void onCallback(User user) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.main_fragment, HomeFragment.newInstance(user))
                            .commitNow();
                    setMessage("home");
                }
            });
        } else if (f instanceof ResourceFragment) {
            UserStorage.readUser(new FirestoreCallback() {
                @Override
                public void onCallback(User user) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.main_fragment, HomeFragment.newInstance(user))
                            .commitNow();
                    setMessage("home");
                }
            });
        } else if (!registration) {
            if (f instanceof MedicationFragment || f instanceof NotificationFragment || f instanceof GoalFragment) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.main_fragment, SettingsFragment.newInstance())
                        .commitNow();
                setMessage("settings");
            }
        } else {
            if (f instanceof MedicationFragment) {
                UserStorage.readUser(new FirestoreCallback() {
                    @Override
                    public void onCallback(User user) {
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.main_fragment, PersonalInfoFragment.newInstance(user, registration), "pInfo")
                                .commitNow();
                        setMessage("pInfoSetup");
                    }
                });
            } else if (f instanceof LevelFragment) {
                UserStorage.readUser(new FirestoreCallback() {
                    @Override
                    public void onCallback(User user) {
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.main_fragment, LevelFragment.newInstance(user, registration), "levels")
                                .addToBackStack("levels")
                                .commit();
                        setMessage("levelsSetup");

                    }
                });
            } else if (f instanceof GoalFragment) {
                UserStorage.readUser(new FirestoreCallback() {
                    @Override
                    public void onCallback(User user) {
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.main_fragment, NotificationFragment.newInstance("", user, registration), "notifications")
                                .commitNow();
                        setMessage("notificationSetup");
                    }
                });
            } else if (f instanceof NotificationFragment) {
                UserStorage.readUser(new FirestoreCallback() {
                    @Override
                    public void onCallback(User user) {
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.main_fragment, MedicationFragment.newInstance("", user, registration), "notifications")
                                .commitNow();
                        setMessage("medicationList");
                    }
                });
            } else {
                super.onBackPressed();
                getSupportFragmentManager().popBackStack();
            }
        }
        /*if(getSupportFragmentManager().getBackStackEntryCount()>0){
            getFragmentManager().popBackStackImmediate();
        }
        else {
            super.onBackPressed();
        }*/
    }

    public void setMessage(String caller) {
        String s;
        previousMessage = message;
        message = caller;
        s = getMessage(caller);
        buddyMessage.setText(s);
        speakMessage(s);
    }

    public void speakMessage(final String s) {
        tts.stop();
        Log.d("TTS", "in speakMessage");
        if (ttsOn)
            tts.speak(s, TextToSpeech.QUEUE_FLUSH, null);
        /*UserStorage.readUser(new FirestoreCallback() {
            @Override
            public void onCallback(User user) {
                if (user.isTextToSpeechEnabled())
                    Log.d("TTS", "TTS is enabled, attempting to speak");
                    t
            }
        });*/
    }

    private View.OnClickListener buddyIconListener = new View.OnClickListener() {
        public void onClick(View v) {
            v.playSoundEffect(SoundEffectConstants.CLICK);
            Log.d("DEBUGGING", "buddy icon clicked");
            tts.stop();
            if (message.equals("help"))
                setMessage(previousMessage);
            else
                setMessage("help");
        }
    };

    private View.OnClickListener buddyMessageListener = new View.OnClickListener() {
        public void onClick(View v) {
            v.playSoundEffect(SoundEffectConstants.CLICK);
            Log.d("DEBUGGING", "buddy message clicked");
            tts.stop();
            ttsOn = !ttsOn;
            if (!ttsOn)
                ttsIndicator.setImageResource(R.drawable.ic_volume_off_black_24dp);
            else
                ttsIndicator.setImageResource(R.drawable.ic_volume_up_black_24dp);
        }
    };

    private View.OnClickListener logNavListener = new View.OnClickListener() {
        public void onClick(View v) {
            v.playSoundEffect(SoundEffectConstants.CLICK);
            UserStorage.readUser(new FirestoreCallback() {
                @Override
                public void onCallback(User user) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.main_fragment, LogFragment.newInstance(user))
                            .commitNow();
                    setMessage("log");
                }
            });

        }
    };

    private View.OnClickListener homeNavListener = new View.OnClickListener() {
        public void onClick(View v) {
            v.playSoundEffect(SoundEffectConstants.CLICK);
            UserStorage.readUser(new FirestoreCallback() {
                @Override
                public void onCallback(User user) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.main_fragment, HomeFragment.newInstance(user))
                            .commitNow();
                    setMessage("home");
                }
            });
        }
    };

    private View.OnClickListener settingsNavListener = new View.OnClickListener() {
        public void onClick(View v) {
            v.playSoundEffect(SoundEffectConstants.CLICK);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_fragment, SettingsFragment.newInstance())
                    .commitNow();
            setMessage("settings");
        }
    };

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(getString(R.string.channel_id), name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public void signOut() {
        // [START auth_fui_signout]
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        Intent intent = new Intent(MainActivity.this, FirebaseAuthActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                });
        // [END auth_fui_signout]
    }

    @Override
    protected void onSaveInstanceState(Bundle oldInstanceState) {
        super.onSaveInstanceState(oldInstanceState);
        oldInstanceState.clear();
    }


    @Override
    protected void onStop() {
        super.onStop();
        editor.putBoolean("tts", ttsOn);
        editor.commit();
        UserStorage.readUser(new FirestoreCallback() {
            @Override
            public void onCallback(User user) {
                user.setTextToSpeechEnabled(ttsOn);
                UserStorage.saveUser(user);
                if (user.getReminders() != null) {
                    ArrayList<Reminder> reminders = user.getReminders();
                    String filename = "reminderList";
                    FileOutputStream outputStream;
                    try {
                        outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
                        //outputStream.write(fileContents.getBytes());
                        //outputStream.close();
                        ObjectOutputStream objectOut = new ObjectOutputStream(outputStream);
                        objectOut.writeObject(reminders);
                        objectOut.close();
                        Log.d("TESTFILE", "Finished writing reminders to file");
                    } catch (Exception e) {
                        Log.d("TESTFILE", e.getMessage());
                    }
                }
            }
        });/*
        if (user != null && user.getReminders() != null) {
            ArrayList<Reminder> reminders = user.getReminders();
            String filename = "reminderList";
            FileOutputStream outputStream;
            try {
                outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
                //outputStream.write(fileContents.getBytes());
                //outputStream.close();
                ObjectOutputStream objectOut = new ObjectOutputStream(outputStream);
                objectOut.writeObject(reminders);
                objectOut.close();
                Log.d("TESTFILE", "Finished writing reminders to file");
            } catch (Exception e) {
                Log.d("TESTFILE", e.getMessage());
            }
            //storage.saveUser(user, userHash);
        }*/
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("DEBUGGING", "at onDestroy");
        //if (user != null)
        //    storage.saveUser(user, userHash);
        //user = null;
    }
}