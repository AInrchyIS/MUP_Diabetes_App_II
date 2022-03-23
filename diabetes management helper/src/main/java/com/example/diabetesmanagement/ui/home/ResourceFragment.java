package com.example.diabetesmanagement.ui.home;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.diabetesmanagement.R;
import com.example.diabetesmanagement.service.UserStorage;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ResourceFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ResourceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ResourceFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ResourceFragment() {
        // Required empty public constructor
    }

    /*
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ResourceFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ResourceFragment newInstance() {
        ResourceFragment fragment = new ResourceFragment();
        Bundle args = new Bundle();
        //args.putString(ARG_PARAM1, param1);
        //args.putString(ARG_PARAM2, param2);
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
        return inflater.inflate(R.layout.fragment_resource, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.button_resource_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onButtonPressed(Uri.parse("back"));
            }
        });

        TextView aade = view.findViewById(R.id.link_aade);
        SpannableString content = new SpannableString(getString(R.string.aadelink));
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        aade.setText(content);
        aade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserStorage.storeActivity(UserStorage.LINK);
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.aadeurl)));
                startActivity(i);
            }
        });

        TextView ada = view.findViewById(R.id.link_ada);
        content = new SpannableString(getString(R.string.adalink));
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        ada.setText(content);
        ada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserStorage.storeActivity(UserStorage.LINK);
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.adaurl)));
                startActivity(i);
            }
        });

        TextView ndep = view.findViewById(R.id.link_ndep);
        content = new SpannableString(getString(R.string.ndeplink));
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        ndep.setText(content);
        ndep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserStorage.storeActivity(UserStorage.LINK);
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.ndepurl)));
                startActivity(i);
            }
        });

        TextView niddk = view.findViewById(R.id.link_niddk);
        content = new SpannableString(getString(R.string.niddklink));
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        niddk.setText(content);
        niddk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserStorage.storeActivity(UserStorage.LINK);
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.niddkurl)));
                startActivity(i);
            }
        });

        TextView dtf = view.findViewById(R.id.link_dtf);
        content = new SpannableString(getString(R.string.dtflink));
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        dtf.setText(content);
        dtf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserStorage.storeActivity(UserStorage.LINK);
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.dtfurl)));
                startActivity(i);
            }
        });

        TextView hline = view.findViewById(R.id.link_hline);
        content = new SpannableString(getString(R.string.hlinelink));
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        hline.setText(content);
        hline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserStorage.storeActivity(UserStorage.LINK);
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.hlineurl)));
                startActivity(i);
            }
        });
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.messageFromResources(uri);
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
        void messageFromResources(Uri uri);
    }
}
