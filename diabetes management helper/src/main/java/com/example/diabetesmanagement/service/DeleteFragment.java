package com.example.diabetesmanagement.service;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.diabetesmanagement.R;

public class DeleteFragment extends DialogFragment {

    private String message;
    private boolean buttonPressed = false;
    private ButtonDialogListener listener;

    public interface ButtonDialogListener {
        void onDialogButtonPressed(boolean confirm);
    }

    public DeleteFragment(String message){
        super();
        this.message = message;
    }

    public void sendBackResult() {
        // Notice the use of `getTargetFragment` which will be set when the dialog is displayed
        ButtonDialogListener listener = (ButtonDialogListener) getTargetFragment();
        listener.onDialogButtonPressed(buttonPressed);
        dismiss();
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Add the buttons
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
                buttonPressed = true;
                sendBackResult();
            }
        });
        builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                buttonPressed = false;
                sendBackResult();
            }

        });
        builder.setMessage(message);

        return builder.create();
    }
    /*
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the EditNameDialogListener so we can send events to the host
            listener = (ButtonDialogListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(context.toString()
                    + " must implement ButtonDialogListener");
        }
    }*/
}
