package com.example.diabetesmanagement.ui.home.log.entry;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Trace;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.diabetesmanagement.MainActivity;
import com.example.diabetesmanagement.R;
import com.example.diabetesmanagement.constatntClass.ConstantClass;
import com.example.diabetesmanagement.data.Goal;
import com.example.diabetesmanagement.data.User;

import androidx.fragment.app.Fragment;

public class FoodFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Bundle log;
    private User user;
    //private EditText carbs;
    private EditText itemsEaten;
    private RadioButton meal;
    private RadioButton snack;

    public boolean checkValue = false;
    public static boolean foodValue = false;

    public String itemsEatenEditText;

    // private OnFragmentInteractionListener mListener;

    public FoodFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LogBloodSugar.
     */
    // TODO: Rename and change types and number of parameters
    public static FoodFragment newInstance(User param1, String param2) {
        FoodFragment fragment = new FoodFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM2, param2);
        args.putSerializable(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM2);
            user = (User) getArguments().getSerializable(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_log_food, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        //carbs = view.findViewById(R.id.data_carbEntry);
        itemsEaten = view.findViewById(R.id.data_itemsEaten);
        meal = view.findViewById(R.id.radioButton_meal);
        snack = view.findViewById(R.id.radioButton_snack);
        TextView textView = view.findViewById(R.id.label_foodGoal);
        StringBuilder s = new StringBuilder();
        s.append("Your meal goal is ");
        s.append(user.getGoal(Goal.FOODMEAL).getMealAmount());
        s.append(" meals a day.\nYour snack goal is ");
        s.append(user.getGoal(Goal.FOODSNACK).getSnackAmount());
        s.append(" snacks a day.");
        textView.setText(s.toString());


        itemsEaten.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (checkValue) {
                    SharedPreferences sharedPreferences = getContext().getSharedPreferences("counterText", 0);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean(ConstantClass.counterText, true);
                    if (meal.isChecked()){
                        editor.putBoolean(ConstantClass.foodImageTextMeal, true);
                    }
                    if (snack.isChecked()){
                        editor.putBoolean(ConstantClass.foodImageTextSnack, true);
                    }
                    editor.putBoolean(ConstantClass.todayTaskText, true);
                    foodValue = true;
                    editor.apply();
                } else {
                    checkValue = true;
                    SharedPreferences sharedPreferences = getContext().getSharedPreferences("counterText", 0);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean(ConstantClass.counterText, false);
                    editor.putBoolean(ConstantClass.todayTaskText, false);
                    foodValue = true;
                    editor.apply();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        meal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (checkValue) {
                    SharedPreferences sharedPreferences = getContext().getSharedPreferences("counterText", 0);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean(ConstantClass.counterText, true);
//                    if (itemsEaten!=null){
//                        editor.putBoolean(ConstantClass.foodImageTextMeal, true);
//                    }
//                    if (itemsEaten!=null){
//                        editor.putBoolean(ConstantClass.foodImageTextSnack, true);
//                    }
                    editor.putBoolean(ConstantClass.foodImageTextMeal, true);
                    editor.putBoolean(ConstantClass.todayTaskText, true);
                    foodValue = true;
                    editor.apply();
                } else {
                    SharedPreferences sharedPreferences = getContext().getSharedPreferences("counterText", 0);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean(ConstantClass.counterText, false);
                    editor.putBoolean(ConstantClass.foodImageTextMeal, false);
                    editor.putBoolean(ConstantClass.todayTaskText, false);
                    foodValue = true;
                    editor.apply();
                    checkValue = false;
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        snack.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (checkValue) {
                    SharedPreferences sharedPreferences = getContext().getSharedPreferences("counterText", 0);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean(ConstantClass.counterText, true);
//                    if (itemsEaten!=null ){
//                        editor.putBoolean(ConstantClass.foodImageTextMeal, true);
//                    }
//                    if (itemsEaten!=null){
//                        editor.putBoolean(ConstantClass.foodImageTextItemsEaten, true);
//                    }
                    editor.putBoolean(ConstantClass.foodImageTextSnack, true);
                    editor.putBoolean(ConstantClass.todayTaskText, true);
                    foodValue = true;
                    editor.apply();
                } else {
                    SharedPreferences sharedPreferences = getContext().getSharedPreferences("counterText", 0);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean(ConstantClass.counterText, false);
                    editor.putBoolean(ConstantClass.foodImageTextSnack, false);
                    editor.putBoolean(ConstantClass.todayTaskText, false);
                    foodValue = true;
                    editor.apply();
                    checkValue = false;
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }
    /*
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Bundle bundle) {
        if (mListener != null) {
            mListener.messageFromBloodSugar(bundle);
        }
    }*/

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        log = new Bundle();
        /*if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    public Bundle getLogData() {
        // entering carbs might not be possible, -1 means no carb data entered
        int c = -1;
        //if (!carbs.getText().toString().equals("")) {
        //  c = Integer.parseInt(carbs.getText().toString());
        //}
        //Log.d("DEBUGGING", "carbs entered: " + c);
        /*if (itemsEaten.getText().toString().equals("")) {
            Log.d("DEBUGGING", "no itemsEaten entered");
            log.putBoolean("validInput", false);
            //Toast toast=Toast.makeText(getContext(),"Please enter a short description of food eaten", Toast.LENGTH_LONG);
            //toast.show();
            ((MainActivity)getActivity()).setMessage("foodError");
        }
        else {
            log.putBoolean("validInput", true);
           // log.putInt("carbs", c);
            log.putString("itemsEaten", itemsEaten.getText().toString());
        }*/
        log.putBoolean("validInput", true);
        if (itemsEaten.getText().toString().equals(""))
            log.putString("itemsEaten", "Nothing entered");
        else
            log.putString("itemsEaten", itemsEaten.getText().toString());
        log.putBoolean("meal", meal.isChecked());
        log.putBoolean("snack", snack.isChecked());
        return log;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //mListener = null;
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
    //public interface OnFragmentInteractionListener {
    //    // TODO: Update argument type and name
    //    void messageFromBloodSugar(Bundle bundle);
    //}

}
