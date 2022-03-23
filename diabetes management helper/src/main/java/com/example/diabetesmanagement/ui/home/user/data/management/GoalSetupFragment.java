package com.example.diabetesmanagement.ui.home.user.data.management;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.SoundEffectConstants;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.diabetesmanagement.R;
import com.example.diabetesmanagement.data.Goal;
import com.example.diabetesmanagement.data.User;
import com.example.diabetesmanagement.ui.home.log.entry.LogBaseline;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link GoalSetupFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GoalSetupFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GoalSetupFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";

    private Goal goal;
    private User user;
    private TextView goalType;
    private EditText goalDaily;
    private EditText goalWeekly;
    private EditText goalMeal;
    private EditText goalSnack;
    private Button cancel;
    private Button save;
    private Bundle goalBundle;
    private boolean registration;

    private OnFragmentInteractionListener mListener;

    public GoalSetupFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GoalSetupFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GoalSetupFragment newInstance(User param1, Goal param2, boolean param3) {
        GoalSetupFragment fragment = new GoalSetupFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, param1);
        args.putSerializable(ARG_PARAM2, param2);
        args.putBoolean(ARG_PARAM3, param3);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            user = (User) getArguments().getSerializable(ARG_PARAM1);
            goal = (Goal) getArguments().getSerializable(ARG_PARAM2);
            registration = getArguments().getBoolean(ARG_PARAM3);
        }
        goalBundle = new Bundle();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_goal_setup, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        goalType = view.findViewById(R.id.label_goal_type);
        goalDaily = view.findViewById(R.id.data_daily_goal);
        goalWeekly = view.findViewById(R.id.data_weekly_goal);
        goalMeal = view.findViewById(R.id.data_meal_goal);
        goalSnack = view.findViewById(R.id.data_snack_goal);

        if (goal.getType().contains("Medication")) {
            goalWeekly.setVisibility(View.INVISIBLE);
            view.findViewById(R.id.label_weekly_goal).setVisibility(View.INVISIBLE);
        }
        if (goal.getType().equals(Goal.FOODMEAL)) {
            goalDaily.setVisibility(View.GONE);
            view.findViewById(R.id.label_daily_goal).setVisibility(View.GONE);
            goalWeekly.setVisibility(View.GONE);
            view.findViewById(R.id.label_weekly_goal).setVisibility(View.GONE);

            goalMeal.setVisibility(View.VISIBLE);
            view.findViewById(R.id.label_meal_goal).setVisibility(View.VISIBLE);
        }
        if (goal.getType().equals(Goal.FOODSNACK)) {
            goalDaily.setVisibility(View.GONE);
            view.findViewById(R.id.label_daily_goal).setVisibility(View.GONE);
            goalWeekly.setVisibility(View.GONE);
            view.findViewById(R.id.label_weekly_goal).setVisibility(View.GONE);

            goalSnack.setVisibility(View.VISIBLE);
            view.findViewById(R.id.label_snack_goal).setVisibility(View.VISIBLE);
        }

        String s = goalType.getText().toString() + "  " + goal.getType();
        if (goal.getType().equals(Goal.ACTIVITY))
            s += " (minutes of activity)";
        else if (goal.getType().equals(Goal.FOODMEAL))
            s = goalType.getText().toString() + "  Meals eaten per day";
        else if (goal.getType().equals(Goal.FOODSNACK))
            s = goalType.getText().toString() + "  Snacks eaten per day";
        else if (goal.getType().contains("Medication"))
            s += " (doses taken per day)";
        else
            s += " (number of log entries)";
        goalType.setText(s);
        goalDaily.setText(String.valueOf(goal.getDailyAmount()));
        goalWeekly.setText(String.valueOf(goal.getWeeklyAmount()));
        goalMeal.setText(String.valueOf(goal.getMealAmount()));
        goalSnack.setText(String.valueOf(goal.getSnackAmount()));

        cancel = view.findViewById(R.id.button_goal_cancel);
        save = view.findViewById(R.id.button_goal_save);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.playSoundEffect(SoundEffectConstants.CLICK);
                goalBundle.putString("button", "cancel");
                onButtonPressed(goalBundle);
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.playSoundEffect(SoundEffectConstants.CLICK);
                if (goalDaily.getText().toString().equals(""))
                    goalDaily.setText(0);
                if (goalWeekly.getText().toString().equals(""))
                    goalWeekly.setText(0);
                if (goalMeal.getText().toString().equals(""))
                    goalMeal.setText(0);
                if (goalSnack.getText().toString().equals(""))
                    goalSnack.setText(0);

                goal.setDailyAmount(Integer.parseInt(goalDaily.getText().toString()));
                goal.setWeeklyAmount(Integer.parseInt(goalWeekly.getText().toString()));
                goal.setMealAmount(Integer.parseInt(goalMeal.getText().toString()));
                goal.setSnackAmount(Integer.parseInt(goalSnack.getText().toString()));
                goalBundle.putString("button", "save");
                goalBundle.putSerializable("goal", goal);
                onButtonPressed(goalBundle);
                /*LogBaseline.fireBaseDoneBooleanBtn = true;*/
            }
        });


    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Bundle bundle) {
        bundle.putBoolean("registration", registration);
        if (mListener != null) {
            mListener.messageFromGoalSetupFragment(bundle);
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

        void messageFromGoalSetupFragment(Bundle bundle);
    }
}
