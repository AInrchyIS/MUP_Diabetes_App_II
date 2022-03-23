/*package com.example.diabetesmanagement.service;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;

public class UploadWorker extends Worker {

    public UploadWorker(
            @NonNull Context context,
            @NonNull WorkerParameters params) {
        super(context, params);
    }

    @Override
    public Result doWork() {
        // Do the work here--in this case, upload the images.

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null)
        {
            String id = firebaseUser.getUid();
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("goalRecords").document(id +" " + Calendar.getInstance().getTime()).set(goal);
            //Log.d("FBDEBUG", "SAVING GOAL: " + goal.getType());
        }

        // Indicate whether the task finished successfully with the Result
        return Result.success();
    }
}
*/