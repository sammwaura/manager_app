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
import static com.usalamatechnology.manageapp.models.Constants.paymentDetails;
import static com.usalamatechnology.manageapp.models.Constants.vehicle_no;

public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, PaymentObserver{


    private static final String TAG = "Home Activity";

    DrawerLayout drawer;

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    PaymentAdapter paymentAdapter;
    private ArrayList<Paymentdetails>paymentdetails;

    private ArrayList<PassengerDetails>passengerDetails;
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        credentialsSharedPreferences =getSharedPreferences(CREDENTIALSPREFERENCES, Context.MODE_PRIVATE);
        credentialsEditor = credentialsSharedPreferences.edit();


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

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        NavigationView navigationViewRight = (NavigationView) findViewById(R.id.nav_view_right);
        navigationViewRight.setNavigationItemSelectedListener(this);


        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.user_name);
        navUsername.setText(credentialsSharedPreferences.getString(Constants.name, "User Name"));


        paymentdetails = new ArrayList <>();
        passengerDetails = new ArrayList <>();

        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        adapter = new PaymentAdapter(this, this, paymentdetails);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        retrievePaymentDetails();
    }

    private void retrievePaymentDetails() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Retrieving data....");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, paymentDetails,
                new Response.Listener <String>() {
                    @Override
                    public void onResponse(String s) {
                        progressDialog.dismiss();
                        System.out.println("Retrieve### " + s);
                        Toast.makeText(getApplicationContext(), "Retrieved successfully",
                                Toast.LENGTH_LONG).show();
                            try {
                                JSONObject jsonObj = new JSONObject(s);
                                JSONArray array = jsonObj.getJSONArray("payments");

                                String amount = null;
                                String no_of_passengers = null;
                                    for (int i = 0; i < array.length(); i++) {
                                        JSONObject row = array.getJSONObject(i);
                                        Paymentdetails paymentdetail = new Paymentdetails(
                                         row.getString("number_plate"),
                                         row.getString("rate"),
                                         row.getString("destination")
                                        );
                                        row.getString(amount);
                                        row.getString(no_of_passengers);

                                        paymentdetails.add(paymentdetail);
                                        initializeData();
                                        findViewById(R.id.empty_view).setVisibility(View.GONE);
                                    }
                                    adapter.notifyDataSetChanged();
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
                System.out.println();
                //returning parameters
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(Home.this);
        requestQueue.add(stringRequest);
    }


    private void initializeData() {
        PaymentAdapter paymentAdapter = new PaymentAdapter(this, this, paymentdetails);
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
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        // Handle the camera action
        //Toast.makeText(this,"id"+id,Toast.LENGTH_LONG).show();

        if (id == R.id.nav_home) {
                    Intent it = new Intent(Home.this, Home.class);
                    startActivity(it);

        }
        else if (id == R.id.nav_fare) {

            Intent intent = new Intent(Home.this, Fare.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_courier) {
            Intent intent = new Intent(Home.this, Courier.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_logout) {
            credentialsEditor.putString(Constants.user_id, "0");
            credentialsEditor.apply();

            Intent intent = new Intent(Home.this, PinActivity.class);
            startActivity(intent);

        }
//        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onCardClicked(int pos, String name) {
        Toast.makeText(getApplicationContext(), "Clicked", Toast.LENGTH_LONG).show();

        Intent it = new Intent(Home.this, PassengerActivity.class);
        it.putExtra("passenger_name", passengerDetails.get(pos).passenger_name);
        it.putExtra("phone_no", passengerDetails.get(pos).phone_no);
        startActivity(it);
    }

}