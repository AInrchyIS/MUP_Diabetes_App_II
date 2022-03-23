package com.example.diabetesmanagement.ui.home;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.diabetesmanagement.R;
import com.example.diabetesmanagement.data.Goal;
import com.example.diabetesmanagement.data.Medication;
import com.example.diabetesmanagement.data.User;

import java.util.ArrayList;
import java.util.Calendar;

public class LogFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";

    private LogViewModel mViewModel;
    private User user;
    private Goal activityGoal;
    private Goal bloodSugarGoal;
    private Goal insulinGoal;
    private Goal foodGoal;
    private Goal snackGoal;
    private Goal weightGoal;
    private Goal moodGoal;

    private TextView weeklyA1cView;
    private TextView monthlyA1cView;
    private TextView activityView;
    private TextView bloodSugarView;
    private TextView insulinView;
    private TextView medicineView;
    private TextView foodView;
    private TextView snackView;
    private TextView weightView;
    private TextView moodView;

    private int numGone = 0;

    public static LogFragment newInstance(User param1) {
        LogFragment fragment = new LogFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            user = (User)getArguments().getSerializable(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.log_fragment, container, false);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(LogViewModel.class);
        // TODO: Use the ViewModel
        //Log.d("DEBUGGING", " Weekly Avg: " + ((MainActivity)getActivity()).getUser().getWeeklyAvgBloodSugar());
        //Log.d("DEBUGGING", " Monthly Avg: " + ((MainActivity)getActivity()).getUser().getMonthlyAvgBloodSugar());
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView lastBloodSugar = view.findViewById(R.id.text_lastBloodSugar);
        if (user.getCurrentBloodSugar() != 0)
        {
            lastBloodSugar.setText("The last blood sugar you entered was " + user.getCurrentBloodSugar() + ".");
        }
        else
            lastBloodSugar.setVisibility(View.GONE);

        ArrayList<Goal> goals = user.getGoals();
        for (Goal goal:goals)
        {
            if (goal.resetWeeklyYet(Calendar.getInstance().getTime()))
                goal.resetWeekly();
            if (goal.resetDailyYet(Calendar.getInstance().getTime()))
                goal.resetDaily();
        }
        activityGoal = user.getGoal(Goal.ACTIVITY);
        bloodSugarGoal = user.getGoal(Goal.BLOODSUGAR);
        insulinGoal = user.getGoal(Goal.INSULIN);
        foodGoal = user.getGoal(Goal.FOODMEAL);
        snackGoal = user.getGoal(Goal.FOODSNACK);
        weightGoal = user.getGoal(Goal.WEIGHT);
        moodGoal = user.getGoal(Goal.MOOD);

        weeklyA1cView = view.findViewById(R.id.text_weeklyA1C);
        monthlyA1cView = view.findViewById(R.id.text_monthlyA1C);
        activityView = view.findViewById(R.id.text_activity_feedback);
        bloodSugarView = view.findViewById(R.id.text_bloodSugar_feedback);
        insulinView = view.findViewById(R.id.text_insulin_feedback);
        medicineView = view.findViewById(R.id.text_medicine_feedback);
        foodView = view.findViewById(R.id.text_food_feedback);
        snackView = view.findViewById(R.id.text_snack_feedback);
        weightView = view.findViewById(R.id.text_weight_feedback);
        moodView = view.findViewById(R.id.text_mood_feedback);
        if (!activityGoal.isActive())
            activityView.setVisibility(View.GONE);
        if (!bloodSugarGoal.isActive())
            bloodSugarView.setVisibility(View.GONE);
        if (!insulinGoal.isActive())
            insulinView.setVisibility(View.GONE);
        if (!foodGoal.isActive())
            foodView.setVisibility(View.GONE);
        if (!snackGoal.isActive())
            snackView.setVisibility(View.GONE);
        if (!weightGoal.isActive())
            weightView.setVisibility(View.GONE);
        if (!moodGoal.isActive())
            moodView.setVisibility(View.GONE);

        String builder;
        if (user.getLogsBloodSugar().size() > 2) {
            double a1c = user.weeklyAvgBloodSugar();
            builder = "Your average blood sugar for the past week is " + Math.round(a1c) + ".";
            //if (a1c <= 7)
            //    builder += " That's at or below 7%! Good job!";

            weeklyA1cView.setText(builder);

            a1c = user.monthlyAvgBloodSugar();
            builder = "Your average blood sugar for the past month is " + Math.round(a1c) + ".";
            //if (a1c <= 7)
            //    builder += " That's at or below 7%! Good job!";
            monthlyA1cView.setText(builder);
        }
        else {
            //weeklyA1cView.setText("You don't have enough blood sugar logs for a good A1C reading.");
            //monthlyA1cView.setText("Try to record your blood sugar more often so you can track how you're doing!");
            weeklyA1cView.setVisibility(View.GONE);
            monthlyA1cView.setVisibility(View.GONE);
        }

        if (activityGoal.weeklyGoalMet())
            activityView.setText("You've met your exercise goal for the week!  Great job!");
        else if (activityGoal.getWeeklyProgress()/(double)activityGoal.getWeeklyAmount() >= 0.5 )
            activityView.setText("You're more that halfway toward meeting your exercise goal for the week!  Keep it up!");
        else if (activityGoal.getWeeklyProgress() > 1)
            activityView.setText("Good job on the " + activityGoal.getWeeklyProgress() +" minutes of exercise this week!");
       else
            activityView.setVisibility(View.GONE);

        if (bloodSugarGoal.weeklyGoalMet())
            bloodSugarView.setText("You've met your goal for recording blood sugar for the week!  Great job!");
        else if (bloodSugarGoal.getWeeklyProgress() > 1)
            bloodSugarView.setText("Good job on the " + bloodSugarGoal.getWeeklyProgress() + " blood sugar entries this week!");
        else if (bloodSugarGoal.getWeeklyProgress() > 0)
            bloodSugarView.setText("Good job on the blood sugar entry this week!");
        else
           bloodSugarView.setVisibility(View.GONE);

        if (insulinGoal.weeklyGoalMet())
           insulinView.setText("You've met your goal for recording insulin taken for the week!  Great job!");
        else if (insulinGoal.getWeeklyProgress() > 1)
           insulinView.setText("Good job on the " + insulinGoal.getWeeklyProgress() + " insulin entries this week!");
        else if (insulinGoal.getWeeklyProgress() > 0)
            insulinView.setText("Good job on the insulin entry this week!");
        else
            insulinView.setVisibility(View.GONE);
        //ArrayList<Goal> medGoals = new ArrayList<>();
        StringBuilder medString = new StringBuilder();
        Calendar cal = Calendar.getInstance();
        for (Goal g: user.getGoals()){
            if( g.getType().contains("Medication") && g.getDailyAmount() > 0) {
                //medGoals.add(g);
                medicineView.setVisibility(View.VISIBLE);
                Medication m = user.getMedication(g.getType().substring(12));
                if (!m.getName().equals("None")) {
                    if (g.getWeeklyProgress() >= cal.get(Calendar.DAY_OF_WEEK)) {
                        medString.append("You've taken your medication, ");
                        medString.append(m.getName());
                        medString.append(", every day this week! Great job! \n\n");
                    }
                    else if (g.getWeeklyProgress() > 1){
                        medString.append("Good job taking all of your medication, ");
                        medString.append(m.getName());
                        medString.append(", on ");
                        medString.append(g.getWeeklyProgress());
                        medString.append(" days this week! \n\n");
                    }
                    else if (g.getWeeklyProgress() == 1){
                        medString.append("Good job taking all of your medication, ");
                        medString.append(m.getName());
                        medString.append(", on ");
                        medString.append(g.getWeeklyProgress());
                        medString.append(" day this week! \n\n");
                    }
                    else {
                        medString.append("Try to take your medication, ");
                        medString.append(m.getName());
                        medString.append(", every day, if you can.\n\n");
                    }
                }
            }
        }
        medicineView.setText(medString.toString().trim());

        if (foodGoal.getWeeklyProgress() >= cal.get(Calendar.DAY_OF_WEEK))
           foodView.setText("You've met your meal goal every day this week!  Great job!");
        else if (foodGoal.getWeeklyProgress() > 1)
           foodView.setText("Good job on meeting your meal goal " + foodGoal.getWeeklyProgress() + " days this week!");
        else
            foodView.setVisibility(View.GONE);

        if (snackGoal.getWeeklyProgress() >= cal.get(Calendar.DAY_OF_WEEK))
            snackView.setText("You've met your snack goal every day this week!  Great job!");
        else if (snackGoal.getWeeklyProgress() > 1)
            snackView.setText("Good job on meeting your snack goal " + snackGoal.getWeeklyProgress() + " days this week!");
        else
            snackView.setVisibility(View.GONE);

        if (weightGoal.weeklyGoalMet())
            weightView.setText("You've met your goal for recording your weight for the week!  Great job!");
        else if (weightGoal.getWeeklyProgress() > 1)
            weightView.setText("Good job on the " + weightGoal.getWeeklyProgress() + " weight entries this week!");
        else if (weightGoal.getWeeklyProgress() > 0)
            weightView.setText("Good job on the weight entry this week!");
        else
            weightView.setVisibility(View.GONE);

        if (moodGoal.weeklyGoalMet())
            moodView.setText("You've met your goal for recording your mood for the week!  Great job!");
        else if (moodGoal.getWeeklyProgress() > 1)
            moodView.setText("Good job on the " + moodGoal.getWeeklyProgress() + " mood entries this week!");
        else if (moodGoal.getWeeklyProgress() > 0)
            moodView.setText("Good job on the mood entry this week!");
        else
            moodView.setVisibility(View.GONE);

        /*
        private TextView weeklyA1cView;
        private TextView monthlyA1cView;
        private TextView activityView;
        private TextView bloodSugarView;
        private TextView insulinView;
        private TextView medicineView;
        private TextView foodView;
        private TextView weightView;
        private TextView moodView;
         */

        if (weeklyA1cView.getVisibility() == View.GONE)
            numGone++;
        if (monthlyA1cView.getVisibility() == View.GONE)
            numGone++;
        if (activityView.getVisibility() == View.GONE)
            numGone++;
        if (bloodSugarView.getVisibility() == View.GONE)
            numGone++;
        if (insulinView.getVisibility() == View.GONE)
            numGone++;
        if (medicineView.getVisibility() == View.GONE)
            numGone++;
        if (foodView.getVisibility() == View.GONE)
            numGone++;
        if (snackView.getVisibility() == View.GONE)
            numGone++;
        if (weightView.getVisibility() == View.GONE)
            numGone++;
        if (moodView.getVisibility() == View.GONE)
            numGone++;

        if (numGone > 5)
        {
            TextView noFeedback = view.findViewById(R.id.text_no_feedback);
            noFeedback.setText("Make sure to start entering logs so you can get feedback on how you're doing!");
            noFeedback.setVisibility(View.VISIBLE);
        }
    }

}
