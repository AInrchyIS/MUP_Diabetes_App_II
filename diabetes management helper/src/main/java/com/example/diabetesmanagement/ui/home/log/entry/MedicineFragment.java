package com.example.diabetesmanagement.ui.home.log.entry;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.diabetesmanagement.MainActivity;
import com.example.diabetesmanagement.R;
import com.example.diabetesmanagement.constatntClass.ConstantClass;
import com.example.diabetesmanagement.data.Medication;
import com.example.diabetesmanagement.data.User;
import com.example.diabetesmanagement.service.FirestoreCallback;
import com.example.diabetesmanagement.service.UserStorage;

import java.util.ArrayList;

/*
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MedicineFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MedicineFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MedicineFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private User user;
    private Bundle log;
    private Spinner medication;

    public static boolean medicineValue = false;
    //private OnFragmentInteractionListener mListener;

    public MedicineFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MedicineFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MedicineFragment newInstance(User param1, String param2) {
        MedicineFragment fragment = new MedicineFragment();
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
        return inflater.inflate(R.layout.fragment_medicine, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        medication = view.findViewById(R.id.spinner_medicine);
        UserStorage.readUser(new FirestoreCallback() {
            @Override
            public void onCallback(User user) {
                ArrayList<CharSequence> medicineList = new ArrayList<>();
                //TODO: Populate medication from existing list
                ArrayList<Medication> medications = user.getMedications();
                for (Medication m:medications)
                    medicineList.add(m.getName());
                ArrayAdapter<CharSequence> adapter = new ArrayAdapter<>(getContext(),
                        android.R.layout.simple_spinner_item, medicineList);
                // Specify the layout to use when the list of choices appears
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                // Apply the adapter to the spinner
                medication.setAdapter(adapter);
            }
        });
        Button button = view.findViewById(R.id.button_insulin);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((LogBaseline)getParentFragment()).replaceFragment("insulin");
                ((MainActivity)getActivity()).setMessage("insulin");
            }
        });



        medication.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View view, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7) {
                SharedPreferences sharedPreferences = getContext().getSharedPreferences("counterText", 0);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean(ConstantClass.counterText, true);
                editor.putBoolean(ConstantClass.medicineImageText, true);
                editor.putBoolean(ConstantClass.todayTaskText, true);
                medicineValue = true;
                editor.apply();
            }
        });
    }


    /*public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }*/


    public Bundle getLogData()
    {
        log.putBoolean("validInput", true);
        log.putString("medication", medication.getSelectedItem().toString());

        return log;
    }

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
   /* public interface OnFragmentInteractionListener {

        void onFragmentInteraction(Uri uri);
    }*/
}
