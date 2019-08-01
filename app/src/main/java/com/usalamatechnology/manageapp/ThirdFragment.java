package com.usalamatechnology.manageapp;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
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

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import static com.usalamatechnology.manageapp.Constants.CREDENTIALSPREFERENCES;
import static com.usalamatechnology.manageapp.Constants.addressKey;
import static com.usalamatechnology.manageapp.Constants.currentTripId;
import static com.usalamatechnology.manageapp.Constants.getAdminArea;
import static com.usalamatechnology.manageapp.Constants.getLocality;
import static com.usalamatechnology.manageapp.Constants.getSubLocality;
import static com.usalamatechnology.manageapp.Constants.latKey;
import static com.usalamatechnology.manageapp.Constants.lonKey;
import static com.usalamatechnology.manageapp.Constants.tripStatus;
import static com.usalamatechnology.manageapp.Constants.credentialsSharedPreferences;
import static com.usalamatechnology.manageapp.Constants.credentialsEditor;

/**
 * Created by muhunyu on 11/07/18.
 */

public class ThirdFragment extends Fragment  {
    public static final String ARG_PAGE = "ARG_PAGE";
    private int mPageNo;
    public List<HomePost> homePosts;
    private RecyclerView rv;

    private NiftyDialogBuilder dialogBuilder;
    ImageView navdrawer;

    public static FragmentActivity host ;
    HomeRVAdapter adapter;
    private TextView_Lato textView;
    private TextView_Lato trips_today,total_profits;
    private boolean results;
    private GetHeader getHeader;

    public ThirdFragment() {
        // Required empty public constructor
    }

    public static ThirdFragment newInstance(int pageNo) {

        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, pageNo);
        ThirdFragment fragment = new ThirdFragment();
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

    private boolean getHeaderDetails() {
        results = false;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://zamzam45.com/tally_driver/getHeaderDetails.php",
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
                                try {
                                    // The comma in the format specifier does the trick
                                    s = String.format("%,d", Long.parseLong(totalExpense));
                                } catch (NumberFormatException e) {
                                }
                                total_profits.setText("Kes "+allIncome);

                                //initializeData();
                                //refreshFragment();
                                results = true;
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(getActivity(), "Error Fetching Data", Toast.LENGTH_LONG).show();
                                results = false;
                            }

                        }
                        System.out.println("MAIN response " + s);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Dismissing the progress dialog
                        //loading.dismiss();
                        System.out.println("volleyError response " + volleyError.getMessage());
                        //Showing toast
                        Toast.makeText(getActivity(), "Error Fetching Data", Toast.LENGTH_LONG).show();
                        results = false;
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new Hashtable<>();

                params.put("user_id", "1");
                params.put("roles_id", "2");
                //returning parameters
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        //Adding request to the queue
        requestQueue.add(stringRequest);
        return results;
    }

     View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         view = inflater.inflate(R.layout.fragment_third, container, false);
//        TextView textView = (TextView) view;
//        textView.setText("Fragment #" + mPageNo);

        credentialsSharedPreferences = getActivity().getSharedPreferences(CREDENTIALSPREFERENCES, Context.MODE_PRIVATE);
        credentialsEditor= credentialsSharedPreferences.edit();

        homePosts = new ArrayList<>();

        host=getActivity();
        rv = (RecyclerView) view.findViewById(R.id.rv);
        TextView emptyView = (TextView) view.findViewById(R.id.empty_view);

        trips_today = (TextView_Lato) view.findViewById(R.id.total_trips);
        total_profits = (TextView_Lato) view.findViewById(R.id.total_earned);

        textView = (TextView_Lato) view.findViewById(R.id.earning);

        if (credentialsSharedPreferences.getString(tripStatus, "0").equals("0")) {
            textView.setText("Start Trip");
        } else if (credentialsSharedPreferences.getString(tripStatus, "0").equals("1")) {
            textView.setText("End Trip");
        }

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), textView.getText(), Toast.LENGTH_SHORT).show();

                if (textView.getText().toString().equals("Start Trip")) {
                    dialogBuilder = NiftyDialogBuilder.getInstance(getActivity());
                    dialogBuilder
                            .withTitle("Add Trip")
                            .withTitleColor("#00B873")                                  //def
                            .withDividerColor("#00B873")                              //def//.withMessage(null)  no Msg
                            .withMessageColor("#00B873")                              //def  | withMessageColor(int resid)
                            .withDialogColor("#FFFFFF")                               //def  | withDialogColor(int resid)
                            .withDuration(700)
                            .isCancelableOnTouchOutside(false)
                            .isCancelable(false)
                            .withEffect(Effectstype.Newspager)
                            .withButton2Text("CANCEL")//def Effectstype.Slidetop
                            .withButton1Text("CONTINUE")
                            .setCustomView(R.layout.add_trip_dialog, getContext())//def gone//def gone
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




                                    EditText t = (EditText) dialogBuilder.findViewById(R.id.editText5);
                                    //dialogBuilder.findViewById(R.id.main_layout).setVisibility(View.GONE);
                                    //dialogBuilder.findViewById(R.id.progressBar3).setVisibility(View.VISIBLE);
                                    String rate = t.getText().toString();

                                    if (!rate.isEmpty()) {
                                        uploadTrip(rate);
                                        //dialogBuilder.findViewById(R.id.main_layout).setVisibility(View.GONE);
                                        //dialogBuilder.findViewById(R.id.progressBar3).setVisibility(View.VISIBLE);

                                    }


                                }
                            })
                            .show();
                } else if (textView.getText().toString().equals("End Trip")) {

                    credentialsEditor.putString(tripStatus, "0");

                    credentialsEditor.apply();

                    endTrip();



                }


            }
        });

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());

        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);

        getAllPostFromOnline();
        getHeader = new GetHeader(host, view);
        getHeader.execute("");

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

    private void endTrip() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://zamzam45.com/tally_driver/endTrip.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        System.out.println("////////////////123e "+s);
                        textView.setText("Start Trip");
                        Toast.makeText(getActivity(), "Successfully ended trip", Toast.LENGTH_LONG).show();

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

                params.put("id", credentialsSharedPreferences.getString(currentTripId, "0"));
                params.put("lat", String.valueOf(credentialsSharedPreferences.getString(latKey, "0")));
                params.put("lon", String.valueOf(credentialsSharedPreferences.getString(lonKey, "0")));
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

    private void uploadTrip(final String invite) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://zamzam45.com/tally_driver/uploadTrip.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //System.out.println("////////////////123 "+s);
                        textView.setText("End Trip");
                        Toast.makeText(getActivity(), "Successfully saved trip", Toast.LENGTH_LONG).show();


                        credentialsEditor.putString(tripStatus, "1");
                        credentialsEditor.putString(currentTripId, s);
                        credentialsEditor.apply();

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

                params.put("trip_rate", invite);
                params.put("lat", String.valueOf(credentialsSharedPreferences.getString(latKey, "0")));
                params.put("lon", String.valueOf(credentialsSharedPreferences.getString(lonKey, "0")));
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

    private void getAllPostFromOnline() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://zamzam45.com/tally_driver/getIncome.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if(s!=null)
                        {
                            JSONArray array = null;
                            try {
                                JSONObject jsonObj = new JSONObject(s);

                                JSONArray posts = jsonObj.getJSONArray("income_expenses");

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

                                    homePosts.add(new HomePost(id,amount,time,city,street,type,more,category,address));


                                }

                                initializeData();
                                //refreshFragment();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                        System.out.println("MAIN response " + s);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Dismissing the progress dialog
                        //loading.dismiss();
                        System.out.println("volleyError response " + volleyError.getMessage());
                        //Showing toast
                        Toast.makeText(getActivity(),"Error Fetching Data",Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new Hashtable<>();

                params.put("user_id","1");
                params.put("roles_id","2");
                //returning parameters
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        //Adding request to the queue
        requestQueue.add(stringRequest);
    }

    private void initializeData() {

        adapter = new HomeRVAdapter(homePosts);
        rv.setAdapter(adapter);

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
