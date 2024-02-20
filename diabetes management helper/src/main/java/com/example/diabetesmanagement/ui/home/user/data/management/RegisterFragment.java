package com.example.diabetesmanagement.ui.home.user.data.management;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.diabetesmanagement.MainActivity;
import com.example.diabetesmanagement.R;
import com.example.diabetesmanagement.data.User;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RegisterFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RegisterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";

    private EditText nameField;
    private EditText passField;
    private Button registerButton;

    private String username;
    private String password;
    private User user;
    private Bundle accountBundle;

    private OnFragmentInteractionListener mListener;

    public RegisterFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegisterFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RegisterFragment newInstance(User param1, String param2, String param3) {
        RegisterFragment fragment = new RegisterFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putString(ARG_PARAM3, param3);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            user = (User)getArguments().getSerializable(ARG_PARAM1);
            username = getArguments().getString(ARG_PARAM2);
            password = getArguments().getString(ARG_PARAM3);
        }
        accountBundle = new Bundle();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Bundle bundle) {
        if (mListener != null) {
            mListener.messageFromRegistrationFragment(bundle);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Toast.makeText(requireContext(), "Hello", Toast.LENGTH_SHORT).show();
        Log.d("egisterFragmentsClass", "onViewCreated: hello");
        nameField = view.findViewById(R.id.email);
        passField = view.findViewById(R.id.password);
        registerButton = view.findViewById(R.id.button_register);

        nameField.setText(username);
        passField.setText(password);

        registerButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(!nameField.getText().toString().equals("") &&
                    !passField.getText().toString().equals(""))
                {
                    username = nameField.getText().toString().trim();
                    password = passField.getText().toString();

                    if(password.length() < 8 || password.length() > 16)
                        ((MainActivity)getActivity()).setMessage("passwordLength");
                    else if (!password.matches("\\S*[0-9]+\\S*"))
                        ((MainActivity)getActivity()).setMessage("passwordRequirements");
                    else if (!password.matches("\\S*[a-z]+\\S*"))
                        ((MainActivity)getActivity()).setMessage("passwordRequirements");
                    else if (!password.matches("\\S*[A-Z]+\\S*"))
                        ((MainActivity)getActivity()).setMessage("passwordRequirements");
                    else if (!password.matches("\\S{8,16}"))
                        ((MainActivity)getActivity()).setMessage("passwordRequirements");
                    else {
                        accountBundle.putString("username", username);
                        accountBundle.putString("passwordHash", password.hashCode() + "");
                        onButtonPressed(accountBundle);
                    }
                }
                else
                    ((MainActivity)getActivity()).setMessage("registrationEmptyError");
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

        void messageFromRegistrationFragment(Bundle bundle);
    }
}
