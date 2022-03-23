package com.example.diabetesmanagement.ui.home.log.entry;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.diabetesmanagement.MainActivity;
import com.example.diabetesmanagement.R;
import com.example.diabetesmanagement.constatntClass.ConstantClass;

import androidx.fragment.app.Fragment;

public class ActivityFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Bundle log;
    private EditText minutesActivity;
    private EditText activityDesc;
    public boolean checkValue = false;
    public static boolean activityValue;

    // private OnFragmentInteractionListener mListener;

    public ActivityFragment() {
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
    public static ActivityFragment newInstance(String param1, String param2) {
        ActivityFragment fragment = new ActivityFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_log_activity, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        minutesActivity = view.findViewById(R.id.data_activity);
        activityDesc = view.findViewById(R.id.data_activityDesc);
        minutesActivity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (checkValue) {
                    SharedPreferences sharedPreferences = getContext().getSharedPreferences("counterText", 0);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean(ConstantClass.counterText, true);
                    editor.putBoolean(ConstantClass.activityImageText, true);
                    editor.putBoolean(ConstantClass.todayTaskText, true);
                    activityValue = true;
                    editor.apply();
                } else {
                    checkValue = true;
                    SharedPreferences sharedPreferences = getContext().getSharedPreferences("counterText", 0);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean(ConstantClass.counterText, false);
                    editor.putBoolean(ConstantClass.activityImageText, false);
                    editor.putBoolean(ConstantClass.todayTaskText, false);
                    activityValue = true;
                    editor.apply();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        activityDesc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (checkValue) {
                    SharedPreferences sharedPreferences = getContext().getSharedPreferences("counterText", 0);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean(ConstantClass.counterText, true);
                    editor.putBoolean(ConstantClass.activityImageText, true);
                    editor.putBoolean(ConstantClass.todayTaskText, true);
                    activityValue = true;
                    editor.apply();
                } else {
                    checkValue = false;
                    SharedPreferences sharedPreferences = getContext().getSharedPreferences("counterText", 0);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean(ConstantClass.counterText, false);
                    editor.putBoolean(ConstantClass.activityImageText, false);
                    editor.putBoolean(ConstantClass.todayTaskText, false);
                    activityValue = true;
                    editor.apply();

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

        if (minutesActivity.getText().toString().equals("") || (minutesActivity.getText().toString().equals("0"))
                || activityDesc.getText().toString().equals("")) {
            Log.d("DEBUGGING", "no minutesActivity/activityDesc entered");
            log.putBoolean("validInput", false);
            ((MainActivity) getActivity()).setMessage("activityError");
        } else {
            log.putBoolean("validInput", true);
            log.putInt("minutesActivity", Integer.parseInt(minutesActivity.getText().toString()));
            log.putString("activityDesc", activityDesc.getText().toString());
        }
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
