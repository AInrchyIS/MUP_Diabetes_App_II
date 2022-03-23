package com.example.diabetesmanagement.ui.home.user.data.management;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SoundEffectConstants;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.diabetesmanagement.MainActivity;
import com.example.diabetesmanagement.R;
import com.example.diabetesmanagement.data.Reminder;
import com.example.diabetesmanagement.service.UserStorage;

import java.util.ArrayList;


public class NotificationsAdapter extends ArrayAdapter<Reminder> {

    private NotificationFragment hostFragment;

    public NotificationsAdapter(Context context, ArrayList<Reminder> users, NotificationFragment parent) {
        super(context, 0, users);
        hostFragment = parent;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Reminder reminder = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_notification, parent, false);
        }
        // Lookup view for data population
        TextView notificationDesc = convertView.findViewById(R.id.list_notification_desc);
        TextView notificationFrequency = convertView.findViewById(R.id.list_notification_freq);
        final TextView notificationStatus = convertView.findViewById(R.id.notification_status);
        Button deleteButton = convertView.findViewById(R.id.button_notification_delete);
        Button editButton = convertView.findViewById(R.id.button_notification_edit);
        final Button enableButton = convertView.findViewById(R.id.button_notification_enable);

        final LinearLayout buttonBar = convertView.findViewById(R.id.notification_buttons);
        if (!reminder.isVisible())
            buttonBar.setVisibility(View.GONE);
        else
            buttonBar.setVisibility(View.VISIBLE);
        // Populate the data into the template view using the data object
        String s = "";
        s = "Description: " + reminder.getDescription();
        notificationDesc.setText(s);
        s = "Repeats: " + reminder.frequencyString();
        notificationFrequency.setText(s);
        if (!reminder.isEnabled()) {
            enableButton.setText(R.string.notificationEnable);
            notificationStatus.setText(R.string.notificationOff);
        }
        else
            notificationStatus.setText(R.string.notificationOn);

        notificationDesc.setTag(position);
        // Attach the click event handler
        notificationDesc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.playSoundEffect(SoundEffectConstants.CLICK);
                int position = (Integer) view.getTag();
                // Access the row position here to get the correct data item
                Reminder reminder = getItem(position);
                // Do what you want here...
                Log.d("DEBUGGING", "Clicked on: " + reminder.getDescription());

                //hide or show button bar
                if (reminder.isVisible()) {
                   reminder.setVisible(false);
                   buttonBar.setVisibility(View.GONE);
                }
                else {
                    reminder.setVisible(true);
                    buttonBar.setVisibility(View.VISIBLE);
                }
            }
        });

        notificationFrequency.setTag(position);
        // Attach the click event handler
        notificationFrequency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.playSoundEffect(SoundEffectConstants.CLICK);
                int position = (Integer) view.getTag();
                // Access the row position here to get the correct data item
                Reminder reminder = getItem(position);
                // Do what you want here...
                Log.d("DEBUGGING", "Clicked on: " + reminder.getDescription());

                //hide or show button bar
                if (reminder.isVisible()) {
                    reminder.setVisible(false);
                    buttonBar.setVisibility(View.GONE);
                }
                else {
                    reminder.setVisible(true);
                    buttonBar.setVisibility(View.VISIBLE);
                }
            }
        });

        deleteButton.setTag(position);
        // Attach the click event handler
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.playSoundEffect(SoundEffectConstants.CLICK);
                int position = (Integer) view.getTag();
                // Access the row position here to get the correct data item
                Reminder reminder = getItem(position);
                // Do what you want here...
                Log.d("DEBUGGING", "Clicked on delete for: " + reminder.getDescription());
                hostFragment.deleteNotification(reminder);
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
                Reminder reminder = getItem(position);
                // Do what you want here...
                Log.d("DEBUGGING", "Clicked on edit for: " + reminder.getDescription());
                hostFragment.editNotification(reminder, false);
            }
        });

        enableButton.setTag(position);
        // Attach the click event handler
        enableButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.playSoundEffect(SoundEffectConstants.CLICK);
                int position = (Integer) view.getTag();
                // Access the row position here to get the correct data item
                Reminder reminder = getItem(position);
                // Do what you want here...
                Log.d("DEBUGGING", "Clicked on " + ((Button)view).getText() + " for: " + reminder.getDescription());
                if (reminder.isEnabled()) {
                    reminder.cancelNotification(getContext());
                    reminder.setEnabled(false);
                    ((Button)view).setText(R.string.notificationEnable);
                    notificationStatus.setText(R.string.notificationOff);
                }
                else {
                    reminder.scheduleNotification(getContext(), reminder.getNotification(getContext()));
                    reminder.setEnabled(true);
                    ((Button)view).setText(R.string.notificationDisable);
                    notificationStatus.setText(R.string.notificationOn);
                }
            }
        });

        // Return the completed view to render on screen
        return convertView;
    }

}
