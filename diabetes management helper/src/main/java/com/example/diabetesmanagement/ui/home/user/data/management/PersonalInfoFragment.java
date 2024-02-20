package com.example.diabetesmanagement.ui.home.user.data.management;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.SoundEffectConstants;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.diabetesmanagement.MainActivity;
import com.example.diabetesmanagement.R;
import com.example.diabetesmanagement.data.User;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PersonalInfoFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PersonalInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PersonalInfoFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private Bundle personalInfo;
    //private Spinner diabetesType;
    private Spinner gender;
    private EditText fName;
    private EditText lName;
    private EditText age;
    private User user;
    private boolean registration;

    // TODO: Rename and change types of parameters
    //private String mParam1;
    //private String mParam2;

    private OnFragmentInteractionListener mListener;

    public PersonalInfoFragment() {
        // Required empty public constructor
    }

    /*
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PersonalInfoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PersonalInfoFragment newInstance(User param1, boolean param2) {
        PersonalInfoFragment fragment = new PersonalInfoFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, param1);
        args.putBoolean(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
           user = (User)getArguments().getSerializable(ARG_PARAM1);
           registration = getArguments().getBoolean(ARG_PARAM2);
        }
        personalInfo = new Bundle();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_personal_info, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Bundle bundle) {
        bundle.putBoolean("registration", registration);
        if (mListener != null) {
            mListener.messageFromPersonalInfoFragment(bundle);
        }
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //fName = view.findViewById(R.id.settings_firstName);
        //lName = view.findViewById(R.id.settings_lastName);
        age = view.findViewById(R.id.settings_age);


        /*if (!user.getfName().equals(""))
            fName.setText(user.getfName());
        if (!user.getlName().equals(""))
            lName.setText(user.getlName());*/
        if (user.getAge() != -1 && user.getAge() != 0)
            age.setText(String.valueOf(user.getAge()));

        /*diabetesType = view.findViewById(R.id.spinner_diabetesType);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.diabetes_type, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        diabetesType.setAdapter(adapter);
        */
        gender = view.findViewById(R.id.spinner_gender);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(getContext(),
                R.array.gender, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gender.setAdapter(adapter2);
        if (user.getGender() != null) {
            if (user.getGender().equals("Male"))
                gender.setSelection(1);
            else if (user.getGender().equals("Female"))
                gender.setSelection(2);
        }
        /*if (user.getDiabetesType().equals("Type 1"))
            diabetesType.setSelection(0);
        if (user.getDiabetesType().equals("Type 2"))
            diabetesType.setSelection(1);
        */

        if (registration)
            ((Button)view.findViewById(R.id.button_savePersonalInfo)).setText(R.string.next);
        view.findViewById(R.id.button_savePersonalInfo).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                v.playSoundEffect(SoundEffectConstants.CLICK);
                String type = "Type 2"; //diabetesType.getSelectedItem().toString();
                /**Check age entry*/
                String genderString = gender.getSelectedItem().toString();
                if (//!fName.getText().toString().equals("")
                //&& !lName.getText().toString().equals("") &&
                !age.getText().toString().equals("")
                && age.getText().toString().length() < 4
                && !type.equals("") && !genderString.equals("")) {
                    String value = age.getText().toString();
                    int finalValue = Integer.parseInt(value);
                    if (finalValue <= 17 || finalValue > 100) {
                        age.setError("Invalid input");
                    } else {
                        if (//!fName.getText().toString().equals("")
                            //&& !lName.getText().toString().equals("") &&
                                !age.getText().toString().equals("")
                                        && age.getText().toString().length() < 4
                                        && !type.equals("") && !genderString.equals("")) {
                            //personalInfo.putString("fName", fName.getText().toString());
                            //personalInfo.putString("lName", lName.getText().toString());
                            personalInfo.putInt("age", Integer.parseInt(age.getText().toString()));
                            personalInfo.putString("diabetesType", type);
                            personalInfo.putString("gender", genderString);
                    /*Log.d("DEBUGGING", fName.getText().toString() + " " +
                            lName.getText().toString()+ " " +
                            age.getText().toString() + " " +
                            diabetesType.getSelectedItem().toString());*/
                            onButtonPressed(personalInfo);
                        } else if (age.getText().toString().length() > 3)
                            ((MainActivity) getActivity()).setMessage("pInfoAgeError");
                            //else if (type.equals(""))
                            //    ((MainActivity)getActivity()).setMessage("pInfoTypeError");
                        else if (genderString.equals(""))
                            ((MainActivity) getActivity()).setMessage("pInfoGenderError");
                        else
                            ((MainActivity) getActivity()).setMessage("pInfoError");
                    }
                }
            }
        });
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
        void messageFromPersonalInfoFragment(Bundle bundle);
    }
}
