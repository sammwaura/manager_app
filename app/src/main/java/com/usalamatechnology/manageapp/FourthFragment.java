package com.usalamatechnology.manageapp;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.telephony.SmsManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.andexert.library.RippleView;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;

import pl.bclogic.pulsator4droid.library.PulsatorLayout;

import static com.usalamatechnology.manageapp.Constants.CREDENTIALSPREFERENCES;
import static com.usalamatechnology.manageapp.Constants.credentialsEditor;
import static com.usalamatechnology.manageapp.Constants.credentialsSharedPreferences;
import static com.usalamatechnology.manageapp.Constants.latKey;
import static com.usalamatechnology.manageapp.Constants.lonKey;

/**
 * Created by muhunyu on 11/07/18.
 */

public class FourthFragment extends Fragment {
    public static final String ARG_PAGE = "ARG_PAGE";
    private int mPageNo;
    private NiftyDialogBuilder dialogBuilder;
    private static final int PERMISSIONS_REQUEST_SEND_SMS = 100;
    ImageView navdrawer;

    public FourthFragment() {
        // Required empty public constructor
    }

    public static FourthFragment newInstance(int pageNo) {

        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, pageNo);
        FourthFragment fragment = new FourthFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private MyInterface myInterface;

    DrawerLayout drawer;
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            myInterface = (MyInterface) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement MyInterface");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPageNo = getArguments().getInt(ARG_PAGE);
    }

     View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         view = inflater.inflate(R.layout.fragment_fourth, container, false);
        // Inflate the layout for this fragment
        final TextView text = (TextView) view.findViewById(R.id.text_help);

        final PulsatorLayout pulsator = (PulsatorLayout) view.findViewById(R.id.pulsatorgreen);
        pulsator.start();
        credentialsSharedPreferences = getActivity().getSharedPreferences(CREDENTIALSPREFERENCES, Context.MODE_PRIVATE);
        credentialsEditor= credentialsSharedPreferences.edit();

        final PulsatorLayout pulsator2 = (PulsatorLayout) view.findViewById(R.id.pulsatorred);
        pulsator2.start();


            final RippleView rippleView = (RippleView) view.findViewById(R.id.more);
            rippleView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    rippleView.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {

                        @Override
                        public void onComplete(RippleView rippleView) {
                            pulsator.setVisibility(View.GONE);
                            pulsator2.setVisibility(View.VISIBLE);

                            final RippleView rippleView2 = (RippleView) view.findViewById(R.id.more2);
                            rippleView2.setOnClickListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View v) {

                                    Toast.makeText(getActivity(), "Successfully cancelled distress",
                                            Toast.LENGTH_LONG).show();

                                    rippleView2.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {

                                        @Override
                                        public void onComplete(RippleView rippleView) {
                                            pulsator2.setVisibility(View.GONE);
                                            pulsator.setVisibility(View.VISIBLE);
                                        }

                                    });
                                }
                            });
                            startSendMessages();
                        }

                    });
                }
            });






        navdrawer = (ImageView)view.findViewById(R.id.back);
        navdrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer =myInterface.getDrawer();
                //Toast.makeText(v.getContext(), "Clicked drawer", Toast.LENGTH_SHORT).show();

                if (drawer.isDrawerOpen(Gravity.LEFT)) {
                    drawer.closeDrawer( Gravity.START);
                    //Toast.makeText(v.getContext(), "Closing drawer", Toast.LENGTH_SHORT).show();
                } else {
                    drawer.openDrawer( Gravity.START);
                    //Toast.makeText(v.getContext(), "Opening drawer", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }

    private void startSendMessages() {
        if(getActivity() != null)
        {
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.SEND_SMS)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.SEND_SMS}, PERMISSIONS_REQUEST_SEND_SMS);
                if (Looper.myLooper() == null) {
                    Looper.prepare();
                }
                //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
                if(getActivity() != null)
                {
                    Toast.makeText(getActivity(), "Please grant permission to send SMS for distress to be sent",
                            Toast.LENGTH_LONG).show();
                }

            } else {

                Toast.makeText(getActivity(), "SOS sent to admin.",
                        Toast.LENGTH_LONG).show();


                SmsManager smsManager = SmsManager.getDefault();

                    smsManager.sendTextMessage(
                            "+254700402788", null,
                            "Tally Alert: Help, I am the driver of Bus KCE 2547K and I am in an emergency.Location:http://maps.google.com/?q="+String.valueOf(credentialsSharedPreferences.getString(latKey, "0"))+","+String.valueOf(credentialsSharedPreferences.getString(lonKey, "0"))+"&z=14", null,
                            null);

                Intent intent = new Intent(view.getContext(), Expenses.class);
                intent.putExtra(Constants.type, "Accident");
                startActivity(intent);


            }
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }
}
