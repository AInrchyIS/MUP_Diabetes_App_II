package com.example.diabetesmanagement.service;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.diabetesmanagement.R;



public class ActivityAlertFragment extends DialogFragment {


    public ActivityAlertFragment(){
        super();

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Add the buttons
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
            }
        });
        builder.setMessage("Please enter a value greater than or equal to minutes set for the goal or decrease the amount of minutes in goal settings.\n\n  (Maximum 180 minutes per day)");
        return builder.create();
    }
}
