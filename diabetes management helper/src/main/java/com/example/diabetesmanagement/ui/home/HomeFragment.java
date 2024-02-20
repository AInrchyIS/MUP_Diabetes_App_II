package com.example.diabetesmanagement.ui.home;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SoundEffectConstants;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.diabetesmanagement.MainActivity;
import com.example.diabetesmanagement.R;
import com.example.diabetesmanagement.data.Goal;
import com.example.diabetesmanagement.data.User;
import com.example.diabetesmanagement.ui.home.log.entry.BloodSugarFragment;
import com.example.diabetesmanagement.ui.home.log.entry.LogBaseline;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;

public class HomeFragment extends Fragment {

    private static final String TAG = "Home Fragment";
    private HomeViewModel mViewModel;
    private static final String ARG_PARAM1 = "param1";
    private User user;
    private ArrayList<Goal> userGoals;
    TextView totalPointsTextView;
    TextView todayPointsTextView;
    TextView badgeTextView;

    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mDatabaseReference;
    ImageView imageBloodsugardImageviewBeforeMeal, imageBloodsugardImageviewAfterMeal, imageBloodsugardImageviewBeforeBed,
            imageMedicalImageview, imageInsulinImageview,
            imageFoodImageviewMeal, imageFoodImageviewSnack,
            imageActivityImageview, imageWeightImageview, imageMoodImageview;
    Calendar c;

    private HomeFragment.OnFragmentInteractionListener mListener;

    public static HomeFragment newInstance(User param1) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.home_fragment, container, false);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getArguments() != null) {
            user = (User) getArguments().getSerializable(ARG_PARAM1);
        }
        mViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        // TODO: Use the ViewModel
    }


    // Create an anonymous implementation of OnClickListener
    private View.OnClickListener mButtonListener = new View.OnClickListener() {
        public void onClick(View v) {
            v.playSoundEffect(SoundEffectConstants.CLICK);
            String s = "";
            switch (v.getId()) {
                case R.id.button_bloodSugar:
                    s = "bloodSugar";
                    break;
                case R.id.button_food:
                    s = "food";
                    break;
                case R.id.button_weight:
                    s = "weight";
                    break;
                case R.id.button_mood:
                    s = "mood";
                    break;
                case R.id.button_activity:
                    s = "activity";
                    break;
                case R.id.button_resource:
                    s = "resources";
                    break;
            }

            onButtonPressed(Uri.parse(s));
        }
    };

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //if (view.getId() == R.id.button_logout) {
        view.findViewById(R.id.button_bloodSugar).setOnClickListener(mButtonListener);
        view.findViewById(R.id.button_food).setOnClickListener(mButtonListener);
        view.findViewById(R.id.button_weight).setOnClickListener(mButtonListener);
        view.findViewById(R.id.button_mood).setOnClickListener(mButtonListener);
        view.findViewById(R.id.button_activity).setOnClickListener(mButtonListener);

//        imageBloodsugardImageviewBeforeMeal = view.findViewById(R.id.imageViewBloodSugarBeforeMeal);
//        imageBloodsugardImageviewAfterMeal = view.findViewById(R.id.imageViewBloodSugarAfterMeal);
//        imageBloodsugardImageviewBeforeBed = view.findViewById(R.id.imageViewBloodSugarBeforeBed);
//        imageMedicalImageview = view.findViewById(R.id.imageView_medicine);
//        imageInsulinImageview = view.findViewById(R.id.imageView_insulin);
//        imageFoodImageviewMeal = view.findViewById(R.id.imageView_food_meal);
//        imageFoodImageviewSnack = view.findViewById(R.id.imageView_food_snack);
//        imageActivityImageview = view.findViewById(R.id.imageView_activity);
//        imageWeightImageview = view.findViewById(R.id.imageView_weight);
//        imageMoodImageview = view.findViewById(R.id.imageView_mood);
        badgeTextView = view.findViewById(R.id.badges_collected_text);

        totalPointsTextView = view.findViewById(R.id.counter_textview);
        todayPointsTextView = view.findViewById(R.id.todayTaskTextView);

        totalPointsTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlertDialogTotalPoints();
            }
        });

        badgeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlertDialogTotalBadges();
            }
        });

        // create firestore instance and reference to collection and document
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference();

        c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY,13);
        c.set(Calendar.MINUTE,11);


//        final SharedPreferences sharedPreferences = getContext().getSharedPreferences("counterText", 0);
//        final SharedPreferences.Editor editor = sharedPreferences.edit();

        // grab current user id from firebase auth
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        // grab data from documents of firestore of the current user
        FirebaseFirestore.getInstance().collection("users")
                .document(uid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                try {
                    User userData = documentSnapshot.toObject(User.class);
                    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

                    userGoals = userData.getGoals();

                    if(userData.getGoalReset()==null){
                        Calendar c = Calendar.getInstance();
                        c.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE), 0, 0);
                        c.add(Calendar.DATE, 1);
                        userData.setGoalReset(c.getTime());
                    }

                    Calendar reset = Calendar.getInstance();
                    reset.setTime(userData.getGoalReset());
                    if(reset.getTimeInMillis() < Calendar.getInstance().getTimeInMillis()){
                        for (Goal goal:userGoals)
                        {
                            goal.resetDaily();
                        }

                        reset.add(Calendar.DATE,1);
                        System.out.println("Date of next reset: "+ userData.getGoalReset());

                        userData.setGoalReset(reset.getTime());
                        userData.setToday(false);
                    }

                    System.out.println("after resetting"+userData.getToday());
                    System.out.println("Date of next reset after resetting: "+ userData.getGoalReset());

                    int totalValues = userData.getPoints();
                    boolean todayBadge = userData.getToday();
                    int badgeValue = userData.getBadges();
                    Log.d(TAG, "Badge Value : "+badgeValue);
                    int amount = 0;
                    int progress = 0;
                    LinearLayout layout = view.findViewById(R.id.imageLinear);
                    for (Goal goal : userGoals) {
                        if (goal.isActive()) {
                            if (goal.getType().equals(Goal.ACTIVITY)) {
                                ImageView image = new ImageView(getContext());
                                image.setLayoutParams(new android.view.ViewGroup.LayoutParams(80, 60));
                                image.setMaxHeight(20);
                                image.setMaxWidth(20);
                                if (goal.getDailyProgress() > 0) {
                                    image.setImageResource(R.drawable.run1);
                                } else
                                    image.setImageResource(R.drawable.run);
                                // Adds the view to the layout
                                layout.addView(image);
                            }
                            if (goal.getType().equals(Goal.BLOODSUGAR)) {
                                amount = goal.getDailyAmount();
                                progress = goal.getDailyProgress();
                                for (int i = 1; i <= amount; i++) {
                                    ImageView image = new ImageView(getContext());
                                    image.setLayoutParams(new android.view.ViewGroup.LayoutParams(80, 60));
                                    image.setMaxHeight(20);
                                    image.setMaxWidth(20);
                                    if (progress >= i)
                                        image.setImageResource(R.drawable.drop1);
                                    else if (progress < i)
                                        image.setImageResource(R.drawable.drop);
                                    // Adds the view to the layout
                                    layout.addView(image);
                                }
                            }
                            if (goal.getType().equals(Goal.INSULIN)) {
                                amount = goal.getDailyAmount();
                                System.out.println("insulin" + amount);
                                progress = goal.getDailyProgress();
                                for (int i = 1; i <= amount; i++) {
                                    ImageView image = new ImageView(getContext());
                                    image.setLayoutParams(new android.view.ViewGroup.LayoutParams(80, 60));
                                    image.setMaxHeight(20);
                                    image.setMaxWidth(20);
                                    if (progress >= i)
                                        image.setImageResource(R.drawable.medical1);
                                    else if (progress < i)
                                        image.setImageResource(R.drawable.medical);

                                    // Adds the view to the layout
                                    layout.addView(image);
                                }
                            }

                            if (goal.getType().equals(Goal.MEDICINE)) {
                                amount = goal.getDailyAmount();
                                progress = goal.getDailyProgress();
                                System.out.println("medicine" + amount);
                                Log.d("GetDailyAmount", "onSuccess: "+progress);
                                for (int i = 1; i <= amount; i++) {
                                    ImageView image = new ImageView(getContext());
                                    image.setLayoutParams(new android.view.ViewGroup.LayoutParams(80, 60));
                                    image.setMaxHeight(20);
                                    image.setMaxWidth(20);
                                    if (progress >= i)
                                        image.setImageResource(R.drawable.medical1);
                                     else if (progress < i)
                                        image.setImageResource(R.drawable.medical);
                                    // Adds the view to the layout
                                    layout.addView(image);
                                }
                            }

                            if (goal.getType().equals(Goal.FOODMEAL)) {
                                amount = goal.getMealAmount();
                                progress = goal.getMealsEaten();
                                System.out.println("foodmeal" + amount);
                                for (int i = 1; i <= amount; i++) {
                                    ImageView image = new ImageView(getContext());
                                    image.setLayoutParams(new android.view.ViewGroup.LayoutParams(80, 60));
                                    image.setMaxHeight(20);
                                    image.setMaxWidth(20);
                                    if (progress >= i)
                                        image.setImageResource(R.drawable.fork1);
                                    else if (progress < i)
                                        image.setImageResource(R.drawable.fork);
                                    // Adds the view to the layout
                                    layout.addView(image);

                                }
                            }
                            if (goal.getType().equals(Goal.FOODSNACK)) {
                                amount = goal.getSnackAmount();
                                progress = goal.getSnacksEaten();
                                System.out.println("Foodsnack" + amount);
                                for (int i = 1; i <= amount; i++) {
                                    ImageView image = new ImageView(getContext());
                                    image.setLayoutParams(new android.view.ViewGroup.LayoutParams(80, 60));
                                    image.setMaxHeight(20);
                                    image.setMaxWidth(20);
                                    if (progress >= i)
                                        image.setImageResource(R.drawable.fork1);
                                    else if (progress < i)
                                        image.setImageResource(R.drawable.fork);
                                    // Adds the view to the layout
                                    layout.addView(image);
                                }
                            }
                            if (goal.getType().equals(Goal.WEIGHT)) {
                                amount = goal.getDailyAmount();
                                progress = goal.getDailyProgress();
                                System.out.println("Weight" + amount);
                                for (int i = 1; i <= amount; i++) {
                                    ImageView image = new ImageView(getContext());
                                    image.setLayoutParams(new android.view.ViewGroup.LayoutParams(80, 60));
                                    image.setMaxHeight(20);
                                    image.setMaxWidth(20);
                                    if (progress >= i)
                                        image.setImageResource(R.drawable.scale1);
                                    else if (progress < i)
                                        image.setImageResource(R.drawable.scale);
                                    // Adds the view to the layout
                                    layout.addView(image);
                                }

                            }
                            if (goal.getType().equals(Goal.MOOD)) {
                                amount = goal.getDailyAmount();
                                progress = goal.getDailyProgress();
                                System.out.println("Mood" + amount);
                                for (int i = 1; i <= amount; i++) {
                                    ImageView image = new ImageView(getContext());
                                    image.setLayoutParams(new android.view.ViewGroup.LayoutParams(80, 60));
                                    image.setMaxHeight(20);
                                    image.setMaxWidth(20);
                                    if (progress >= i)
                                        image.setImageResource(R.drawable.ic_mood_image_change_black_64dp);
                                    else if (progress < i)
                                        image.setImageResource(R.drawable.ic_sentiment_satisfied_black_64dp);
                                    // Adds the view to the layout
                                    layout.addView(image);
                                }
                            }
                        }
                    }



                    if (LogBaseline.fireBaseDoneBooleanBtn == true) {
                        totalValues = totalValues + 10;
                        LogBaseline.fireBaseDoneBooleanBtn = false;
                    }

                    System.out.println("Total Values1:" +  totalValues);
                    System.out.println("Today badge1:" +  todayBadge);

                    if (allGoalsMet(userData.getGoals()) && (todayBadge==false)) {
                        System.out.println("All goals completed  " + todayBadge);
                        badge_congratulation_dialogbox();
                        badgeValue = badgeValue +1;
                        todayBadge = true;

                    }

                    userData.setPoints(totalValues);
                    userData.setBadges(badgeValue);
                    userData.setToday(todayBadge);

                    if(userData.getToday()==true) {
                        System.out.println("All goals checked  " + userData.getToday());
                        Calendar c = Calendar.getInstance();

                        if (c.get(Calendar.DAY_OF_WEEK) == 2) {
                            System.out.println("Day of the week" + c.get(Calendar.DAY_OF_WEEK));
                            userData.setSunday(false);
                            userData.setMonday(true);
                            userData.setTuesday(false);
                            userData.setWednesday(false);
                            userData.setThursday(false);
                            userData.setFriday(false);
                            userData.setSaturday(false);
                        }
                        if (c.get(Calendar.DAY_OF_WEEK) == 3) {
                            System.out.println("Day of the week" + c.get(Calendar.DAY_OF_WEEK));
                            userData.setTuesday(true);
                        }
                        if (c.get(Calendar.DAY_OF_WEEK) == 4) {
                            System.out.println("Day of the week" + c.get(Calendar.DAY_OF_WEEK));
                            userData.setWednesday(true);
                        }
                        if (c.get(Calendar.DAY_OF_WEEK) == 5) {
                            System.out.println("Day of the week" + c.get(Calendar.DAY_OF_WEEK));
                            userData.setThursday(true);
                        }
                        if (c.get(Calendar.DAY_OF_WEEK) == 6) {
                            System.out.println("Day of the week" + c.get(Calendar.DAY_OF_WEEK));
                            userData.setFriday(true);
                        }
                        if (c.get(Calendar.DAY_OF_WEEK) == 7) {
                            System.out.println("Day of the week" + c.get(Calendar.DAY_OF_WEEK));
                            userData.setSaturday(true);
                        }
                        if (c.get(Calendar.DAY_OF_WEEK) == 1) {
                            System.out.println("Day of the week" + c.get(Calendar.DAY_OF_WEEK));
                            userData.setSunday(true);
                        }
                    }


                    try {
                        System.out.println("Data saved in users");
                        FirebaseFirestore.getInstance().collection("users").document(uid).set(userData);
                    }
                    catch (Exception e) {
                        System.out.println("Exception" +  e);
                    }


                    totalPointsTextView.setText("Total Points: " + userData.getPoints());
                    badgeTextView.setText("Total Badges: " + userData.getBadges());


                } catch (Exception e) {
                    todayPointsTextView.setText("Today Points: 0");
                    totalPointsTextView.setText("Total Points: 0");
                    badgeTextView.setText("Total Badges: 0");
                }

            }
        });



        view.findViewById(R.id.button_insulin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.playSoundEffect(SoundEffectConstants.CLICK);
                if (user.hasMedication()) {
                    /*PopupMenu popUp = new PopupMenu(getContext(), view.findViewById(R.id.button_insulin));
                    Menu menu = popUp.getMenu();
                    menu.add(R.string.insulin);
                    menu.add(R.string.other_medicine);
                    popUp.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            String s = item.getTitle().toString();
                            Log.d("DEBUGGING", s + " " + getResources().getString(R.string.insulin));
                            if (s.equals(getResources().getString(R.string.insulin)))
                                onButtonPressed(Uri.parse("insulin"));
                            else if (s.equals(getResources().getString(R.string.other_medicine)))
                                onButtonPressed(Uri.parse("medicine"));
                            return true;
                        }
                    });

                    popUp.show();*/
                    onButtonPressed(Uri.parse("medicine"));

                } else
                    onButtonPressed(Uri.parse("insulin"));
            }
        });
        view.findViewById(R.id.button_resource).setOnClickListener(mButtonListener);


        //

    }

    // TODO: Rename method, update argument and hook method into UI event
    private void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.messageFromHome(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof HomeFragment.OnFragmentInteractionListener) {
            mListener = (HomeFragment.OnFragmentInteractionListener) context;

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

    public Bundle getLogData() {
        Bundle bundle = new Bundle();
        if (totalPointsTextView.getText().toString().equals("")) {
            Log.d("DEBUGGING", "no weight entered");
            bundle.putBoolean("validInput", false);
            //Toast toast=Toast.makeText(getContext(),"Please enter a number for your weight", Toast.LENGTH_LONG);
            //toast.show();
            ((MainActivity) getActivity()).setMessage("weightError");
        } else {
            bundle.putBoolean("validInput", true);
            bundle.putInt("totalPointsValue", 100 /*Integer.parseInt(totalPointsTextView.getText().toString())*/);
        }
        return bundle;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void messageFromHome(Uri uri);
    }

   /* @Override
    public void onResume() {
        super.onResume();

        */
    /*SharedPreferences sharedPreferences = getContext().getSharedPreferences("counterText", 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (sharedPreferences.getBoolean(ConstantClass.counterText, false) == true) {
            int value = sharedPreferences.getInt(ConstantClass.counterIncrement, 0);
            value = value + 10;
            editor.putInt(ConstantClass.counterIncrement, value);
            editor.putBoolean(ConstantClass.counterText, false);
            editor.apply();
        }*/
    /*

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseFirestore.getInstance().collection("users")
                .document(uid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                try {
                    //int badgeValue = 0;
                    //int todayValue = 0;
                    User userData = documentSnapshot.toObject(User.class);
                    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    */
    /*int totalValues = user.getScores().getTotalScore();
                    if (LogBaseline.fireBaseDoneBooleanBtn == true) {
                        totalValues = totalValues + 10;
                        LogBaseline.fireBaseDoneBooleanBtn = false;
                    }
                    badgeValue = totalValues / 100;
                    todayValue = totalValues % 100;
                    ProductionModelClass pmd = new ProductionModelClass(totalValues, todayValue, badgeValue);
                    user.setScores(pmd);
                    Log.d(TAG, "onSuccess: TotalValues" + LogBaseline.totalValues + "Today Values"
                            + todayValue + "Badges Earned" + badgeValue);

                    FirebaseFirestore.getInstance().collection("users").document(uid).set(user);
                    todayPointsTextView.setText("Points Earned Today: " + String.valueOf(user.getScores().getTodayScore()) + "/100");
                    totalPointsTextView.setText("Total Points: " + String.valueOf(user.getScores().getTotalScore()));
                    badgeTextView.setText("Total Badges: " + String.valueOf(user.getScores().getNumberOfBadgesEarned()));
                    if (todayPointsTextView.getText().equals("100")) {
                        badge_congratulation_dialogbox();
                    }*/
    /*
                    userGoals = userData.getGoals();
                    int amount = 0;
                    int progress = 0;
                    int amount1 = 0;
                    int progress1 = 0;
                    if (LogBaseline.fireBaseDoneBooleanBtn == true) {
                        totalValues = userData.getScores().getTotalScore();
                        for (Goal goal : userGoals) {
                            if (goal.getType().equals(Goal.FOODMEAL)) {
                                amount1 = goal.getMealAmount();
                                progress1 = goal.getMealsEaten();
                            } else if (goal.getType().equals(Goal.ACTIVITY)) {
                                totalValues = totalValues + 10;
                                Log.d(TAG, "onSuccess:total vaalues Is:  " + totalValues);
                                badgeValue = totalValues / 100;
                                todayValue = totalValues % 100;
                            } else {
                                amount1 = goal.getDailyAmount();
                                progress1 = goal.getDailyProgress();
                            }
                            Log.d(TAG, "onSuccess:Amount1 Is:  " + amount1 + "Progress1 Is : " + progress1);
                            for (int i = 1; i <= amount1; i++) {
                                Log.d(TAG, "onSuccess:Amount Is:  " + amount1 + "Progress Is : " + progress1);
                                if (progress1 >= i) {

                                    Log.d(TAG, "onSuccess: FireBaseDoneValue: " + LogBaseline.fireBaseDoneBooleanBtn);

                                    totalValues = totalValues + 10;
                                    Log.d(TAG, "onSuccess:total vaalues Is:  " + totalValues);
                                    badgeValue = totalValues / 100;
                                    todayValue = totalValues % 100;
                                    pmd = new ProductionModelClass(totalValues, todayValue, badgeValue);
                                    userData.setScores(pmd);
                                    // sending data to firebase
                                   /// FirebaseFirestore.getInstance().collection("users").document(uid).set(userData);
                                    // getting data from user model class sent from firebase
                                   // todayPointsTextView.setText("Points Earned Today: " + String.valueOf(userData.getScores().getTodayScore()) + "/100");
                                    //totalPointsTextView.setText("Total Points: " + String.valueOf(userData.getScores().getTotalScore()));
                                    //badgeTextView.setText("Total Badges: " + String.valueOf(userData.getScores().getNumberOfBadgesEarned()));
                                    if (todayPointsTextView.getText().equals("100")) {
                                        badge_congratulation_dialogbox();

                                    }
                                }
                            }
                        }
                        LogBaseline.fireBaseDoneBooleanBtn = false;
                    }
                    pmd = new ProductionModelClass(totalValues, todayValue, badgeValue);
                    userData.setScores(pmd);
                    // sending data to firebase
                  ///  FirebaseFirestore.getInstance().collection("users").document(uid).set(userData);
                    // getting data from user model class sent from firebase
                    todayPointsTextView.setText("Points Earned Today: " + String.valueOf(userData.getScores().getTodayScore()) + "/100");
                    totalPointsTextView.setText("Total Points: " + String.valueOf(userData.getScores().getTotalScore()));
                    badgeTextView.setText("Total Badges: " + String.valueOf(userData.getScores().getNumberOfBadgesEarned()));
                    if (todayPointsTextView.getText().equals("100")) {
                        badge_congratulation_dialogbox();
                    }
                } catch (
                        Exception e) {
                    todayPointsTextView.setText("0");
                    totalPointsTextView.setText("0");
                    badgeTextView.setText("0");
                }

            }
        });



      */
    /*      int todayValue = sharedPreferences.getInt(ConstantClass.todayTaskIncrement, 0);
        Log.d(TAG, "Today Value Is: : " + todayValue);
        int totalBadgedValue = todayValue / 100;
        Log.d(TAG, "total Badged Value: " + totalBadgedValue);
        int newTodayValue = todayValue % 100;
        int counterTextIncrement = sharedPreferences.getInt(ConstantClass.counterIncrement, 0);
       */
    /*


     */
    /* int TodayTask = sharedPreferences.getInt(ConstantClass.counterIncrement, 0) % 100;

        if (sharedPreferences.getBoolean(ConstantClass.todayTaskText, false) == true) {
//            int todayTaskCounter = (sharedPreferences.getInt(ConstantClass.counterIncrement, 0));
//
//            editor.putBoolean(ConstantClass.todayTaskText, false);
//            editor.apply();
//            if (todayValue > 100) {
//                todayValue = todayTaskCounter % 100 + 10;
//                todayPointsTextView.setText("Points Earned Today: " + String.valueOf(todayValue) + "/100");
//            } else {
//                todayPointsTextView.setText("Points Earned Today: " + String.valueOf(todayValue) + "/100");
//
//            }


        }


    }*/




    public void badge_congratulation_dialogbox() {
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_dialogbox);
        TextView close_btn = dialog.findViewById(R.id.close_dialog);
        close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
        dialog.setCancelable(false);
    }

    public void showAlertDialogTotalPoints() {
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_dialogbox_totalpoint);
        TextView close_btn = dialog.findViewById(R.id.close_dialog_totalpoint);
        close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
        dialog.setCancelable(false);
    }


    public void showAlertDialogTotalBadges() {
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_dialogbox_totalbadges);
        TextView close_btn = dialog.findViewById(R.id.close_dialog_totalbadge);
        close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
        dialog.setCancelable(false);
    }

    public boolean allGoalsMet(ArrayList<Goal> goals) {
        ArrayList<Goal> unmetGoals = new ArrayList<>();
        boolean goalMet = false;
        for (Goal g : goals) {
            if (!g.dailyGoalMet())
                unmetGoals.add(g);
        }

        if (unmetGoals.size() == 0) {
            goalMet = true;
        }

        return goalMet;
    }
}
