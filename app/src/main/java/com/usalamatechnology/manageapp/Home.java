package com.usalamatechnology.manageapp;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.usalamatechnology.manageapp.Constants.CREDENTIALSPREFERENCES;
import static com.usalamatechnology.manageapp.Constants.credentialsEditor;
import static com.usalamatechnology.manageapp.Constants.credentialsSharedPreferences;
import static com.usalamatechnology.manageapp.Constants.paymentDetails;

public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, MyInterface {


    private static final String TAG = "Home Activity";
    DrawerLayout drawer;

    //widgets
    private EditText number_plate1;
    private EditText amount1;
    private EditText name_passenger1;
    private EditText phone_passenger1;
    private EditText ID_passenger1;
    private EditText destination1;


    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    private ArrayList<Paymentdetails>paymentdetails;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        credentialsSharedPreferences =getSharedPreferences(CREDENTIALSPREFERENCES, Context.MODE_PRIVATE);
        credentialsEditor = credentialsSharedPreferences.edit();


        findViewById(R.id.makePayment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: opening dialog");
                CustomDialog dialog = new CustomDialog();
                dialog.show(getSupportFragmentManager(), "Custom Dialog");


            }
        });



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        NavigationView navigationViewRight = (NavigationView) findViewById(R.id.nav_view_right);
        navigationViewRight.setNavigationItemSelectedListener(this);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.user_name);
        navUsername.setText(credentialsSharedPreferences.getString(Constants.name, "User Name"));


        paymentdetails = new ArrayList <>();

        recyclerView = findViewById(R.id.recyclerview);
        adapter = new PaymentAdapter(paymentdetails);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));



        retrievePaymentDetails();

    }

    private void retrievePaymentDetails() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, paymentDetails,
                new Response.Listener <String>() {
                    @Override
                    public void onResponse(String s) {
                        System.out.println("123RETRIEEEVE" + s);
//                        textView.setText("retrieve details");
                        Toast.makeText(getApplicationContext(), "successfully retrieved", Toast.LENGTH_SHORT).show();
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            JSONArray array = jsonObject.getJSONArray("payments");

                            for (int i =0; i< array.length(); i++){
                                JSONObject row = array.getJSONObject(i);
                                Paymentdetails paymentdetail = new Paymentdetails(
                                        row.getString("number_plate"),
                                        row.getInt("amount"),
                                        row.getString("no_of_passengers"),
                                        row.getInt("rate"),
                                        row.getString("destination")
                                );
                                paymentdetails.add(paymentdetail);
                                initializeData();

                            }

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
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    private void initializeData() {
        PaymentAdapter paymentAdapter = new PaymentAdapter(paymentdetails);
        recyclerView.setAdapter(paymentAdapter);
        paymentAdapter.setListener((PassengerDetailsIObserver) this);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public DrawerLayout getDrawer() {
        return drawer;
    }

}