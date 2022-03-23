package com.example.diabetesmanagement.ui.home.log.entry;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SoundEffectConstants;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.diabetesmanagement.MainActivity;
import com.example.diabetesmanagement.R;
import com.example.diabetesmanagement.constatntClass.ConstantClass;

import androidx.fragment.app.Fragment;

public class MoodFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Bundle log;

    private ImageView great;
    private ImageView poor;
    private ImageView fine;
    private ImageView neutral;
    private TextView currentMood;
    private EditText physical;
    private EditText mental;
    private String mood;

    public static boolean moodValue=false;

    // private OnFragmentInteractionListener mListener;

    public MoodFragment() {
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
    public static MoodFragment newInstance(String param1, String param2) {
        MoodFragment fragment = new MoodFragment();
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
        return inflater.inflate(R.layout.fragment_log_mood, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        //physical = view.findViewById(R.id.data_physical);
        //mental = view.findViewById(R.id.data_mental);
        currentMood = view.findViewById(R.id.label_mood);
        great = view.findViewById(R.id.mood_great);
        poor = view.findViewById(R.id.mood_poor);
        fine = view.findViewById(R.id.mood_fine);
        neutral = view.findViewById(R.id.mood_neutral);

        great.setOnClickListener(moodListener);
        poor.setOnClickListener(moodListener);
        fine.setOnClickListener(moodListener);
        neutral.setOnClickListener(moodListener);

        mood = "";
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
        /*
        if (physical.getText().toString().equals("") && mental.getText().toString().equals("")) {
            Log.d("DEBUGGING", "no mood entered");
            log.putBoolean("validInput", false);
            Toast toast=Toast.makeText(getContext(),"Please enter a short description of either your physical or mental feelings", Toast.LENGTH_LONG);
            toast.show();
        }
        else {
            log.putBoolean("validInput", true);
            log.putString("physical", physical.getText().toString());
            log.putString("mental", mental.getText().toString());
        }
        */
        if (mood.equals("")) {
            Log.d("DEBUGGING", "no mood entered");
            log.putBoolean("validInput", false);
            //Toast toast=Toast.makeText(getContext(),"Please select how you're feeling right now", Toast.LENGTH_LONG);
            //toast.show();
            ((MainActivity) getActivity()).setMessage("moodError");
        } else {
            log.putBoolean("validInput", true);
            log.putString("mood", mood);
        }

        return log;
    }

    private void moodClicked(View v) {
        switch (v.getId()) {
            case R.id.mood_great:
                if (((ColorDrawable) great.getBackground()).getColor() != getResources().getColor(R.color.colorSelected)) {
                    great.setBackgroundColor(getResources().getColor(R.color.colorSelected));
                    poor.setBackgroundColor(getResources().getColor(R.color.fui_transparent));
                    fine.setBackgroundColor(getResources().getColor(R.color.fui_transparent));
                    neutral.setBackgroundColor(getResources().getColor(R.color.fui_transparent));
                    mood = "great";
                } else {
                    great.setBackgroundColor(getResources().getColor(R.color.fui_transparent));
                    mood = "";
                }
                break;
            case R.id.mood_poor:
                if (((ColorDrawable) poor.getBackground()).getColor() != getResources().getColor(R.color.colorSelected)) {
                    great.setBackgroundColor(getResources().getColor(R.color.fui_transparent));
                    poor.setBackgroundColor(getResources().getColor(R.color.colorSelected));
                    fine.setBackgroundColor(getResources().getColor(R.color.fui_transparent));
                    neutral.setBackgroundColor(getResources().getColor(R.color.fui_transparent));
                    mood = "not good";
                } else {
                    poor.setBackgroundColor(getResources().getColor(R.color.fui_transparent));
                    mood = "";
                }
                break;
            case R.id.mood_fine:
                if (((ColorDrawable) fine.getBackground()).getColor() != getResources().getColor(R.color.colorSelected)) {
                    great.setBackgroundColor(getResources().getColor(R.color.fui_transparent));
                    poor.setBackgroundColor(getResources().getColor(R.color.fui_transparent));
                    fine.setBackgroundColor(getResources().getColor(R.color.colorSelected));
                    neutral.setBackgroundColor(getResources().getColor(R.color.fui_transparent));
                    mood = "fine";
                } else {
                    fine.setBackgroundColor(getResources().getColor(R.color.fui_transparent));
                    mood = "";
                }
                break;
            case R.id.mood_neutral:
                if (((ColorDrawable) neutral.getBackground()).getColor() != getResources().getColor(R.color.colorSelected)) {
                    great.setBackgroundColor(getResources().getColor(R.color.fui_transparent));
                    poor.setBackgroundColor(getResources().getColor(R.color.fui_transparent));
                    fine.setBackgroundColor(getResources().getColor(R.color.fui_transparent));
                    neutral.setBackgroundColor(getResources().getColor(R.color.colorSelected));
                    mood = "no strong emotion";
                } else {
                    neutral.setBackgroundColor(getResources().getColor(R.color.fui_transparent));
                    mood = "";
                }
                break;

        }
        currentMood.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() >= 1) {
                    SharedPreferences sharedPreferences = getContext().getSharedPreferences("counterText", 0);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean(ConstantClass.counterText, true);
                    editor.putBoolean(ConstantClass.moodImageText, true);
                    editor.putBoolean(ConstantClass.todayTaskText, true);
                    moodValue = true;
                    editor.apply();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        currentMood.setText(getString(R.string.label_mood) + " " + mood);
    }

    private View.OnClickListener moodListener = new View.OnClickListener() {
        public void onClick(View v) {
            v.playSoundEffect(SoundEffectConstants.CLICK);
            moodClicked(v);
        }
    };

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
