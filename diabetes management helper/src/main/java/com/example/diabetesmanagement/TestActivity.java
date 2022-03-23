package com.example.diabetesmanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.diabetesmanagement.data.LogBloodSugar;
import com.example.diabetesmanagement.data.Medication;
import com.example.diabetesmanagement.data.Reminder;
import com.example.diabetesmanagement.data.User;
import com.example.diabetesmanagement.ui.home.log.entry.BloodSugarFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.sql.Time;
import java.util.Calendar;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        Log.d("FIREBASELOGIN", user.getEmail());
        if (user != null) {
            String id = user.getUid();
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            /*User user1 = new User(id);
            user1.setUserName(user.getEmail());
            LogBloodSugar bLog = new com.example.diabetesmanagement.data.LogBloodSugar();
            bLog.setDateTime(Timestamp.now());
            bLog.setDateTimeEntered(Timestamp.now());
            bLog.setBeforeBed(true);
            bLog.setUserID(id);
            bLog.setBloodSugar(100);
            user1.addLog(bLog);

            Reminder r = new Reminder();
            r.setFrequency(Reminder.Frequency.DAILY);
            user1.setReminderRequestCodes(user1.getReminderRequestCodes()+1);
            r.setRequestCode(user1.getReminderRequestCodes());
            r.setDescription("Firestore Test");
            r.setEnabled(true);
            r.setUserID(id);
            r.setVisible(true);
            r.setHour(15);
            r.setMinute(0);
            user1.addReminder(r);

            Medication m = new Medication();
            m.setName("Firestore med");
            m.setUserID(id);
            user1.addMedication(m);

            db.collection("users").document(id).set(user1);
           // db.collection("users").add(user1);
            */


            final DocumentReference docRef = db.collection("users").document(id);
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Log.d("FIREBASELOGIN", "DocumentSnapshot data: " + document.getData());
                            User user1 = document.toObject(User.class);
                            TextView text = findViewById(R.id.text_testActivity);
                            if (user1.getMedications().size() > 0)
                                text.setText(user1.getMedication("Firestore med").getName());
                        } else {
                            Log.d("FIREBASELOGIN", "No such document");
                        }
                    } else {
                        Log.d("FIREBASELOGIN", "get failed with ", task.getException());
                    }
                }
            });

        }
    }
}
