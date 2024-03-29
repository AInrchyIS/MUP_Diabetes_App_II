package com.example.diabetesmanagement.ui.home;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.diabetesmanagement.AdapterLevelInterface;
import com.example.diabetesmanagement.R;
import com.example.diabetesmanagement.constatntClass.ConstantClass;
import com.example.diabetesmanagement.data.Goal;
import com.example.diabetesmanagement.data.User;
//import com.example.diabetesmanagement.service.LevelAlertFragment;
import com.example.diabetesmanagement.ui.home.user.data.management.GoalFragment;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LevelFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LevelFragment extends Fragment implements AdapterLevelInterface {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    //private static final String ARG_PARAM3 = "param3";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    RecyclerView recyclerView;
    ArrayList<RecyclerViewModel> dataholder;

    private OnFragmentInteractionListener mListener;
    private User user;
    private boolean registration;

    WarriorClubFragment mWarriorClubFragment;
    CaptaincyClubFragment mCaptaincyClubFragment;
    GovernorClubFragment mGovernorClubFragment;
    PresidencyClubFragment mPresidencyClubFragment;
    EliteClubFragment mEliteClubFragment;

    public LevelFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LevelFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LevelFragment newInstance(User param1, boolean param2) {
        LevelFragment fragment = new LevelFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, param1);
        args.putBoolean(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    /*@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            //registration = getArguments().getBoolean(ARG_PARAM3);
        }
    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_level, container, false);
        recyclerView = view.findViewById(R.id.recview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        dataholder = new ArrayList<>();

        mWarriorClubFragment = new WarriorClubFragment();
        mCaptaincyClubFragment = new CaptaincyClubFragment();
        mGovernorClubFragment = new GovernorClubFragment();
        mPresidencyClubFragment = new PresidencyClubFragment();
        mEliteClubFragment = new EliteClubFragment();


        RecyclerViewModel obj1 = new RecyclerViewModel(R.drawable.level1, "Warrior Club", "Level 1");
        dataholder.add(obj1);

        RecyclerViewModel obj2 = new RecyclerViewModel(R.drawable.level2, "Captaincy Club", "Level 2");
        dataholder.add(obj2);

        RecyclerViewModel obj3 = new RecyclerViewModel(R.drawable.level3, "Governor Club", "Level 3");
        dataholder.add(obj3);

        RecyclerViewModel obj4 = new RecyclerViewModel(R.drawable.level4, "Presidency Club", "Level 4");
        dataholder.add(obj4);

        RecyclerViewModel obj5 = new RecyclerViewModel(R.drawable.level5, "Elite Club", "Level 5");
        dataholder.add(obj5);

        recyclerView.setAdapter(new RecyclerViewAdapter(dataholder, getContext(), this));

        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Bundle bundle) {
        bundle.putBoolean("registration", registration);
        if (mListener != null) {
            mListener.messageFromLevelFragment(bundle);
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

    @Override
    public void levelAdapterMethod(final String levelPosition) {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("counterText", 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        int counterTextIncrement = sharedPreferences.getInt(ConstantClass.counterIncrement, 0);
        int levelBadgeIncrement = (counterTextIncrement % 500);
        levelBadgeIncrement = levelBadgeIncrement / 100;

        /**Get Badges Value from Firebase*/
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseFirestore.getInstance().collection("users")
                .document(uid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                try {
                    User userData = documentSnapshot.toObject(User.class);
                    assert userData != null;
                    switch (levelPosition) {
                        case "Level 1":
                            if (userData.getBadges() >= 5) {
                                Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.main_fragment, mWarriorClubFragment)
                                        .commitNow();
                            } else showAlertDialogLevels(5 - userData.getBadges());
                            break;
                        case "Level 2":
                            if (userData.getBadges() >= 15) {
                                Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.main_fragment, mCaptaincyClubFragment)
                                        .commitNow();
                            } else showAlertDialogLevels(15-userData.getBadges());
                            break;
                        case "Level 3":
                            if (userData.getBadges() >= 30) {
                                Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.main_fragment, mGovernorClubFragment)
                                        .commitNow();
                            } else showAlertDialogLevels(30-userData.getBadges());
                            break;
                        case "Level 4":
                            if (userData.getBadges() >= 50) {
                                Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.main_fragment, mPresidencyClubFragment)
                                        .commitNow();
                            } else showAlertDialogLevels(50-userData.getBadges());
                            break;
                        case "Level 5":
                            if (userData.getBadges() >= 75) {
                                Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.main_fragment, mEliteClubFragment)
                                        .commitNow();
                            } else showAlertDialogLevels(75-userData.getBadges());
                            break;
                    }
                } catch (Exception ignored) {

                }
            }
        });
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name

        void messageFromLevelFragment(Bundle bundle);
    }

    public void showAlertDialogLevels(int collectBadgesValue) {
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_dialogbox_level);
        TextView collectTextView = dialog.findViewById(R.id.custom_diaglog_totalbadges_tv_id);
        collectTextView.setText("Please collect " +  collectBadgesValue +  " badges to unlock this level.");
        TextView close_btn = dialog.findViewById(R.id.close_dialog_totalbadge);
        close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
        dialog.setCancelable(false);
    }

}
