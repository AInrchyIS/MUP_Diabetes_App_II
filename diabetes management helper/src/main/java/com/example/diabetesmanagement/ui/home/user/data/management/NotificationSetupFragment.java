package com.example.diabetesmanagement.ui.home.user.data.management;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SoundEffectConstants;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.diabetesmanagement.MainActivity;
import com.example.diabetesmanagement.R;
import com.example.diabetesmanagement.data.Reminder;
import com.example.diabetesmanagement.data.User;
import com.example.diabetesmanagement.ui.home.log.entry.LogBaseline;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import static com.example.diabetesmanagement.data.Reminder.Frequency.DAILY;
import static com.example.diabetesmanagement.data.Reminder.Frequency.NONE;
import static com.example.diabetesmanagement.data.Reminder.Frequency.WEEKLY;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NotificationSetupFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NotificationSetupFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NotificationSetupFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";
    private static final String ARG_PARAM4 = "param4";

    // TODO: Rename and change types of parameters
    private boolean newNotification;
    private static Bundle notificationBundle;
    private Handler handler;
    private boolean registration;

    private OnFragmentInteractionListener mListener;
    private User user;
    private Reminder reminder;
    private int hour;
    private int minute;

    private EditText description;
    private RadioGroup frequencyGroup;
    private RadioButton dailyButton;
    private RadioButton weeklyButton;
    private RadioButton neverButton;
    private Button timeButton;
    private Button dateButton;
    private TextView weeklyLabel;
    private LinearLayout weeklyGroup;
    private CheckBox sunday;
    private CheckBox monday;
    private CheckBox tuesday;
    private CheckBox wednesday;
    private CheckBox thursday;
    private CheckBox friday;
    private CheckBox saturday;

    private Button saveButton;
    private Button cancelButton;

    public NotificationSetupFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NotificationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NotificationSetupFragment newInstance(Boolean param1, User param2, Reminder param3,
                                                        boolean param4) {
        NotificationSetupFragment fragment = new NotificationSetupFragment();
        Bundle args = new Bundle();
        args.putBoolean(ARG_PARAM1, param1);
        args.putSerializable(ARG_PARAM2, param2);
        args.putSerializable(ARG_PARAM3, param3);
        args.putBoolean(ARG_PARAM4, param4);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            newNotification = getArguments().getBoolean(ARG_PARAM1);
            user = (User)getArguments().getSerializable(ARG_PARAM2);
            if (!newNotification)
                reminder = (Reminder)getArguments().getSerializable(ARG_PARAM3);
            else
                reminder = new Reminder();
            registration = getArguments().getBoolean(ARG_PARAM4);
        }
        Log.d("DEBUGGING", "This is new? " + String.valueOf(newNotification));
        if (!newNotification)
            Log.d("DEBUGGING", "Received reminder " + reminder.getDescription());
        handler = new Handler();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notification_setup, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        description = view.findViewById(R.id.data_notification_title);
        frequencyGroup = view.findViewById(R.id.radioGroup_frequency);
        dailyButton = view.findViewById(R.id.radio_daily);
        weeklyButton = view.findViewById(R.id.radio_weekly);
        neverButton = view.findViewById(R.id.radio_never);
        timeButton = view.findViewById(R.id.button_notification_time);
        dateButton = view.findViewById(R.id.button_notification_date);
        saveButton = view.findViewById(R.id.button_notification_save);
        cancelButton = view.findViewById(R.id.button_notification_cancel);

        //bind weekly checkbox and related views
        weeklyLabel = view.findViewById(R.id.label_notification_weekly_days);
        weeklyGroup = view.findViewById(R.id.buttonGroup_daysOfWeek);
        sunday = view.findViewById(R.id.checkBox_sunday);
        monday = view.findViewById(R.id.checkBox_monday);
        tuesday = view.findViewById(R.id.checkBox_tuesday);
        wednesday = view.findViewById(R.id.checkBox_wednesday);
        thursday = view.findViewById(R.id.checkBox_thursday);
        friday = view.findViewById(R.id.checkBox_friday);
        saturday = view.findViewById(R.id.checkBox_saturday);

        // This overrides the radiogroup onCheckListener
        frequencyGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                if (weeklyButton.isChecked())
                {
                    weeklyLabel.setVisibility(View.VISIBLE);
                    weeklyGroup.setVisibility(View.VISIBLE);
                }
                else
                {
                    weeklyLabel.setVisibility(View.INVISIBLE);
                    weeklyGroup.setVisibility(View.INVISIBLE);
                }
                if (neverButton.isChecked())
                    dateButton.setVisibility(View.VISIBLE);
                else if (!neverButton.isChecked())
                    dateButton.setVisibility(View.INVISIBLE);
            }
        });

        //get current date/time values
        Calendar c = Calendar.getInstance();
        notificationBundle.putInt("hour", c.get(Calendar.HOUR_OF_DAY));
        notificationBundle.putInt("minute", c.get(Calendar.MINUTE));
        notificationBundle.putInt("year", c.get(Calendar.YEAR));
        notificationBundle.putInt("month", c.get(Calendar.MONTH));
        notificationBundle.putInt("day", c.get(Calendar.DAY_OF_MONTH));

        DateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.US);
        DateFormat timeFormat = new SimpleDateFormat("hh:mm a", Locale.US);
        timeButton.setText(timeFormat.format(c.getTime()));
        dateButton.setText(dateFormat.format(c.getTime()));

        if(!newNotification) {
            description.setText(reminder.getDescription());
            c.set(0,0,0, reminder.getHour(), reminder.getMinute());
            timeButton.setText(timeFormat.format(c.getTime()));
            notificationBundle.putInt("hour", reminder.getHour());
            notificationBundle.putInt("minute", reminder.getMinute());
            switch(reminder.getFrequency())
            {
                case DAILY:
                    dailyButton.setChecked(true);
                    weeklyLabel.setVisibility(View.INVISIBLE);
                    weeklyGroup.setVisibility(View.INVISIBLE);
                    dateButton.setVisibility(View.INVISIBLE);
                    break;
                case WEEKLY:
                    weeklyButton.setChecked(true);
                    dateButton.setVisibility(View.INVISIBLE);
                    weeklyLabel.setVisibility(View.VISIBLE);
                    weeklyGroup.setVisibility(View.VISIBLE);
                    sunday.setChecked(reminder.isSunday());
                    monday.setChecked(reminder.isMonday());
                    tuesday.setChecked(reminder.isTuesday());
                    wednesday.setChecked(reminder.isWednesday());
                    thursday.setChecked(reminder.isThursday());
                    friday.setChecked(reminder.isFriday());
                    saturday.setChecked(reminder.isSaturday());
                    break;
                case NONE:
                    neverButton.setChecked(true);
                    dateButton.setVisibility(View.VISIBLE);
                    weeklyLabel.setVisibility(View.INVISIBLE);
                    weeklyGroup.setVisibility(View.INVISIBLE);
                    c.set(reminder.getCalendarYear(), reminder.getCalendarMonth(), reminder.getCalendarDay());
                    dateButton.setText(dateFormat.format(c.getTime()));
                    notificationBundle.putInt("year", reminder.getCalendarYear());
                    notificationBundle.putInt("month", reminder.getCalendarMonth());
                    notificationBundle.putInt("day", reminder.getCalendarDay());
                    break;
            }
        }

        timeButton.setOnClickListener(timeButtonListener);
        dateButton.setOnClickListener(dateButtonListener);
        saveButton.setOnClickListener(saveButtonListener);
        cancelButton.setOnClickListener(cancelButtonListener);
    }

    private View.OnClickListener cancelButtonListener = new View.OnClickListener() {
        public void onClick(View v) {
            v.playSoundEffect(SoundEffectConstants.CLICK);
            notificationBundle.putString("button", "cancel");
            onButtonPressed(notificationBundle);
        }
    };
    private View.OnClickListener saveButtonListener = new View.OnClickListener() {
        public void onClick(View v) {
            v.playSoundEffect(SoundEffectConstants.CLICK);
            ArrayList<String> reminderNames = new ArrayList<>();
            for (Reminder r:user.getReminders())
            {
                reminderNames.add(r.getDescription());
            }
            Calendar alarmDateTime = Calendar.getInstance();
            if (frequencyGroup.getCheckedRadioButtonId() == R.id.radio_never)
                alarmDateTime.set(notificationBundle.getInt("year"),
                        notificationBundle.getInt("month"), notificationBundle.getInt("day"),
                        notificationBundle.getInt("hour"), notificationBundle.getInt("minute"));
            if (!description.getText().toString().equals("")) {
                if ((!newNotification && user.reminderCount(description.getText().toString()) <= 1) ||
                        (newNotification && user.reminderCount(description.getText().toString()) == 0)) {
                    if ((frequencyGroup.getCheckedRadioButtonId() == R.id.radio_never
                            && Calendar.getInstance().getTimeInMillis() < alarmDateTime.getTimeInMillis())
                            || frequencyGroup.getCheckedRadioButtonId() == R.id.radio_weekly
                            || frequencyGroup.getCheckedRadioButtonId() == R.id.radio_daily) {
                        if (!newNotification)
                            notificationBundle.putString("oldDesc", reminder.getDescription());
                        notificationBundle.putBoolean("newNotification", newNotification);
                        notificationBundle.putString("button", "save");
                        reminder.setDescription(description.getText().toString());
                        reminder.setHour(notificationBundle.getInt("hour"));
                        reminder.setMinute(notificationBundle.getInt("minute"));

                        switch (frequencyGroup.getCheckedRadioButtonId()) {
                            case R.id.radio_daily:
                                reminder.setFrequency(DAILY);
                                reminder.setSunday(false);
                                reminder.setMonday(false);
                                reminder.setTuesday(false);
                                reminder.setWednesday(false);
                                reminder.setThursday(false);
                                reminder.setFriday(false);
                                reminder.setSaturday(false);
                                reminder.setCalendarDay(0);
                                reminder.setCalendarMonth(0);
                                reminder.setCalendarYear(0);
                                break;
                            case R.id.radio_weekly:
                                reminder.setFrequency(WEEKLY);
                                reminder.setSunday(sunday.isChecked());
                                reminder.setMonday(monday.isChecked());
                                reminder.setTuesday(tuesday.isChecked());
                                reminder.setWednesday(wednesday.isChecked());
                                reminder.setThursday(thursday.isChecked());
                                reminder.setFriday(friday.isChecked());
                                reminder.setSaturday(saturday.isChecked());
                                reminder.setCalendarDay(0);
                                reminder.setCalendarMonth(0);
                                reminder.setCalendarYear(0);
                                break;
                            case R.id.radio_never:
                                reminder.setFrequency(NONE);
                                reminder.setSunday(false);
                                reminder.setMonday(false);
                                reminder.setTuesday(false);
                                reminder.setWednesday(false);
                                reminder.setThursday(false);
                                reminder.setFriday(false);
                                reminder.setSaturday(false);
                                reminder.setCalendarDay(notificationBundle.getInt("day"));
                                reminder.setCalendarMonth(notificationBundle.getInt("month"));
                                reminder.setCalendarYear(notificationBundle.getInt("year"));
                                break;
                        }
                        reminder.setEnabled(true);

                        notificationBundle.putSerializable("reminder", reminder);
                        onButtonPressed(notificationBundle);
                    } else
                        ((MainActivity) getActivity()).setMessage("notificationSetupTimeError");
                }
                else
                    ((MainActivity) getActivity()).setMessage("notificationDuplicateError");
            }
            else {
                ((MainActivity) getActivity()).setMessage("notificationSetupError");
            }
        }
    };

    // Create an anonymous implementation of OnClickListener
    private View.OnClickListener timeButtonListener = new View.OnClickListener() {
        public void onClick(View v) {
            v.playSoundEffect(SoundEffectConstants.CLICK);
            notificationBundle.putString("button", "time");
            onButtonPressed(notificationBundle);
            // Define the code block to be executed
            Runnable runnableCode = new Runnable() {
                @Override
                public void run() {
                    // Do something here on the main thread
                    Log.d("Handlers", "Called on main thread");
                    LogBaseline.updateDateTime("time", timeButton, notificationBundle);
                }
            };
            // Start the initial runnable task by posting through the handler
            handler.postDelayed(runnableCode, 1000);
            handler.postDelayed(runnableCode, 2500);
            handler.postDelayed(runnableCode, 5000);
            handler.postDelayed(runnableCode, 7500);
            handler.postDelayed(runnableCode, 10000);
            handler.postDelayed(runnableCode, 12500);
            handler.postDelayed(runnableCode, 15000);
            handler.postDelayed(runnableCode, 17500);
            handler.postDelayed(runnableCode, 20000);
        }
    };
    private View.OnClickListener dateButtonListener = new View.OnClickListener() {
        public void onClick(View v) {
            v.playSoundEffect(SoundEffectConstants.CLICK);
            notificationBundle.putString("button", "date");
            onButtonPressed(notificationBundle);
            // Define the code block to be executed
            Runnable runnableCode = new Runnable() {
                @Override
                public void run() {
                    // Do something here on the main thread
                    Log.d("Handlers", "Called on main thread");
                    LogBaseline.updateDateTime("date", dateButton, notificationBundle);
                }
            };
            // Start the initial runnable task by posting through the handler
            handler.postDelayed(runnableCode, 1000);
            handler.postDelayed(runnableCode, 2500);
            handler.postDelayed(runnableCode, 5000);
            handler.postDelayed(runnableCode, 7500);
            handler.postDelayed(runnableCode, 10000);
            handler.postDelayed(runnableCode, 12500);
            handler.postDelayed(runnableCode, 15000);
            handler.postDelayed(runnableCode, 17500);
            handler.postDelayed(runnableCode, 20000);
        }
    };

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Bundle bundle) {
        bundle.putBoolean("registration", registration);
        if (mListener != null) {
            mListener.messageFromNotificationSetupFragment(bundle);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        notificationBundle = new Bundle();
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        notificationBundle.clear();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void messageFromNotificationSetupFragment(Bundle bundle);
    }

    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    false);
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // Do something with the time chosen by the user
            Log.d("Debugging", "Hour: " + hourOfDay + " min: " + minute);
            notificationBundle.putInt("hour",hourOfDay);
            notificationBundle.putInt("minute", minute);
        }
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            Log.d("Debugging", "Year: " + year + " Month: " + month + " Day: " + day);
            notificationBundle.putInt("year", year);
            notificationBundle.putInt("month", month);
            notificationBundle.putInt("day", day);
        }
    }
}
