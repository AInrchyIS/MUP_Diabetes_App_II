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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.diabetesmanagement.MainActivity;
import com.example.diabetesmanagement.R;
import com.example.diabetesmanagement.constatntClass.ConstantClass;
import com.example.diabetesmanagement.data.User;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class InsulinFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private User user;
    private Bundle log;
    private EditText insulin;
    private RadioButton food;
    private RadioButton corrective;
    private Spinner insulinType;

    public static boolean insulinValue = false;
   // private OnFragmentInteractionListener mListener;

    public InsulinFragment() {
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
    public static InsulinFragment newInstance(User param1, String param2) {
        InsulinFragment fragment = new InsulinFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            user = (User)getArguments().getSerializable(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_log_insulin, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        insulin = view.findViewById(R.id.data_insulinEntry);
        food = view.findViewById(R.id.data_food);
        corrective = view.findViewById(R.id.data_corrective);
        insulinType = view.findViewById(R.id.data_insulinType);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.insulin_type, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        insulinType.setAdapter(adapter);
        insulinType.setSelection(1);

        Button back = view.findViewById(R.id.button_insulin_back);
        if (user.hasMedication())
            back.setVisibility(View.VISIBLE);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((LogBaseline)getParentFragment()).replaceFragment("medicine");
                ((MainActivity)getActivity()).setMessage("medicine");
            }
        });


        insulin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                SharedPreferences sharedPreferences = getContext().getSharedPreferences("counterText", 0);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean(ConstantClass.counterText, true);
                editor.putBoolean(ConstantClass.insulinImageText, true);
                editor.putBoolean(ConstantClass.todayTaskText, true);
                insulinValue = true;
                editor.apply();
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
        if (insulin.getText().toString().equals("")
                || (Integer.parseInt(insulin.getText().toString()) < 1)
                || (Integer.parseInt(insulin.getText().toString()) > 120)) {
            Log.d("DEBUGGING", "no insulin entered");
            log.putBoolean("validInput", false);
            insulin.setError("Invalid input");
            //Toast toast=Toast.makeText(getContext(),"Please enter the amount of insulin taken", Toast.LENGTH_LONG);
            //toast.show();
            ((MainActivity) getActivity()).setMessage("insulinError");
        }
        else{
            log.putBoolean("validInput", true);
            log.putInt("insulin", Integer.parseInt(insulin.getText().toString()));
            //log.putBoolean("food", food.isChecked());
            //log.putBoolean("corrective", corrective.isChecked());
            Log.d("DEBUGGING", insulinType.getSelectedItem().toString());
            log.putString("insulinType", insulinType.getSelectedItem().toString());
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
