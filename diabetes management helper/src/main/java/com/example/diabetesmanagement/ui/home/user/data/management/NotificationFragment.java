package com.example.diabetesmanagement.ui.home.user.data.management;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.SoundEffectConstants;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.example.diabetesmanagement.MainActivity;
import com.example.diabetesmanagement.R;
import com.example.diabetesmanagement.data.Reminder;
import com.example.diabetesmanagement.data.User;
import com.example.diabetesmanagement.service.DeleteFragment;
import com.example.diabetesmanagement.service.UserStorage;

import java.util.ArrayList;

/*
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NotificationFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NotificationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NotificationFragment extends Fragment implements DeleteFragment.ButtonDialogListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ListView notificationList;
    private OnFragmentInteractionListener mListener;
    private User user;
    private ArrayList<Reminder> userReminders;
    private boolean hasNotifications = false;
    private Button addButton;
    private NotificationsAdapter adapter;
    private boolean registration;
    private Button nextButton;
    private Reminder toDelete;

    private Bundle notificationData;

    public NotificationFragment() {
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
    public static NotificationFragment newInstance(String param1, User param2, boolean param3) {
        NotificationFragment fragment = new NotificationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putSerializable(ARG_PARAM2, param2);
        args.putBoolean(ARG_PARAM3, param3);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            user = (User)getArguments().getSerializable(ARG_PARAM2);
            registration = getArguments().getBoolean(ARG_PARAM3);
            if (user.getReminders().size() != 0) {
                hasNotifications = true;
                Log.d("DEBUGGING", "Has notifications");
            }
            else {
                Log.d("DEBUGGING", "No existing notifications");
            }
            userReminders = user.getReminders();
        }
        Log.d("DEBUGGING", String.valueOf(hasNotifications));
        notificationData = new Bundle();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notification, container, false);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Create the adapter to convert the array to views
        adapter = new NotificationsAdapter(getContext(), userReminders, this);
        // Attach the adapter to a ListView
        notificationList = view.findViewById(R.id.list_notifications);
        notificationList.setAdapter(adapter);

        addButton = view.findViewById(R.id.button_new_notification);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.playSoundEffect(SoundEffectConstants.CLICK);
                editNotification(null, true);
            }
        });

        nextButton = view.findViewById(R.id.button_notification_next);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.playSoundEffect(SoundEffectConstants.CLICK);
                notificationData.putString("button", "next");
                onButtonPressed(notificationData);
            }
        });
        Button backButton = view.findViewById(R.id.button_notification_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.playSoundEffect(SoundEffectConstants.CLICK);
                notificationData.putString("button", "back");
                onButtonPressed(notificationData);
            }
        });
        if (registration) {
            nextButton.setVisibility(View.VISIBLE);
            backButton.setVisibility(View.VISIBLE);
        }
        if (userReminders.size() < 8)
            view.findViewById(R.id.frameLayout_notifications).setBackgroundColor(Color.parseColor("#F9F9F9"));
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Bundle bundle) {
        bundle.putBoolean("registration", registration);
        if (mListener != null) {
            mListener.messageFromNotificationFragment(bundle);
        }
    }

    public void editNotification(Reminder n, boolean newNotification) {
        notificationData.putBoolean("newNotification", newNotification);
        if (!newNotification)
            notificationData.putSerializable("notification", n);
        onButtonPressed(notificationData);
    }

    public void deleteNotification(Reminder n){
        toDelete = n;
        FragmentManager fm = getFragmentManager();
        DeleteFragment alert = new DeleteFragment("Are you sure to want to delete your reminder, " + n.getDescription() + "?");
        alert.setTargetFragment(NotificationFragment.this, 950);
        alert.show(fm, "reminderDeletePrompt");
        Log.d("REM_DELETE", "In deleteNotification");
    }

    @Override
    public void onDialogButtonPressed(boolean confirm){
        Reminder n = toDelete;
        Log.d("REM_DELETE", "In onDialogButtonPressed, was yes pushed? " + confirm);
        if (confirm) {
            n.cancelNotification(getContext());
            user.getReminders().remove(n);
            userReminders = user.getReminders();
            UserStorage.saveUser(user);
            adapter.remove(n);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
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
        for (Reminder n : userReminders) {
            n.setVisible(false);
            Log.d("DEBUGGING", "Reminder " + n.getDescription() + " V: " + n.isVisible()
                + " enabled: " + n.isEnabled());
        }
        UserStorage.saveUser(user);
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
        void messageFromNotificationFragment(Bundle bundle);
    }
}
