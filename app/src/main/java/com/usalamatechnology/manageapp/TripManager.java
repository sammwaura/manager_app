package com.usalamatechnology.manageapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
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
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Timer;

import static com.usalamatechnology.manageapp.Constants.CREDENTIALSPREFERENCES;
import static com.usalamatechnology.manageapp.Constants.credentialsEditor;
import static com.usalamatechnology.manageapp.Constants.credentialsSharedPreferences;
import static com.usalamatechnology.manageapp.Constants.getAdminArea;
import static com.usalamatechnology.manageapp.Constants.getLocality;
import static com.usalamatechnology.manageapp.Constants.getManagerHeaderDetails;
import static com.usalamatechnology.manageapp.Constants.getManagerTrips;
import static com.usalamatechnology.manageapp.Constants.getSubLocality;
import static com.usalamatechnology.manageapp.Constants.getVehicleHeaderTrips;
import static com.usalamatechnology.manageapp.Constants.getVehicleTrips;
import static com.usalamatechnology.manageapp.Constants.latKey;
import static com.usalamatechnology.manageapp.Constants.lonKey;
import static com.usalamatechnology.manageapp.Constants.uploadApproval;
import static com.usalamatechnology.manageapp.Constants.uploadManagerTrip;
import static com.usalamatechnology.manageapp.Constants.user_id;

public class TripManager extends AppCompatActivity implements HomeIObserver {

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
    private GetHeader getHeader;
    private GetData getData;

    final int milliseconds = 1000;
    final int seconds = 30;// in your case as per question one minute
    final int oneMinute = 60 * milliseconds;// in your case as per question one minute
    final int minute = 1; // Time in munutes between distress call updates
    final int periodicUpdate = minute * milliseconds * seconds ;

    private String TAG = "TAG";

    public List<HomePost> homePosts;
    private RecyclerView rv;

    public static FragmentActivity host;
    HomeRVAdapter adapter;
    boolean results;

    String physical_address = "";

    String type="";
    String is_approved="";
    String selectedVehicle;

    private TextView_Lato trips_today,total_profits;
    Timer timer = new Timer(true);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_manager);

        selectedVehicle =getIntent().getStringExtra("VEHICLE_SELECTED");

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TripManager.this, Trips.class);
                startActivity(intent);
            }
        });

        credentialsSharedPreferences =getSharedPreferences(CREDENTIALSPREFERENCES, Context.MODE_PRIVATE);
        credentialsEditor = credentialsSharedPreferences.edit();
        //getHeaderDetails();
        TextView title_day = (TextView_Lato) findViewById(R.id.trips_today);
        TextView date_today = (TextView_Lato) findViewById(R.id.today);
        trips_today = (TextView_Lato) findViewById(R.id.total_trips);
        //total_profits = (TextView_Lato) view.findViewById(R.id.total_earned);

        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("d/M/Y");

        //title_day.setText("Number of trips" + sdf.format(c.getTime()));

        //date_today.setText("Transaction Timeline for "+ sdf.format(c.getTime()));

        homePosts = new ArrayList<>();


        rv = (RecyclerView) findViewById(R.id.rv);
        TextView emptyView = (TextView) findViewById(R.id.empty_view);

        LinearLayoutManager llm = new LinearLayoutManager(TripManager.this);

        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);

        if (CheckNetwork.isInternetAvailable(host)) {
            getData = new GetData();
            getData.execute("");

            getHeader = new GetHeader();
            getHeader.execute("");
        } else {
            findViewById(R.id.no_internet).setVisibility(View.VISIBLE);
            findViewById(R.id.progressBar).setVisibility(View.GONE);
            findViewById(R.id.empty_view).setVisibility(View.GONE);
            findViewById(R.id.rv).setVisibility(View.GONE);
        }

        //getHeaderDetails();

    }

    private class GetHeader extends AsyncTask<String, Integer, Boolean> {
        String data = null;


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


        @Override
        protected Boolean doInBackground(String... views) {

            return getAllPostFromOnline();
        }

        @Override
        protected void onPreExecute() {



                if (Looper.myLooper() == null) {
                    Looper.prepare();
                }
                findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
                findViewById(R.id.no_internet).setVisibility(View.GONE);
                findViewById(R.id.empty_view).setVisibility(View.GONE);
                findViewById(R.id.rv).setVisibility(View.GONE);


        }


        @Override
        protected void onPostExecute(Boolean result) {


                if (Looper.myLooper() == null) {
                    Looper.prepare();
                }
                findViewById(R.id.no_internet).setVisibility(View.GONE);
                findViewById(R.id.progressBar).setVisibility(View.GONE);
                findViewById(R.id.empty_view).setVisibility(View.GONE);
                findViewById(R.id.rv).setVisibility(View.VISIBLE);
        }

    }

    private boolean getAllPostFromOnline() {

        getHeader = new GetHeader();
        getHeader.execute("");

        results = false;
        RequestQueue queue = Volley.newRequestQueue(TripManager.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, getVehicleTrips,
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
                                    findViewById(R.id.no_internet).setVisibility(View.GONE);
                                    findViewById(R.id.progressBar).setVisibility(View.GONE);
                                    findViewById(R.id.empty_view).setVisibility(View.VISIBLE);
                                    findViewById(R.id.rv).setVisibility(View.GONE);
                                }

                                initializeData();
                                //refreshFragment();
                                results = true;
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(TripManager.this, "Error Fetching Data", Toast.LENGTH_LONG).show();
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
                        Toast.makeText(getApplicationContext(), "Error. Try Reopening App", Toast.LENGTH_LONG).show();

                        dialogBuilder = NiftyDialogBuilder.getInstance(TripManager.this);
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

                params.put("selectedVehicle", selectedVehicle);

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
        StringRequest stringRequest = new StringRequest(Request.Method.POST, getVehicleHeaderTrips,
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
                                Toast.makeText(TripManager.this, "Error Fetching Data", Toast.LENGTH_LONG).show();
                                results = false;
                            }

                        }
                        System.out.println("MAIN response h2" + s);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        System.out.println("volleyError response " + volleyError.getMessage());
                        //Showing toast
                        Toast.makeText(getApplicationContext(), "Error. Try Reopening App", Toast.LENGTH_LONG).show();

                        dialogBuilder = NiftyDialogBuilder.getInstance(TripManager.this);
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
                Map<String, String> params = new Hashtable<>();

                params.put("selectedVehicle", selectedVehicle);
                //returning parameters
                return params;
            }
        };


            requestQueue = Volley.newRequestQueue(TripManager.this);
            requestQueue.add(stringRequest);



        return results;
    }

    private void uploadTrip(final String invite, final String trip_id, final String mode) {

        System.out.println("Here uploadTrip "+invite+" trip_id "+trip_id+" mode "+mode);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, uploadManagerTrip,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {

                        initializeData();


                        System.out.println("////////////////123u "+s);
                        //textView.setText("End Trip");
                        Toast.makeText(TripManager.this, "Successfully saved rate", Toast.LENGTH_LONG).show();

                        getHeader = new GetHeader();
                        getHeader.execute("");


                        dialogBuilder.dismiss();

                        TripManager.this.finish();
                        Intent intent = new Intent(TripManager.this, TripManager.class);
                        intent.putExtra("VEHICLE_SELECTED", selectedVehicle);
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


            requestQueue = Volley.newRequestQueue(TripManager.this);
            requestQueue.add(stringRequest);


    }

    private void initializeData() {

        adapter = new HomeRVAdapter(homePosts);
        rv.setAdapter(adapter);
        adapter.setListener(this);
    }

    @Override
    public void onCardClicked(final int pos, String userid, String type, String time, String address, String city, String more, String amount, String category) {
        type= homePosts.get(pos).type.split("#")[0];
        is_approved= homePosts.get(pos).type.split("#")[1];
        if(type.equals("0"))
        {
            dialogBuilder = NiftyDialogBuilder.getInstance(TripManager.this);
            dialogBuilder
                    .withTitle("Trip Rate")
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
                    .setCustomView(R.layout.add_trip_dialog, TripManager.this)//def gone//def gone
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

                            if (Holder != null) {
                                if (ActivityCompat.checkSelfPermission(
                                        TripManager.this,
                                        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                                        &&
                                        ActivityCompat.checkSelfPermission(TripManager.this, Manifest.permission.ACCESS_COARSE_LOCATION)
                                                != PackageManager.PERMISSION_GRANTED) {
                                    return;
                                }

                            }
                            EditText t = (EditText) dialogBuilder.findViewById(R.id.editText5);
                            //dialogBuilder.findViewById(R.id.main_layout).setVisibility(View.GONE);
                            //dialogBuilder.findViewById(R.id.progressBar3).setVisibility(View.VISIBLE);
                            String rate = t.getText().toString();

                            if (!rate.isEmpty()) {
                                uploadTrip(rate,homePosts.get(pos).id,"save");
                                //dialogBuilder.findViewById(R.id.main_layout).setVisibility(View.GONE);
                                //dialogBuilder.findViewById(R.id.progressBar3).setVisibility(View.VISIBLE);
                            }
                        }
                    })
                    .show();
        }

    }

    @Override
    public void onCardApproved(final int pos, String userid, String type, String time, String address, String city, String more, String amount, String category) {

        if(type.split("#").length >1)
        {
            type= homePosts.get(pos).type.split("#")[0];
        }

        if(type.split("#").length >2)
        {
            is_approved= homePosts.get(pos).type.split("#")[1];
        }

         String phone = "";
        if(type.split("#").length >3)
        {
             phone = homePosts.get(pos).type.split("#")[2];
        }

        String trip_rate="";
        if(type.split("#").length >4)
        {
             trip_rate = homePosts.get(pos).type.split("#")[3];
        }

        String time_start="";
        if(type.split("#").length >5)
        {
             time_start = homePosts.get(pos).type.split("#")[4];
        }
        String time_end="";

        if(type.split("#").length >6)
        {
             time_end = homePosts.get(pos).type.split("#")[5];
        }

        String name ="";
        if(type.split("#").length >7)
        {
             name = homePosts.get(pos).type.split("#")[6];
        }

        String trip_amount ="";
        if(type.split("#").length >8)
        {
             trip_amount = homePosts.get(pos).type.split("#")[7];
        }








        String car_reg = homePosts.get(pos).city;
        String addressOfCar = homePosts.get(pos).address;

        if(type.equals("1") && is_approved.equals("0"))
        {
            dialogBuilder = NiftyDialogBuilder.getInstance(TripManager.this);
            dialogBuilder
                    .withTitle("Approve Trip")
                    .withTitleColor("#00B873")                                  //def
                    .withDividerColor("#00B873")                              //def//.withMessage(null)  no Msg
                    .withMessageColor("#00B873")                              //def  | withMessageColor(int resid)
                    .withDialogColor("#FFFFFF")                               //def  | withDialogColor(int resid)
                    .withDuration(700)
                    .isCancelableOnTouchOutside(false)
                    .isCancelable(false)
                    .withEffect(Effectstype.Newspager)
                    .withButton2Text("CANCEL")//def Effectstype.Slidetop
                    .withButton1Text("APPROVE")
                    .setCustomView(R.layout.approve_trip_dialog, TripManager.this)
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
                            uploadApproval(homePosts.get(pos).id);
                        }
                    });

            TextView textViewName = (TextView) dialogBuilder.findViewById(R.id.textViewName);
            TextView textViewLocation = (TextView) dialogBuilder.findViewById(R.id.textViewLocation);
            TextView textViewRate = (TextView) dialogBuilder.findViewById(R.id.textViewRate);
            TextView textViewTimeStart = (TextView) dialogBuilder.findViewById(R.id.textViewTimeStart);
            TextView textViewTimeEnd = (TextView) dialogBuilder.findViewById(R.id.textViewTimeEnd);
            TextView textViewTotal = (TextView) dialogBuilder.findViewById(R.id.textViewTotal);
            TextView textViewPhone = (TextView) dialogBuilder.findViewById(R.id.textViewPhone);
            TextView textCarReg = (TextView) dialogBuilder.findViewById(R.id.textViewCar);

            textViewName.setText("Driver Name : "+name);
            textViewLocation.setText("Location: "+addressOfCar);
            textViewRate.setText("Rate : Ksh. "+trip_rate+" per person");
            textViewTimeStart.setText("Time Start: "+time_start);
            textViewTimeEnd.setText("Time End: "+time_end);
            textViewTotal.setText("Total: Ksh."+trip_amount);
            textViewPhone.setText("Phone: "+phone);
            textCarReg.setText("Car Reg: "+car_reg);

            final String finalPhone = phone;
            textViewPhone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse("tel:" + finalPhone));
                    if (ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(TripManager.this, new String[]{Manifest.permission.CALL_PHONE},REQUEST_PHONE_CALL);
                    }
                    else
                    {
                        context.startActivity(intent);
                    }

                }
            });

            dialogBuilder.show();
        }
    }

    @Override
    public void onEditRate(final int pos, String userid, String type, String time, String address, String city, String more, String amount, String category) {
        final String managerRate = homePosts.get(pos).more;

        dialogBuilder = NiftyDialogBuilder.getInstance(TripManager.this);
        dialogBuilder
                .withTitle("Edit Trip Rate")
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
                .setCustomView(R.layout.edit_trip_dialog, TripManager.this)//def gone//def gone
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

                        if (Holder != null) {
                            if (ActivityCompat.checkSelfPermission(
                                    TripManager.this,
                                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                                    &&
                                    ActivityCompat.checkSelfPermission(TripManager.this, Manifest.permission.ACCESS_COARSE_LOCATION)
                                            != PackageManager.PERMISSION_GRANTED) {
                                return;
                            }

                        }
                        EditText t = (EditText) dialogBuilder.findViewById(R.id.editText5);
                        //dialogBuilder.findViewById(R.id.main_layout).setVisibility(View.GONE);
                        //dialogBuilder.findViewById(R.id.progressBar3).setVisibility(View.VISIBLE);
                        String rate = t.getText().toString();
                        //t.setText(managerRate);

                        if (!rate.isEmpty()) {
                            uploadTrip(rate,homePosts.get(pos).id,"edit");
                            //dialogBuilder.findViewById(R.id.main_layout).setVisibility(View.GONE);
                            //dialogBuilder.findViewById(R.id.progressBar3).setVisibility(View.VISIBLE);
                        }
                    }
                })
                .show();


    }

    private void uploadApproval(final String id) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, uploadApproval,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        dialogBuilder.dismiss();

                        TripManager.this.finish();
                        Intent intent = new Intent(TripManager.this, TripManager.class);
                        intent.putExtra("VEHICLE_SELECTED", selectedVehicle);
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

                params.put("id", id);


                //returning parameters
                return params;
            }
        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(TripManager.this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }
}
