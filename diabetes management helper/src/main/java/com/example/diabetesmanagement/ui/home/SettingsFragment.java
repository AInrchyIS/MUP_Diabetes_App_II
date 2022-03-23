package com.example.diabetesmanagement.ui.home;

import androidx.lifecycle.ViewModelProviders;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.SoundEffectConstants;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.diabetesmanagement.R;
import com.example.diabetesmanagement.constatntClass.ConstantClass;
import com.example.diabetesmanagement.data.User;
import com.example.diabetesmanagement.service.FirestoreCallback;
import com.example.diabetesmanagement.service.UserStorage;

public class SettingsFragment extends Fragment {

    private static final String TAG = "Setting Fragment";
    private SettingsViewModel mViewModel;
    ImageView bdge1, bdge2, bdge3, bdge4, bdge5, bdge6, bdge7;
    public int badgeIncrement = 1;

    private SettingsFragment.OnFragmentInteractionListener mListener;

    private Bundle bundle;

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.settings_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(SettingsViewModel.class);
        // TODO: Use the ViewModel
    }

    // Create an anonymous implementation of OnClickListener
    private View.OnClickListener mButtonListener = new View.OnClickListener() {
        public void onClick(View v) {
            v.playSoundEffect(SoundEffectConstants.CLICK);
            switch (v.getId()) {
                case R.id.button_logout:
                    bundle.putString("uri", "Logout");
                    onButtonPressed(bundle);
                    break;
                case R.id.image_levels:
                    bundle.putString("uri", "levels");
                    onButtonPressed(bundle);
                    break;
                case R.id.image_notifications:
                    bundle.putString("uri", "notifications");
                    onButtonPressed(bundle);
                    break;
                case R.id.image_medications:
                    bundle.putString("uri", "medications");
                    onButtonPressed(bundle);
                    break;
                case R.id.image_goals:
                    bundle.putString("uri", "goals");
                    onButtonPressed(bundle);
                    break;
                /*case R.id.button_notificationSetup:
                    showNotificationPopup(v);
                    break;*/
                default:
                    bundle.putString("uri", "");
                    onButtonPressed(bundle);
            }

        }
    };

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        view.findViewById(R.id.button_logout).setOnClickListener(mButtonListener);
        view.findViewById(R.id.image_levels).setOnClickListener(mButtonListener);
        view.findViewById(R.id.image_notifications).setOnClickListener(mButtonListener);
        view.findViewById(R.id.image_goals).setOnClickListener(mButtonListener);
        view.findViewById(R.id.image_medications).setOnClickListener(mButtonListener);
        bdge1 = view.findViewById(R.id.badge_iv1_id);
        bdge2 = view.findViewById(R.id.badge_iv2_id);
        bdge3 = view.findViewById(R.id.badge_iv3_id);
        bdge4 = view.findViewById(R.id.badge_iv4_id);
        bdge5 = view.findViewById(R.id.badge_iv5_id);
        bdge6 = view.findViewById(R.id.badge_iv6_id);
        bdge7 = view.findViewById(R.id.badge_iv7_id);
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("counterText", 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        int counterTextIncrement = sharedPreferences.getInt(ConstantClass.counterIncrement, 0);
        badgeIncrement = (counterTextIncrement % 700);
        badgeIncrement = badgeIncrement / 100;
        editor.putInt(ConstantClass.badgeIncrement, badgeIncrement);
        editor.apply();
        //Log.d(TAG, "onViewCCheck Values  "+"Counter Text: " + counterTextIncrement + "Badge Increment: "+badgeIncrement);

        UserStorage.readUser(new FirestoreCallback() {
            @Override
            public void onCallback(User user) {
                if (user.getMonday() == true) {
                    System.out.println("Sunday true");
                    bdge1.setVisibility(View.VISIBLE);
                } else {
                    bdge1.setVisibility(View.INVISIBLE);
                }

                if (user.getTuesday() == true) {
                    System.out.println("Monday true");
                    bdge2.setVisibility(View.VISIBLE);
                } else {
                    bdge2.setVisibility(View.INVISIBLE);
                }

                if (user.getWednesday() == true) {
                    System.out.println("Tuesday true");
                    bdge3.setVisibility(View.VISIBLE);
                } else {
                    bdge3.setVisibility(View.INVISIBLE);
                }

                if (user.getThursday() == true) {
                    System.out.println("Wednesday true");
                    bdge4.setVisibility(View.VISIBLE);
                } else {
                    bdge4.setVisibility(View.INVISIBLE);
                }

                if (user.getFriday() == true) {
                    System.out.println("Thursday true");
                    bdge5.setVisibility(View.VISIBLE);
                } else {
                    bdge5.setVisibility(View.INVISIBLE);
                }

                if (user.getSaturday() == true) {
                    System.out.println("Friday true");
                    bdge6.setVisibility(View.VISIBLE);
                } else {
                    bdge6.setVisibility(View.INVISIBLE);
                }

                if (user.getSunday() == true) {
                    System.out.println("Saturday true");
                    bdge7.setVisibility(View.VISIBLE);
                } else {
                    bdge7.setVisibility(View.INVISIBLE);
                }


            }
        });
    }




    // TODO: Rename method, update argument and hook method into UI event
    private void onButtonPressed(Bundle bundle) {
        if (mListener != null) {
            mListener.messageFromSettings(bundle);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        bundle = new Bundle();
        if (context instanceof SettingsFragment.OnFragmentInteractionListener) {
            mListener = (SettingsFragment.OnFragmentInteractionListener) context;
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

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void messageFromSettings(Bundle bundle);
    }


}
