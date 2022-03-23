package com.example.diabetesmanagement.ui.home.log.entry;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.SoundEffectConstants;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.example.diabetesmanagement.MainActivity;
import com.example.diabetesmanagement.R;
import com.example.diabetesmanagement.constatntClass.ConstantClass;

import com.example.diabetesmanagement.data.Medication;
import com.example.diabetesmanagement.data.User;
import com.example.diabetesmanagement.ui.home.user.data.management.MedicationFragment;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LogBaseline.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LogBaseline#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LogBaseline extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private static Bundle logData;
    private EditText notes;
    private String mParam1;
    private Button time;
    private Button date;
    private User user;
    public static int totalValues;

    private Handler handler;
    public static boolean fireBaseDoneBooleanBtn = false;

    private OnFragmentInteractionListener mListener;

    public LogBaseline() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment LogBaseline.
     */
    // TODO: Rename and change types and number of parameters
    public static LogBaseline newInstance(String param1, User param2) {
        LogBaseline fragment = new LogBaseline();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putSerializable(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            user = (User) getArguments().getSerializable(ARG_PARAM2);
        }
        handler = new Handler();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_log_baseline, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Bundle bundle) {

        if (mListener != null) {
            mListener.messageFromLogBaseline(bundle);
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Fragment innerLog;
        switch (mParam1) {
            case "bloodSugar":
                innerLog = new BloodSugarFragment();
                break;
            case "insulin":
                innerLog = InsulinFragment.newInstance(user, mParam1);
                break;
            case "medicine":
                innerLog = MedicineFragment.newInstance(user, mParam1);
                break;
            case "food":
                innerLog = FoodFragment.newInstance(user, mParam1);
                break;
            case "weight":
                innerLog = new WeightFragment();
                break;
            case "mood":
                innerLog = new MoodFragment();
                break;
            case "activity":
                innerLog = new ActivityFragment();
                break;
            default:
                innerLog = new BloodSugarFragment();

        }

        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.logContainer, innerLog, mParam1).commit();

        view.findViewById(R.id.button_time).setOnClickListener(timeButtonListener);
        view.findViewById(R.id.button_date).setOnClickListener(dateButtonListener);
        view.findViewById(R.id.button_cancel).setOnClickListener(cancelButtonListener);
        view.findViewById(R.id.button_enter).setOnClickListener(enterButtonListener);

        //get current date/time values
        Calendar c = Calendar.getInstance();
        logData.putInt("hour", c.get(Calendar.HOUR_OF_DAY));
        logData.putInt("minute", c.get(Calendar.MINUTE));
        logData.putInt("year", c.get(Calendar.YEAR));
        logData.putInt("month", c.get(Calendar.MONTH));
        logData.putInt("day", c.get(Calendar.DAY_OF_MONTH));

        DateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.US);
        DateFormat timeFormat = new SimpleDateFormat("hh:mm a", Locale.US);

        time = view.findViewById(R.id.button_time);
        time.setText(timeFormat.format(c.getTime()));
        date = view.findViewById(R.id.button_date);
        date.setText(dateFormat.format(c.getTime()));


    }

    public void replaceFragment(String s) {
        Fragment innerLog;
        switch (s) {
            case "bloodSugar":
                innerLog = new BloodSugarFragment();
                break;
            case "insulin":
                innerLog = InsulinFragment.newInstance(user, s);
                break;
            case "medicine":
                innerLog = MedicineFragment.newInstance(user, s);
                break;
            case "food":
                innerLog = FoodFragment.newInstance(user, s);
                break;
            case "weight":
                innerLog = new WeightFragment();
                break;
            case "mood":
                innerLog = new MoodFragment();
                break;
            case "activity":
                innerLog = new ActivityFragment();
                break;
            default:
                innerLog = new BloodSugarFragment();

        }

        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.logContainer, innerLog, s).commit();
        mParam1 = s;
    }

    // Create an anonymous implementation of OnClickListener
    private View.OnClickListener timeButtonListener = new View.OnClickListener() {
        public void onClick(View v) {
            v.playSoundEffect(SoundEffectConstants.CLICK);
            logData.putString("button", "time");
            onButtonPressed(logData);
            // Define the code block to be executed
            Runnable runnableCode = new Runnable() {
                @Override
                public void run() {
                    // Do something here on the main thread
                    Log.d("Handlers", "Called on main thread");
                    updateDateTime("time", time, logData);
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
            logData.putString("button", "date");
            onButtonPressed(logData);
            // Define the code block to be executed
            Runnable runnableCode = new Runnable() {
                @Override
                public void run() {
                    // Do something here on the main thread
                    Log.d("Handlers", "Called on main thread");
                    updateDateTime("date", date, logData);
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
    private View.OnClickListener cancelButtonListener = new View.OnClickListener() {
        public void onClick(View v) {
            v.playSoundEffect(SoundEffectConstants.CLICK);
            logData.putString("button", "cancel");
            fireBaseDoneBooleanBtn = false;
            SharedPreferences sharedPreferences = getContext().getSharedPreferences("counterText", 0);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            if (sharedPreferences.getBoolean(ConstantClass.counterText, false) == true) {
                editor.putBoolean(ConstantClass.counterText, false);
                editor.apply();
            }
            onButtonPressed(logData);
        }
    };
    private View.OnClickListener enterButtonListener = new View.OnClickListener() {
        public void onClick(View v) {
            v.playSoundEffect(SoundEffectConstants.CLICK);
            if (ActivityFragment.activityValue || BloodSugarFragment.bloodSugarValue
                    || FoodFragment.foodValue || InsulinFragment.insulinValue || MedicineFragment.medicineValue
                    || MoodFragment.moodValue || WeightFragment.weightValue) {
                fireBaseDoneBooleanBtn = true;
                Log.d("bloodSugarValue", "onClick: "+BloodSugarFragment.bloodSugarValue);
            }

            Calendar c = Calendar.getInstance();
            c.set(logData.getInt("year"), logData.getInt("month"), logData.getInt("day"),
                    logData.getInt("hour"), logData.getInt("minute"), 0);
            if (c.getTimeInMillis() <= Calendar.getInstance().getTimeInMillis()) {
                logData.putBoolean("validInput", true);
                logData.putString("type", mParam1);
                logData.putString("button", "submit");
                onButtonPressed(logData);
            } else {
                ((MainActivity) getActivity()).setMessage("futureLogError");
            }
        }
    };

    public User getUser() {
        return user;
    }

    public static void updateDateTime(String s, View v, Bundle bundle) {
        Log.d("DEBUGGING", "in updateDateTime() S:" + s + " and time is null =  " + (v == null));
        if (s.equals("time") && v != null) {
            Log.d("DEBUGGING", "changing displayed time");
            int hour = bundle.getInt("hour");
            String minute = bundle.getInt("minute") + "";
            String timeString;

            if (bundle.getInt("minute") < 10)
                minute = "0" + minute;
            if (hour == 0)
                timeString = "12:" + minute + " AM";
            else if (hour == 12)
                timeString = "12:" + minute + " PM";
            else if (hour > 12)
                timeString = (hour - 12) + ":" + minute + " PM";
            else
                timeString = hour + ":" + minute + " AM";
            ((Button) v).setText(timeString);
        }
        if (s.equals("date") && v != null) {
            Calendar c = Calendar.getInstance();
            c.set(bundle.getInt("year"), bundle.getInt("month"), bundle.getInt("day"));
            DateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.US);
            ((Button) v).setText(dateFormat.format(c.getTime()));
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        logData = new Bundle();
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
        logData.clear();
        mListener = null;
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
        void messageFromLogBaseline(Bundle bundle);
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
            logData.putInt("hour", hourOfDay);
            logData.putInt("minute", minute);
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
            logData.putInt("year", year);
            logData.putInt("month", month);
            logData.putInt("day", day);
        }
    }
}

