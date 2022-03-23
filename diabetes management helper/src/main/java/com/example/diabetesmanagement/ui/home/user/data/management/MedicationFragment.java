package com.example.diabetesmanagement.ui.home.user.data.management;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SoundEffectConstants;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.example.diabetesmanagement.R;
import com.example.diabetesmanagement.data.Goal;
import com.example.diabetesmanagement.data.Medication;
import com.example.diabetesmanagement.data.User;
import com.example.diabetesmanagement.service.DeleteFragment;
import com.example.diabetesmanagement.service.UserStorage;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.util.ArrayList;

/*
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NotificationFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NotificationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MedicationFragment extends Fragment implements DeleteFragment.ButtonDialogListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ListView medicationList;
    private OnFragmentInteractionListener mListener;
    private User user;
    private Button addButton;
    private MedicationAdapter adapter;
    private boolean registration;
    private Button nextButton;
    private ArrayList<Medication> userMedications;
    private Medication toDelete;

    private Bundle medicationData;

    public MedicationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MedicationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MedicationFragment newInstance(String param1, User param2, boolean param3) {
        MedicationFragment fragment = new MedicationFragment();
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
        }
        medicationData = new Bundle();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_medication, container, false);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Create the adapter to convert the array to views
        userMedications = user.getMedications();
        adapter = new MedicationAdapter(getContext(), userMedications, this);
        // Attach the adapter to a ListView
        medicationList = view.findViewById(R.id.list_medications);
        medicationList.setAdapter(adapter);

        addButton = view.findViewById(R.id.button_new_medication);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.playSoundEffect(SoundEffectConstants.CLICK);
                editMedication(null);
            }
        });

        nextButton = view.findViewById(R.id.button_medication_next);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.playSoundEffect(SoundEffectConstants.CLICK);
               medicationData.putString("button", "next");
               onButtonPressed(medicationData);
            }
        });
        Button backButton = view.findViewById(R.id.button_medication_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.playSoundEffect(SoundEffectConstants.CLICK);
                medicationData.putString("button", "back");
                onButtonPressed(medicationData);
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
            mListener.messageFromMedicationFragment(bundle);
        }
    }

    public void editMedication(Medication m) {
        if (m != null) {
            medicationData.putSerializable("medication", m);
            medicationData.putBoolean("newMedication", false);
        }
        else
            medicationData.putBoolean("newMedication", true);
        onButtonPressed(medicationData);
        adapter.notifyDataSetChanged();
    }


    public void deleteMedication(Medication m){
        toDelete = m;
        FragmentManager fm = getFragmentManager();
        DeleteFragment alert = new DeleteFragment("Are you sure to want to delete your medication, " + m.getName() + "?");
        alert.setTargetFragment(MedicationFragment.this, 900);
        alert.show(fm, "medicationDeletePrompt");
        Log.d("MED_DELETE", "In medicationDelete");
    }

    @Override
    public void onDialogButtonPressed(boolean confirm){
        Medication m = toDelete;
        Log.d("MED_DELETE", "In onDialogButtonPressed, was yes pushed? " + confirm);
        if (confirm) {
            Goal g = user.getGoal("Medication: " + m.getName());
            UserStorage.storeGoalProgress(g);
            user.getGoals().remove(g);
            user.getMedications().remove(m);
            userMedications = user.getMedications();
            UserStorage.saveUser(user);
            adapter.remove(m);
            adapter.notifyDataSetChanged();
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
        // TODO: Update argument type and name
        void messageFromMedicationFragment(Bundle bundle);
    }
}
