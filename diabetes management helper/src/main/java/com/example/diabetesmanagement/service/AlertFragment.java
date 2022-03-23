package com.example.diabetesmanagement.service;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.diabetesmanagement.R;

public class AlertFragment extends DialogFragment {

    private int bloodsugar;

    public AlertFragment(int bloodsugar){
        super();
        this.bloodsugar = bloodsugar;
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
        if (bloodsugar > 180) {
            builder.setMessage("The last blood sugar you entered, " + bloodsugar + ", is outside a safe range.  Please call your doctor.");
        }
        else {
            builder.setMessage("The last blood sugar you entered, " + bloodsugar
            + ", is below a safe range.  You should drink some juice or have some glucose tablets.");
        }
        return builder.create();
    }
}
