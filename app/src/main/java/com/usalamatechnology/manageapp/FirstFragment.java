package com.usalamatechnology.manageapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Address;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;


import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.ResultReceiver;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gitonway.lee.niftymodaldialogeffects.lib.Effectstype;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import static android.widget.Toast.LENGTH_SHORT;
import static com.usalamatechnology.manageapp.Constants.CREDENTIALSPREFERENCES;
import static com.usalamatechnology.manageapp.Constants.addressKey;
import static com.usalamatechnology.manageapp.Constants.editTrip;
import static com.usalamatechnology.manageapp.Constants.getAdminArea;
import static com.usalamatechnology.manageapp.Constants.getLocality;
import static com.usalamatechnology.manageapp.Constants.getManagerHeaderDetails;
import static com.usalamatechnology.manageapp.Constants.getManagerTrips;
import static com.usalamatechnology.manageapp.Constants.getSubLocality;
import static com.usalamatechnology.manageapp.Constants.latKey;
import static com.usalamatechnology.manageapp.Constants.lonKey;
import static com.usalamatechnology.manageapp.Constants.roles_id;
import static com.usalamatechnology.manageapp.Constants.tripStatus;
import static com.usalamatechnology.manageapp.Constants.currentTripId;
import static com.usalamatechnology.manageapp.Constants.credentialsSharedPreferences;
import static com.usalamatechnology.manageapp.Constants.credentialsEditor;
import static com.usalamatechnology.manageapp.Constants.uploadApproval;
import static com.usalamatechnology.manageapp.Constants.uploadManagerTrip;
import static com.usalamatechnology.manageapp.Constants.user_id;
import static com.usalamatechnology.manageapp.Constants.savePayment;
import static com.usalamatechnology.manageapp.Constants.paymentDetails;



public class FirstFragment extends Fragment implements LocationListener, HomeIObserver, CustomDialogueListener {
    public static final String ARG_PAGE = "ARG_PAGE";
    private static final int PERMISSIONS_REQUEST_SEND_SMS = 100;
    private static final int REQUEST_PHONE_CALL = 200;
    private int mPageNo;
    private NiftyDialogBuilder dialogBuilder;
    ImageView navdrawer;
    public static final int RequestPermissionCode = 1;
    Context context;
    TextView_Lato textView;
    Intent intent1;
    Location location;
    LocationManager locationManager;
    boolean GpsStatus = false;
    Criteria criteria;
    String Holder;
    private double lat = 0.0;
    private double lon = 0.0;

    final int milliseconds = 1000;
    final int seconds = 30;// in your case as per question one minute
    final int oneMinute = 60 * milliseconds;// in your case as per question one minute
    final int minute = 1; // Time in munutes between distress call updates
    final int periodicUpdate = minute * milliseconds * seconds ;

    private String TAG = "TAG";
    private static final String SAVED_PAYMENT = "savedpayment";
    private static final int TARGET_FRAGMENT_REQUEST_CODE = 1;





    public List<HomePost> homePosts;
    ArrayList<Paymentdetails> paymentdetails;

     RecyclerView rv;

    public static FragmentActivity host;
    HomeRVAdapter adapter;
    boolean results;
    private GetData getData;
    private GetHeader getHeader;
    AddressResultReceiver mResultReceiver;
    private TextView_Lato trips_today,total_profits;
    Timer timer = new Timer(true);

    private MyInterface myInterface;

    DrawerLayout drawer;
     private Object CustomDialogue;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            myInterface = (MyInterface) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement MyInterface");
        }
    }


    public static FirstFragment getInstance() {
        Bundle args = new Bundle();
//        args.putInt(ARG_PAGE, pageNo);
        FirstFragment fragment = new FirstFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPageNo = getArguments().getInt(ARG_PAGE);
        credentialsSharedPreferences = getActivity().getSharedPreferences(CREDENTIALSPREFERENCES, Context.MODE_PRIVATE);
        credentialsEditor = credentialsSharedPreferences.edit();
        EnableRuntimePermission();
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        criteria = new Criteria();
        Holder = locationManager.getBestProvider(criteria, false);
        context = getContext();
        CheckGpsStatus();

    }



    private void showCustomDialog() {
        MyCustomDialogFragment myCustomDialogFragment = new MyCustomDialogFragment();
        myCustomDialogFragment.show(getFragmentManager(), getString(R.string.my_custom_dialog));
        myCustomDialogFragment.setTargetFragment(FirstFragment.this, TARGET_FRAGMENT_REQUEST_CODE);

    }


    String physical_address = "";

    String type="";
    String is_approved="";


    private void uploadApproval(final String id) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, uploadApproval,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        dialogBuilder.dismiss();
                        refreshFragment();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                //Creating parameters
                Map<String, String> params = new Hashtable<>();
                //Adding parameters

                params.put("id", id);


                //returning parameters
                return params;
            }
        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }




    @Override
    public void onCardClicked(String number_plate, String destination, String origin, String no_passenger, String rate, String amount) {
        //when using rv
    }

    @Override
    public void onApplyText(String number_plate, String amount, String name_of_passenger, String phone_no_of_passenger, String id_no_of_passenger, String destination) {
        textView.setText(number_plate);
        textView.setText(amount);
        textView.setText(name_of_passenger);
        textView.setText(phone_no_of_passenger);
        textView.setText(id_no_of_passenger);
        textView.setText(destination);
    }


    class AddressResultReceiver extends ResultReceiver {
        public AddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, final Bundle resultData) {
            if (resultCode == Constants.SUCCESS_RESULT) {
                final Address address = resultData.getParcelable(Constants.RESULT_ADDRESS);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        physical_address = "Address: " + address.getSubLocality() + " " + address.getSubAdminArea()
                                + " " + address.getAdminArea() + " " + address.getExtras() + " " + address.getLocale()
                                + " " + address.getLocality() + " " + address.getCountryName() + " " + address.describeContents();

                        if (address.getSubLocality() != null) {
                            credentialsEditor.putString(getSubLocality, address.getSubLocality());
                            credentialsEditor.apply();
                        }

                        if (address.getLocality() != null) {
                            credentialsEditor.putString(getLocality, address.getLocality());
                            credentialsEditor.apply();
                        }

                        if (address.getAdminArea() != null) {
                            credentialsEditor.putString(getAdminArea, address.getAdminArea());
                            credentialsEditor.apply();
                        }

                        //System.out.println("Physical address ###$$$### "+physical_address);

                    }
                });
            } else {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        physical_address = resultData.getString(Constants.RESULT_DATA_KEY);
                        credentialsEditor.putString(addressKey, physical_address);
                        credentialsEditor.apply();
                        Toast.makeText(getActivity(), "Unable to get location" + physical_address, Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    }

    View view;



    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_first, container, false);

        view.findViewById(R.id.makePayment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(CheckNetwork.isInternetAvailable(getActivity()))
                {
//                    getAllPostFromOnline(view);
                    refreshFragment();
                    getHeaderDetails();
//                    showCustomDialog();

                }

            }
        });
//        retrievePaymentDetails();






//        CardView cardView = (CardView) view.findViewById(R.id.cardPay);
//        cardView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getActivity(), PassengerActivity.class);
//                startActivity(intent);
//
//            }
//        });


        TextView title_day = (TextView_Lato) view.findViewById(R.id.trips_today);
        TextView date_today = (TextView_Lato) view.findViewById(R.id.today);
        trips_today = (TextView_Lato) view.findViewById(R.id.total_trips);
        //total_profits = (TextView_Lato) view.findViewById(R.id.total_earned);

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.SEND_SMS}, PERMISSIONS_REQUEST_SEND_SMS);
            if (Looper.myLooper() == null) {
                Looper.prepare();
            }
            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
            if(getActivity() != null)
            {
                // Toast.makeText(getActivity(), "Please grant permission to send SMS for distress to be sent",
                //       Toast.LENGTH_LONG).show();
            }

        }

        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("d/M/Y");

        //title_day.setText("Number of trips" + sdf.format(c.getTime()));

        //date_today.setText("Transaction Timeline for "+ sdf.format(c.getTime()));

        homePosts = new ArrayList<>();

        host = getActivity();
        rv = (RecyclerView) view.findViewById(R.id.rv);
        TextView emptyView = (TextView) view.findViewById(R.id.empty_view);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());

        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);


        CheckGpsStatus();


        if (Holder != null) {
            if (ActivityCompat.checkSelfPermission(
                    getContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    &&
                    ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED) {

            }
            location = locationManager.getLastKnownLocation(Holder);
            locationManager.requestLocationUpdates(Holder, 5000, 1, FirstFragment.this);
        }


        view.findViewById(R.id.no_internet).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CheckNetwork.isInternetAvailable(host)) {
                    getData = new GetData(host, view);
                    getData.execute("");
                } else {
                    view.findViewById(R.id.no_internet).setVisibility(View.VISIBLE);
                    view.findViewById(R.id.progressBar).setVisibility(View.GONE);
                    view.findViewById(R.id.empty_view).setVisibility(View.GONE);
                    view.findViewById(R.id.rv).setVisibility(View.GONE);
                }
            }
        });

        if (CheckNetwork.isInternetAvailable(host)) {
            getData = new GetData(host, view);
            getData.execute("");

            getHeader = new GetHeader(host, view);
            getHeader.execute("");
        } else {
            view.findViewById(R.id.no_internet).setVisibility(View.VISIBLE);
            view.findViewById(R.id.progressBar).setVisibility(View.GONE);
            view.findViewById(R.id.empty_view).setVisibility(View.GONE);
            view.findViewById(R.id.rv).setVisibility(View.GONE);
        }

        navdrawer = (ImageView) view.findViewById(R.id.back);
        navdrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer = myInterface.getDrawer();
                //Toast.makeText(v.getContext(), "Clicked drawer", Toast.LENGTH_SHORT).show();

                if (drawer.isDrawerOpen(Gravity.LEFT)) {
                    drawer.closeDrawer(Gravity.LEFT);
                    //Toast.makeText(v.getContext(), "Closing drawer", Toast.LENGTH_SHORT).show();
                } else {
                    drawer.openDrawer(Gravity.LEFT);
                    //Toast.makeText(v.getContext(), "Opening drawer", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //textView = (TextView_Lato) view.findViewById(R.id.earning);

       /* if (credentialsSharedPreferences.getString(tripStatus, "0").equals("0")) {
            textView.setText("Start Trip");
        } else if (credentialsSharedPreferences.getString(tripStatus, "0").equals("1")) {
            textView.setText("End Trip");
        }*/
        /*
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), textView.getText(), Toast.LENGTH_SHORT).show();
                if (textView.getText().toString().equals("Start Trip")) {
                } else if (textView.getText().toString().equals("End Trip")) {
                    credentialsEditor.putString(tripStatus, "0");
                    credentialsEditor.apply();
                    endTrip();
                }
            }
        });*/


        if(credentialsSharedPreferences.getString(roles_id, "0").equals("5"))
        {
            view.findViewById(R.id.expenses).setVisibility(View.VISIBLE);
        }
        else
        {
            view.findViewById(R.id.expenses).setVisibility(View.GONE);
        }

//        view.findViewById(R.id.expenses).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(view.getContext(), Expenses.class);
//                startActivity(intent);
//            }
//        });




        return view;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK){
            return;
        }
        if (requestCode == TARGET_FRAGMENT_REQUEST_CODE ){

//            Bundle extra = data.getBundleExtra(SAVED_PAYMENT);
            rv.setAdapter(adapter);
        }
    }


    public static Intent newIntent( String s) {
        Intent intent = new Intent();
        intent.putExtra( SAVED_PAYMENT, s);
//        intent.getExtras();
        return intent;
    }

//    private void retrievePaymentDetails() {
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, paymentDetails,
//                new Response.Listener <String>() {
//                    @Override
//                    public void onResponse(String s) {
//                        System.out.println("123RETRIEEEVE" + s);
////                        textView.setText("retrieve details");
//                        Toast.makeText(getActivity(), "successfully retrieved", Toast.LENGTH_SHORT).show();
//                        try {
//                            JSONObject jsonObject = new JSONObject(s);
//                            JSONArray array = jsonObject.getJSONArray("payments");
//
//                            for (int i =0; i< array.length(); i++){
//                                JSONObject row = array.getJSONObject(i);
//                                Paymentdetails paymentdetail = new Paymentdetails(
//                                        row.getString("number_plate"),
//                                        row.getInt("amount"),
//
//                                        row.getString("destination")
//                                );
//                                paymentdetails.add(paymentdetail);
//
//                            }
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError volleyError) {
//                        System.out.println("volleyError error" + volleyError.getMessage());
//                        Toast.makeText(getActivity(), "Poor network connection.", Toast.LENGTH_LONG).show();
//
//                    }
//                }) {
//        };
//
//        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
//        requestQueue.add(stringRequest);
//
//        }






    private class GetHeader extends AsyncTask<String, Integer, Boolean> {
        String data = null;
        private Context mContext;
        private View rootView;

        public GetHeader(Context context, View rootView) {
            this.mContext = context;
            this.rootView = rootView;
        }

        @Override
        protected Boolean doInBackground(String... views) {
            return getHeaderDetails();
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onPostExecute(Boolean result) {

        }

    }


    private class GetData extends AsyncTask<String, Integer, Boolean> {

        String data = null;
        private Context mContext;
        private View rootView;

        public GetData(Context context, View rootView) {
            this.mContext = context;
            this.rootView = rootView;
        }

        @Override
        protected Boolean doInBackground(String... views) {

            return getAllPostFromOnline(rootView);
        }

        @Override
        protected void onPreExecute() {

            if (getActivity() != null) {

                if (Looper.myLooper() == null) {
                    Looper.prepare();
                }
                rootView.findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
                rootView.findViewById(R.id.no_internet).setVisibility(View.GONE);
                rootView.findViewById(R.id.empty_view).setVisibility(View.GONE);
                rootView.findViewById(R.id.rv).setVisibility(View.GONE);

            }
        }


        @Override
        protected void onPostExecute(Boolean result) {
            if (getActivity() != null) {

                if (Looper.myLooper() == null) {
                    Looper.prepare();
                }
                rootView.findViewById(R.id.no_internet).setVisibility(View.GONE);
                rootView.findViewById(R.id.progressBar).setVisibility(View.GONE);
                rootView.findViewById(R.id.empty_view).setVisibility(View.GONE);
                rootView.findViewById(R.id.rv).setVisibility(View.VISIBLE);

            }

        }

    }


    private boolean getAllPostFromOnline(final View rootView) {

        getHeader = new GetHeader(host, view);
        getHeader.execute("");

        results = false;
        RequestQueue queue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, getManagerTrips,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        JSONArray posts = null;
                        if (s != null) {
                            JSONArray array = null;
                            try {
                                JSONObject jsonObj = new JSONObject(s);
                                posts = jsonObj.getJSONArray("trips");
                                if(posts.length() >0)
                                {
                                    homePosts.clear();
                                    for (int i = 0; i < posts.length(); i++) {
                                        JSONObject row = posts.getJSONObject(i);

                                        String id = row.getString("id");
                                        String amount = row.getString("amount");
                                        String time = row.getString("time");
                                        String street = row.getString("street");
                                        String city = row.getString("city");
                                        String type = row.getString("type");
                                        String more = row.getString("more");
                                        String category = row.getString("category");
                                        String address = row.getString("address");

                                        homePosts.add(new HomePost(id, amount, time, city, street, type, more, category, address));


                                    }
                                }
                                else
                                {
                                    rootView.findViewById(R.id.no_internet).setVisibility(View.GONE);
                                    rootView.findViewById(R.id.progressBar).setVisibility(View.GONE);
                                    rootView.findViewById(R.id.empty_view).setVisibility(View.VISIBLE);
                                    rootView.findViewById(R.id.rv).setVisibility(View.GONE);
                                }

                                initializeData();
                                //refreshFragment();
                                results = true;
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(getActivity(), "Server did not return any useful data", Toast.LENGTH_LONG).show();
                                results = false;
                            }

                        }
                        System.out.println("MAIN response first " + s);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {


                        System.out.println("volleyError response " + volleyError.getMessage());
                        //Showing toast
                        Toast.makeText(getActivity(), "Error. Try Reopening App", Toast.LENGTH_LONG).show();

                        dialogBuilder = NiftyDialogBuilder.getInstance(getActivity());
                        dialogBuilder
                                .withTitle("Re-open App")
                                .withTitleColor("#00B873")                                  //def
                                .withDividerColor("#00B873")                              //def//.withMessage(null)  no Msg
                                .withMessageColor("#00B873")                              //def  | withMessageColor(int resid)
                                .withDialogColor("#FFFFFF")                               //def  | withDialogColor(int resid)
                                .withDuration(700)
                                .isCancelableOnTouchOutside(false)
                                .isCancelable(false)
                                .withEffect(Effectstype.Fadein)
                                .withButton2Text("CANCEL")//def Effectstype.Slidetop
                                .withButton1Text("CONTINUE")
                                .withMessage("The server is not responding. Close the app and try again")
                                .isCancelableOnTouchOutside(true)
                                .setButton2Click(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialogBuilder.dismiss();
                                    }
                                })
                                .setButton1Click(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        //  Toast.makeText(v.getContext(), "Okay", Toast.LENGTH_SHORT).show();

                                        ExitActivity.exitApplication(v.getContext());


                                    }
                                })
                                .show();
                        results = false;

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                if (location != null) {
                    lat = location.getLatitude();
                    lon = location.getLongitude();

                }

                Map<String, String> params = new Hashtable<>();

                params.put("user_id", "1");
                params.put("roles_id", "2");
                params.put("lat", String.valueOf(lat));
                params.put("lon", String.valueOf(lon));
                //returning parameters
                return params;
            }
        };


        // Add the realibility on the connection.
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 1, 1.0f));

        // Start the request immediately
        queue.add(stringRequest);
        return results;
    }

    RequestQueue requestQueue;
    private boolean getHeaderDetails() {
        results = false;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, getManagerHeaderDetails,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (s != null) {
                            JSONArray array = null;
                            try {
                                JSONObject jsonObj = new JSONObject(s);

                                JSONArray posts = jsonObj.getJSONArray("header_details");

                                String totalTrips="";
                                String totalProfit="";
                                String totalIncome="";
                                String totalExpense="";
                                String allIncome="";
                                String allProfit="";
                                String allTrips="";
                                String allExpense="";

                                for (int i = 0; i < 1; i++) {
                                    JSONObject row = posts.getJSONObject(i);
                                    totalTrips = row.getString("totalTrips");
                                    totalProfit = row.getString("totalProfit");
                                    totalIncome = row.getString("totalIncome");
                                    totalExpense = row.getString("totalExpense");
                                    allIncome = row.getString("allIncome");
                                    allProfit = row.getString("allProfit");
                                    allTrips = row.getString("allTrips");
                                    allExpense = row.getString("allExpense");
                                }

                                trips_today.setText(allTrips);
                                String sv="";
                                /*try {
                                    // The comma in the format specifier does the trick
                                    s = String.format("%,d", Long.parseLong(totalExpense));
                                } catch (NumberFormatException e) {
                                }*/

                                //total_profits.setText("Kes "+allExpense);

                                //initializeData();
                                //refreshFragment();
                                results = true;
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(getActivity(), "Error Fetching Data", Toast.LENGTH_LONG).show();
                                results = false;
                            }

                        }
                        System.out.println("MAIN response header " + s);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(getActivity(), "Error. Try Reopening App", Toast.LENGTH_LONG).show();

                        dialogBuilder = NiftyDialogBuilder.getInstance(getActivity());
                        dialogBuilder
                                .withTitle("Re-open App")
                                .withTitleColor("#00B873")                                  //def
                                .withDividerColor("#00B873")                              //def//.withMessage(null)  no Msg
                                .withMessageColor("#00B873")                              //def  | withMessageColor(int resid)
                                .withDialogColor("#FFFFFF")                               //def  | withDialogColor(int resid)
                                .withDuration(700)
                                .isCancelableOnTouchOutside(false)
                                .isCancelable(false)
                                .withEffect(Effectstype.Fadein)
                                .withButton2Text("CANCEL")//def Effectstype.Slidetop
                                .withButton1Text("CONTINUE")
                                .withMessage("The server is not responding. Close the app and try again")
                                .isCancelableOnTouchOutside(true)
                                .setButton2Click(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialogBuilder.dismiss();
                                    }
                                })
                                .setButton1Click(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        //  Toast.makeText(v.getContext(), "Okay", Toast.LENGTH_SHORT).show();

                                        ExitActivity.exitApplication(v.getContext());


                                    }
                                });
                        results = false;
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new Hashtable<>();

                params.put("user_id", credentialsSharedPreferences.getString(user_id, ""));
                params.put("roles_id", "4");
                params.put("lat", String.valueOf(lat));
                params.put("lon", String.valueOf(lon));
                //returning parameters
                return params;
            }
        };


        requestQueue = Volley.newRequestQueue(getActivity());
        //Adding request to the queue
        requestQueue.add(stringRequest);



        return results;
    }

    public void refreshFragment() {
              if (getActivity() != null) {
//
//            try {
//                getActivity().getSupportFragmentManager()
//                        .beginTransaction()
//                        .detach(this)
//                        .attach(this)
//                        .commit();
//            } catch (Exception e) {
//
//                e.printStackTrace();
//            }
        }
    }

    private void initializeData() {

        adapter = new HomeRVAdapter(homePosts);
        rv.setAdapter(adapter);
        adapter.setListener(this);
    }


    public void CheckGpsStatus() {

        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        GpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (!GpsStatus) {
            Toast.makeText(getContext(), "Please Enable GPS First", Toast.LENGTH_LONG).show();
        }

    }

    public void EnableRuntimePermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION)) {

            Toast.makeText(getContext(), "ACCESS_FINE_LOCATION permission allows us to Access GPS in app", Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(getActivity(), new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION}, RequestPermissionCode);

        }
    }

    @Override
    public void onRequestPermissionsResult(int RC, String per[], int[] PResult) {

        switch (RC) {

            case RequestPermissionCode:

                if (PResult.length > 0 && PResult[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(getContext(), "Permission Granted, Now your application can access GPS.", Toast.LENGTH_LONG).show();

                } else {

                    Toast.makeText(getContext(), "Permission Canceled, Now your application cannot access GPS.", Toast.LENGTH_LONG).show();

                }
                break;
        }
    }

    private void endTrip() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, editTrip,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        System.out.println("////////////////123e "+s);
                        textView.setText("Start Trip");
                        Toast.makeText(getActivity(), "Successfully ended trip", Toast.LENGTH_LONG).show();
                        getHeader = new GetHeader(host, view);
                        getHeader.execute("");

                        credentialsEditor.putString(tripStatus, "0");
                        credentialsEditor.apply();

                        //dialogBuilder.dismiss();
                        Intent intent = new Intent(view.getContext(), Earnings.class);
                        startActivity(intent);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                //Creating parameters
                Map<String, String> params = new Hashtable<>();
                //Adding parameters
                if (location != null) {
                    lat = location.getLatitude();
                    lon = location.getLongitude();


                }
                params.put("id", credentialsSharedPreferences.getString(currentTripId, "0"));
                params.put("lat", String.valueOf(lat));
                params.put("lon", String.valueOf(lon));
                params.put("address", credentialsSharedPreferences.getString(getSubLocality, "") + "," + credentialsSharedPreferences.getString(getLocality, "") + "," + credentialsSharedPreferences.getString(getAdminArea, ""));


                //returning parameters
                return params;
            }
        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        //Adding request to the queue
        requestQueue.add(stringRequest);

    }

    private void uploadTrip(final String invite, final String trip_id, final String mode) {

        System.out.println("Here uploadTrip "+invite+" trip_id "+trip_id+" mode "+mode);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, uploadManagerTrip,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {

                        initializeData();
                        refreshFragment();
                        System.out.println("////////////////123u "+s);
                        //textView.setText("End Trip");
                        Toast.makeText(getActivity(), "Successfully saved rate", Toast.LENGTH_LONG).show();

                        getHeader = new GetHeader(host, view);
                        getHeader.execute("");


                        dialogBuilder.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                //Creating parameters
                Map<String, String> params = new Hashtable<>();
                //Adding parameters
                if (location != null) {
                    lat = location.getLatitude();
                    lon = location.getLongitude();


                }
                params.put("trip_rate", invite);
                params.put("trip_id", trip_id);
                params.put("mode", mode);
                params.put("user_id", credentialsSharedPreferences.getString(user_id, ""));
                params.put("lat", String.valueOf(lat));
                params.put("lon", String.valueOf(lon));
                params.put("address", credentialsSharedPreferences.getString(getSubLocality, "") + "," + credentialsSharedPreferences.getString(getLocality, "") + "," + credentialsSharedPreferences.getString(getAdminArea, ""));

                //returning parameters
                return params;
            }
        };


        requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);


    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }


    @Override
    public void onLocationChanged(Location location) {
        FirstFragment.this.location = location;
        credentialsEditor.putString(latKey, String.valueOf(location.getLatitude()));
        credentialsEditor.putString(lonKey, String.valueOf(location.getLongitude()));
        credentialsEditor.apply();

        mResultReceiver = new AddressResultReceiver(null);

        if(getActivity() != null)
        {
            Intent intent = new Intent(getActivity(), GeocodeAddressIntentService.class);
            intent.putExtra(Constants.RECEIVER, mResultReceiver);
            intent.putExtra(Constants.FETCH_TYPE_EXTRA, Constants.USE_ADDRESS_LOCATION);

            intent.putExtra(Constants.LOCATION_LATITUDE_DATA_EXTRA,
                    Double.parseDouble(String.valueOf(location.getLatitude())));
            intent.putExtra(Constants.LOCATION_LONGITUDE_DATA_EXTRA,
                    Double.parseDouble(String.valueOf(location.getLongitude())));
            //Toast.makeText(getActivity(),"1 latitude "+String.valueOf(location.getLatitude())+" longitude "+String.valueOf(location.getLongitude()), Toast.LENGTH_LONG).show();
            Log.e(TAG, "Starting Service");
            getActivity().startService(intent);
        }

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }


}
