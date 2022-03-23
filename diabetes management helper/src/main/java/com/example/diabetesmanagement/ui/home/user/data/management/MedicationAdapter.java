package com.example.diabetesmanagement.ui.home.user.data.management;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SoundEffectConstants;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.diabetesmanagement.R;
import com.example.diabetesmanagement.data.Medication;

import java.util.ArrayList;


public class MedicationAdapter extends ArrayAdapter<Medication> {

    private MedicationFragment hostFragment;

    public MedicationAdapter(Context context, ArrayList<Medication> users, MedicationFragment parent) {
        super(context, 0, users);
        hostFragment = parent;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Medication medication = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_medication, parent, false);
        }
        // Lookup view for data population
        TextView medicationName = convertView.findViewById(R.id.list_medication_name);
        Button deleteButton = convertView.findViewById(R.id.button_medication_delete);
        final Button editButton = convertView.findViewById(R.id.button_medication_edit);


        // Populate the data into the template view using the data object
        String s;
        s = getContext().getResources().getString(R.string.medicationLabel) + " " + medication.getName();
        medicationName.setText(s);

        deleteButton.setTag(position);
        // Attach the click event handler
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.playSoundEffect(SoundEffectConstants.CLICK);
                int position = (Integer) view.getTag();
                // Access the row position here to get the correct data item
                Medication medication1 = getItem(position);
                // Do what you want here...
                Log.d("DEBUGGING", "Clicked on delete for: " + medication1.getName());
                hostFragment.deleteMedication(medication1);
            }
        });

        editButton.setTag(position);
        // Attach the click event handler
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.playSoundEffect(SoundEffectConstants.CLICK);
                int position = (Integer) view.getTag();
                // Access the row position here to get the correct data item
                Medication medication1 = getItem(position);
                // Do what you want here...
                Log.d("DEBUGGING", "Clicked on edit for: " + medication1.getName());
                hostFragment.editMedication(medication1);
            }
        });

        // Return the completed view to render on screen
        return convertView;
    }

}
