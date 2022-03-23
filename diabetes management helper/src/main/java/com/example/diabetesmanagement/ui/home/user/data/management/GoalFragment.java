package com.example.diabetesmanagement.ui.home.user.data.management;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.SoundEffectConstants;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.example.diabetesmanagement.R;
import com.example.diabetesmanagement.data.Goal;
import com.example.diabetesmanagement.data.User;
import com.example.diabetesmanagement.service.UserStorage;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link GoalFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GoalFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GoalFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private User user;
    private ArrayList<Goal> userGoals;
    private boolean hasGoals = false;
    private ListView goalList;
    private Button nextButton;
    private GoalsAdapter adapter;
    private Bundle goalData;
    private boolean registration;

    public GoalFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GoalFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GoalFragment newInstance(String param1, User param2, boolean param3) {
        GoalFragment fragment = new GoalFragment();
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
            userGoals = user.getGoals();
        }
        goalData = new Bundle();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_goal, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        for (Goal goal:userGoals)
        {
            if (goal.resetWeeklyYet(Calendar.getInstance().getTime()))
                goal.resetWeekly();
            if (goal.resetDailyYet(Calendar.getInstance().getTime()))
                goal.resetDaily();
        }
        UserStorage.saveUser(user);
        // Create the adapter to convert the array to views
        adapter = new GoalsAdapter(getContext(), userGoals, this);
        // Attach the adapter to a ListView
        goalList = view.findViewById(R.id.list_goals);
        goalList.setAdapter(adapter);
        nextButton = view.findViewById(R.id.button_goal_next);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.playSoundEffect(SoundEffectConstants.CLICK);
                goalData.putString("button", "next");
                onButtonPressed(goalData);
            }
        });
        Button backButton = view.findViewById(R.id.button_goal_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.playSoundEffect(SoundEffectConstants.CLICK);
                goalData.putString("button", "back");
                onButtonPressed(goalData);
            }
        });
        if (registration) {
            nextButton.setVisibility(View.VISIBLE);
            backButton.setVisibility(View.VISIBLE);
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Bundle bundle) {
        bundle.putBoolean("registration", registration);
        if (mListener != null) {
            mListener.messageFromGoalFragment(bundle);
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


    public void editGoal(Goal g) {
        goalData.putSerializable("goal", g);
        onButtonPressed(goalData);
    }
    /*
    public void deleteGoal(Goal g){
        user.getGoals().remove(g);
        userGoals = user.getGoals();
        adapter.remove(g);
        adapter.notifyDataSetChanged();
    }*/

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        for (Goal g : userGoals) {
            g.setVisible(false);
        }
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
        void messageFromGoalFragment(Bundle bundle);
    }
}
