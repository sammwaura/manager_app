package com.usalamatechnology.manageapp.ui;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.usalamatechnology.manageapp.Interface.PaymentObserver;
import com.usalamatechnology.manageapp.R;
import com.usalamatechnology.manageapp.adapter.PaymentAdapter;
import com.usalamatechnology.manageapp.models.Constants;
import com.usalamatechnology.manageapp.models.PassengerDetails;
import com.usalamatechnology.manageapp.models.Paymentdetails;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;
import java.util.Objects;

import static com.usalamatechnology.manageapp.models.Constants.CREDENTIALSPREFERENCES;
import static com.usalamatechnology.manageapp.models.Constants.credentialsEditor;
import static com.usalamatechnology.manageapp.models.Constants.credentialsSharedPreferences;
import static com.usalamatechnology.manageapp.models.Constants.email;
import static com.usalamatechnology.manageapp.models.Constants.paymentDetails;
import static com.usalamatechnology.manageapp.models.Constants.vehicle_no;

public class Home extends AppCompatActivity {


    private static final String TAG = "Home Activity";

    DrawerLayout drawer;

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    PaymentAdapter paymentAdapter;
    private ArrayList<Paymentdetails>paymentdetails;
    TextView total_trips;

    private ArrayList<PassengerDetails>passengerDetails;
    @SuppressLint({"WrongViewCast", "ResourceType"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        credentialsSharedPreferences =getSharedPreferences(CREDENTIALSPREFERENCES, Context.MODE_PRIVATE);
        credentialsEditor = credentialsSharedPreferences.edit();


        total_trips = findViewById(R.id.total_trips);



        findViewById(R.id.menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DrawerLayout drawer = findViewById(R.id.drawer_layout);
                drawer.openDrawer(Gravity.START);
            }
        });

        findViewById(R.id.home).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Home.this,"Home",Toast.LENGTH_LONG).show();
                Intent it = new Intent(Home.this, Home.class);
                startActivity(it);
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
            }
        });



        findViewById(R.id.makePayment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomDialog dialog = new CustomDialog();
                dialog.show(getSupportFragmentManager(), "Custom Dialog");
            }
        });

        findViewById(R.id.expenses).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, Expenses.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.expense).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, ViewExpenses.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.fare).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, Fare.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.courier).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, Courier.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, PinActivity.class);
                startActivity(intent);
            }
        });





        paymentdetails = new ArrayList <>();



        recyclerView =  findViewById(R.id.recyclerview);
        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(llm);
        recyclerView.setHasFixedSize(true);

        retrievePaymentDetails();
    }


    private void retrievePaymentDetails() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Retrieving data....");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                "https://zamzam45.com/tally_driver_copy/get_payment_details.php",
                new Response.Listener <String>() {
                    @Override
                    public void onResponse(String s) {
                        progressDialog.dismiss();

                        System.out.println("Retrieve########################## " + s);

                            try {
                                JSONObject jsonObject = new JSONObject(s);
                                JSONArray array = jsonObject.getJSONArray("payments");

                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject row = array.getJSONObject(i);


                                    String id = row.getString("id");
                                    String number_plate = row.getString("number_plate");
                                    String rate = row.getString("rate");
                                    String amount = row.getString("amount");
                                    String passenger_no = row.getString("passenger_no");
                                    String destination = row.getString("destination");
                                    String origin = row.getString("origin");
                                    paymentdetails.add(new Paymentdetails(id, number_plate, rate, amount, passenger_no, destination, origin));

                                }

                                    initializeData();
                                findViewById(R.id.no_internet).setVisibility(View.GONE);
                                findViewById(R.id.progressBar).setVisibility(View.GONE);
                                findViewById(R.id.empty_view).setVisibility(View.GONE);
                                findViewById(R.id.recyclerview).setVisibility(View.VISIBLE);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                    }
                },
                            new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError volleyError) {
                                        System.out.println("volleyError error" + volleyError.getMessage());
                                        Toast.makeText(getApplicationContext(), "Poor network connection.", Toast.LENGTH_LONG).show();
                                    }
                                }) {
                                    @Override
                                    protected Map<String, String> getParams() throws AuthFailureError {
                                        //Creating parameters
                                        Map<String, String> params = new Hashtable<>();

                                        params.put("vehicle_id", Objects.requireNonNull(credentialsSharedPreferences.getString(vehicle_no, "0")));


                                        //returning parameters
                                        return params;
                                    }
                                };
                                RequestQueue requestQueue = Volley.newRequestQueue(Home.this);
                                requestQueue.add(stringRequest);
                            }







    private void initializeData() {
        PaymentAdapter paymentAdapter = new PaymentAdapter(this, paymentdetails);
        recyclerView.setAdapter(paymentAdapter);

    }

    @Override
    public void onBackPressed() {
        @SuppressLint("WrongViewCast") DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (drawer.isDrawerOpen(GravityCompat.END)) {
            drawer.closeDrawer(GravityCompat.END);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_right_menu) {
            if (drawer.isDrawerOpen(GravityCompat.END)) {
                drawer.closeDrawer(GravityCompat.END);
            } else {
                drawer.openDrawer(GravityCompat.END);
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

}