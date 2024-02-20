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
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.diabetesmanagement.MainActivity;
import com.example.diabetesmanagement.R;
import com.example.diabetesmanagement.constatntClass.ConstantClass;

import androidx.fragment.app.Fragment;

public class WeightFragment extends Fragment {

    SharedPreferences mSharedPreferences;
    SharedPreferences.Editor mEditor;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Bundle log;
    private EditText weight;

    public static boolean weightValue = false;

    // private OnFragmentInteractionListener mListener;

    public WeightFragment() {
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
    public static WeightFragment newInstance(String param1, String param2) {
        WeightFragment fragment = new WeightFragment();
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
        return inflater.inflate(R.layout.fragment_log_weight, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        weight = view.findViewById(R.id.data_weight);
        weight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() >= 1) {
                    SharedPreferences sharedPreferences = getContext().getSharedPreferences("counterText", 0);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean(ConstantClass.counterText, true);
                    editor.putBoolean(ConstantClass.weightImageText, true);
                    editor.putBoolean(ConstantClass.todayTaskText, true);
                    weightValue = true;
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
        if (weight.getText().toString().equals("")
                || Integer.parseInt(weight.getText().toString()) < 70
                || Integer.parseInt(weight.getText().toString()) > 500
                ) {
            Log.d("DEBUGGING", "no weight entered");
            log.putBoolean("validInput", false);
            weight.setError("Invalid input");
            //Toast toast=Toast.makeText(getContext(),"Please enter a valid input", Toast.LENGTH_LONG);
            //toast.show();
            ((MainActivity) getActivity()).setMessage("weightError");

        } else {
            log.putBoolean("validInput", true);
            log.putInt("weight", Integer.parseInt(weight.getText().toString()));
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
    @Override
    public void onResume() {
        super.onResume();

    }
}
