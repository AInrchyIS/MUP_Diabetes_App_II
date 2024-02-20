package com.example.diabetesmanagement.ui.home.user.data.management;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.SoundEffectConstants;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.diabetesmanagement.MainActivity;
import com.example.diabetesmanagement.R;
import com.example.diabetesmanagement.constatntClass.AddMedicineNoTimesClass;
import com.example.diabetesmanagement.data.Goal;
import com.example.diabetesmanagement.data.Medication;
import com.example.diabetesmanagement.data.User;

import androidx.fragment.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MedicationSetupFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MedicationSetupFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MedicationSetupFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";

    private Medication medication;
    private boolean newMedication = true;
    private User user;
    private EditText medicationName;

    private EditText medicationDosage;

    private Button cancel;
    private Button save;
    private Bundle medicationBundle;
    private boolean registration;

    private OnFragmentInteractionListener mListener;
    AddMedicineNoTimesClass addMedicineNoTimesClass;

    public MedicationSetupFragment() {
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
    public static MedicationSetupFragment newInstance(User param1, Medication param2, boolean param3) {
        MedicationSetupFragment fragment = new MedicationSetupFragment();
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
            if (getArguments().getSerializable(ARG_PARAM2) != null) {
                medication = (Medication) getArguments().getSerializable(ARG_PARAM2);
                newMedication = false;
            } else
                medication = new Medication();
            registration = getArguments().getBoolean(ARG_PARAM3);
        }
        medicationBundle = new Bundle();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_medication_setup, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        addMedicineNoTimesClass = new AddMedicineNoTimesClass(requireContext());
        medicationName = view.findViewById(R.id.data_medication_name);
        medicationDosage = view.findViewById(R.id.data_medication_dose);

        if (!newMedication) {
            medicationName.setText(medication.getName());
            //if (medication.getDailyDoses() == 0)

            medicationDosage.setText(medication.getDailyDoses() + "");
        }
        cancel = view.findViewById(R.id.button_medication_cancel);
        save = view.findViewById(R.id.button_medication_save);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.playSoundEffect(SoundEffectConstants.CLICK);
                medicationBundle.putString("button", "cancel");
                onButtonPressed(medicationBundle);
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.playSoundEffect(SoundEffectConstants.CLICK);
                if (medicationName.getText().toString().equals("")) {
                    ((MainActivity) getActivity()).setMessage("medicationError");
                }
                //else if (medicationDosage.getText().toString().equals("0")) {
                //    ((MainActivity)getActivity()).setMessage("medicationDoseError");
                //}
                else if ((!newMedication && user.medicationCount(medicationName.getText().toString()) > 1)
                        || (newMedication && user.medicationCount(medicationName.getText().toString()) > 0)) {
                    ((MainActivity) getActivity()).setMessage("medicationDuplicateError");
                } else if (medicationName.getText().toString().toLowerCase().contains("insulin")){
                    ((MainActivity) getActivity()).setMessage("medicationInsulinError");
                }
                else {
                    medicationBundle.putString("oldMed", medication.getName());
                    medication.setName(medicationName.getText().toString());
                    if (medicationDosage.getText().toString().equals(""))
                        medication.setDailyDoses(0);
                    else
                        medication.setDailyDoses(Integer.parseInt(medicationDosage.getText().toString()));
                    medicationBundle.putSerializable("med", medication);
                    medicationBundle.putBoolean("newMed", newMedication);
                    medicationBundle.putString("button", "save");
                    onButtonPressed(medicationBundle);
                }
            }
        });
        /**Updated code for medicine badges*/
        /*save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.playSoundEffect(SoundEffectConstants.CLICK);
                if (medicationName.getText().toString().equals("")) {
                    ((MainActivity) getActivity()).setMessage("medicationError");
                } *//*else if (medicationDosage.getText().toString().equals("")) {
                    ((MainActivity) getActivity()).setMessage("medicationDoseError");
                }*//* else if ((!newMedication && user.medicationCount(medicationName.getText().toString()) > 1)
                        || (newMedication && user.medicationCount(medicationName.getText().toString()) > 0)) {
                    ((MainActivity) getActivity()).setMessage("medicationDuplicateError");
                } else if (medicationName.getText().toString().toLowerCase().contains("insulin")) {
                    ((MainActivity) getActivity()).setMessage("medicationInsulinError");
                } else {
                    addMedicineNoTimesClass.medicationDialogue(medicationName.getText().toString());
//                  medicationBundle.putString("oldMed", medication.getName());
                    medication.setName(medicationName.getText().toString());
                    *//*if (medicationDosage.getText().toString().equals("")) {
                        medication.setDailyDoses(0);
                    } else {*//*
                        *//**medication.setDailyDoses(Integer.ParseInt(medicationDosage.getText().toString()));*//*
                        medication.setDailyDoses(1);
                        medicationBundle.putSerializable("med", medication);
                        medicationBundle.putBoolean("newMed", newMedication);
                        medicationBundle.putString("button", "save");
                        onButtonPressed(medicationBundle);
                   *//* }*//*
//                    {
                        *//*if (Integer.parseInt(medicationDosage.getText().toString())<1
                            || Integer.parseInt(medicationDosage.getText().toString())>5){
                            medicationDosage.setError("Invalid input");
                        }
                        else {
                            medication.setDailyDoses(Integer.parseInt(medicationDosage.getText().toString()));*//*
                        medicationBundle.putSerializable("med", medication);
                        medicationBundle.putBoolean("newMed", newMedication);
                        medicationBundle.putString("button", "save");
                        onButtonPressed(medicationBundle);
                        *//*}*//*
//                    }
                }
            }
        });*/
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Bundle bundle) {
        bundle.putBoolean("registration", registration);
        if (mListener != null) {
            mListener.messageFromMedicationSetupFragment(bundle);
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

        void messageFromMedicationSetupFragment(Bundle bundle);
    }
}
