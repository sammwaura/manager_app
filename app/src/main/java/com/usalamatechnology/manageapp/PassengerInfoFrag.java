package com.usalamatechnology.manageapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PassengerInfoFrag.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PassengerInfoFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PassengerInfoFrag extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String TAG = "PassengerInfoFrag";

    private TextView name;
    private TextView phone_no;
    private TextView seat_no;

    private OnFragmentInteractionListener mListener;

    public PassengerInfoFrag() {
        // Required empty public constructor
    }


    public static PassengerInfoFrag newInstance(String PassengerInfoFrag) {
        PassengerInfoFrag fragment = new PassengerInfoFrag();
        Bundle args = new Bundle();
        args.putString("PassengerInfoFrag", PassengerInfoFrag);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {

            name.findViewById(R.id.name);
            phone_no.findViewById(R.id.phone_n0);
            seat_no.findViewById(R.id.seat_no);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_passenger_info, container, false);

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


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
