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
import com.example.diabetesmanagement.data.Goal;

import java.util.ArrayList;
import java.util.Calendar;


public class GoalsAdapter extends ArrayAdapter<Goal> {

    private GoalFragment hostFragment;

    public GoalsAdapter(Context context, ArrayList<Goal> users, GoalFragment parent) {
        super(context, 0, users);
        hostFragment = parent;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Goal goal = getItem(position);

        if (goal.resetDailyYet(Calendar.getInstance().getTime()))
            goal.resetDaily();
        if (goal.resetWeeklyYet(Calendar.getInstance().getTime()))
            goal.resetWeekly();

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_goal, parent, false);
        }
        // Lookup view for data population
        TextView goalType = convertView.findViewById(R.id.list_goal_type);
        TextView dailyGoal = convertView.findViewById(R.id.list_goal_daily_progress);
        TextView weeklyGoal = convertView.findViewById(R.id.list_goal_weekly_progress);
        //Button deleteButton = convertView.findViewById(R.id.button_goal_delete);
        final Button editButton = convertView.findViewById(R.id.button_goal_edit);

        //final LinearLayout buttonBar = convertView.findViewById(R.id.goal_buttons);
        if (!goal.isVisible())
            editButton.setVisibility(View.GONE);
        else
            editButton.setVisibility(View.VISIBLE);
        // Populate the data into the template view using the data object
        String s;
        s = goal.getType() + " goal";
        goalType.setText(s);
        s = "Daily goal: " + goal.getDailyProgress() + " of " + goal.getDailyAmount();
        if (goal.getType().equals(Goal.ACTIVITY))
            s += " minutes";
        else if (goal.getType().contains("Medication"))
            s+= " doses taken";
        else if (goal.getType().equals(Goal.FOODMEAL))
            s = "Meal goal: " + goal.getMealsEaten() + " of " + goal.getMealAmount() + " meals eaten";
        else if (goal.getType().equals(Goal.FOODSNACK))
            s = "Snack goal: " + goal.getSnacksEaten() + " of " + goal.getSnackAmount() + " snacks eaten";
        else
            s += " log entries";
        dailyGoal.setText(s);
        s = "Weekly goal: " + goal.getWeeklyProgress() + " of " + goal.getWeeklyAmount();
        if (goal.getType().equals(Goal.ACTIVITY))
            s += " minutes";
        else
            s += " log entries";
        if (goal.getType().equals(Goal.ACTIVITY)) {
            weeklyGoal.setText(s);
            weeklyGoal.setVisibility(View.VISIBLE);
        }
        else if (goal.getType().contains("Medication") || goal.getType().equals(Goal.FOODSNACK)|| goal.getType().equals(Goal.FOODMEAL))
            weeklyGoal.setVisibility(View.GONE);
        else
            weeklyGoal.setText(s);

       goalType.setTag(position);
        // Attach the click event handler
        goalType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.playSoundEffect(SoundEffectConstants.CLICK);
                int position = (Integer) view.getTag();
                // Access the row position here to get the correct data item
                Goal goal = getItem(position);
                // Do what you want here...
                Log.d("DEBUGGING", "Clicked on: " + goal.getType());

                //hide or show button bar
                if (goal.isVisible()) {
                   goal.setVisible(false);
                   editButton.setVisibility(View.GONE);
                }
                else {
                    goal.setVisible(true);
                    editButton.setVisibility(View.VISIBLE);
                }
            }
        });

        dailyGoal.setTag(position);
        // Attach the click event handler
        dailyGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.playSoundEffect(SoundEffectConstants.CLICK);
                int position = (Integer) view.getTag();
                // Access the row position here to get the correct data item
                Goal goal = getItem(position);
                // Do what you want here...
                Log.d("DEBUGGING", "Clicked on: " + goal.getType());

                //hide or show button bar
                if (goal.isVisible()) {
                    goal.setVisible(false);
                    editButton.setVisibility(View.GONE);
                }
                else {
                    goal.setVisible(true);
                    editButton.setVisibility(View.VISIBLE);
                }
            }
        });

        weeklyGoal.setTag(position);
        // Attach the click event handler
       weeklyGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.playSoundEffect(SoundEffectConstants.CLICK);
                int position = (Integer) view.getTag();
                // Access the row position here to get the correct data item
                Goal goal = getItem(position);
                // Do what you want here...
                Log.d("DEBUGGING", "Clicked on: " + goal.getType());

                //hide or show button bar
                if (goal.isVisible()) {
                    goal.setVisible(false);
                    editButton.setVisibility(View.GONE);
                }
                else {
                    goal.setVisible(true);
                    editButton.setVisibility(View.VISIBLE);
                }
            }
        });
        /*
        deleteButton.setTag(position);
        // Attach the click event handler
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = (Integer) view.getTag();
                // Access the row position here to get the correct data item
                Goal goal = getItem(position);
                // Do what you want here...
                Log.d("DEBUGGING", "Clicked on delete for: " + goal.getType());
                hostFragment.deleteGoal(goal);
            }
        });
        */
        editButton.setTag(position);
        // Attach the click event handler
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.playSoundEffect(SoundEffectConstants.CLICK);
                int position = (Integer) view.getTag();
                // Access the row position here to get the correct data item
                Goal goal = getItem(position);
                // Do what you want here...
                Log.d("DEBUGGING", "Clicked on edit for: " + goal.getType());
                hostFragment.editGoal(goal);
            }
        });

        // Return the completed view to render on screen
        return convertView;
    }

}
