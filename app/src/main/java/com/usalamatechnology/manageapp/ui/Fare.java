package com.usalamatechnology.manageapp.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.usalamatechnology.manageapp.adapter.PaymentAdapter;
import com.usalamatechnology.manageapp.models.Constants;
import com.usalamatechnology.manageapp.adapter.FareAdapter;
import com.usalamatechnology.manageapp.models.Faredetails;
import com.usalamatechnology.manageapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;
import java.util.Objects;

import static com.usalamatechnology.manageapp.models.Constants.credentialsSharedPreferences;
import static com.usalamatechnology.manageapp.models.Constants.email;
import static com.usalamatechnology.manageapp.models.Constants.vehicle_no;


public class Fare extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    private ArrayList<Faredetails>faredetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fare);

        faredetails = new ArrayList <>();

        recyclerView =  findViewById(R.id.recyclerview3);
        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(llm);
        recyclerView.setHasFixedSize(true);


        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Fare.this, Home.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.fab_print).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                printAllFare();
            }
        });

        getAllFare();

    }

    private void getAllFare() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Retrieving data....");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                "https://zamzam45.com/tally_driver_copy/get_fare.php",
                new Response.Listener <String>() {
                    @Override
                    public void onResponse(String s) {
                        progressDialog.dismiss();
                        System.out.println("123RETRIEEEVE#######################################3" + s);

                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            JSONArray array = jsonObject.getJSONArray("fare");

                            for (int i =0; i< array.length(); i++){
                                JSONObject row = array.getJSONObject(i);

                                String number_plate = row.getString("number_plate");
                                String amount = row.getString("amount");
                                String passenger_no = row.getString("passenger_no");

                                faredetails.add(new Faredetails(number_plate, amount, passenger_no));
                            }

                            initializeData();
                            findViewById(R.id.no_internet).setVisibility(View.GONE);
                            findViewById(R.id.progressBar).setVisibility(View.GONE);
                            findViewById(R.id.empty_view).setVisibility(View.GONE);
                            findViewById(R.id.recyclerview3).setVisibility(View.VISIBLE);

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
        FareAdapter fareAdapter = new FareAdapter(this, faredetails);
        recyclerView.setAdapter(fareAdapter);
    }

    private void printAllFare() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Retrieving data....");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                "https://zamzam45.com/tally_driver_copy/print_fare.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        progressDialog.dismiss();

                        System.out.println("Check your email" +s);
                        Toast.makeText(getApplicationContext(), "Check your email within 5 minutes.", Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                        System.out.println("volleyError response " + volleyError.getMessage());
                        Toast.makeText(getApplicationContext(), "Poor network connection.", Toast.LENGTH_LONG).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Creating parameters
                Map<String, String> params = new Hashtable<>();
                params.put("vehicle_id", Objects.requireNonNull(credentialsSharedPreferences.getString(vehicle_no, "0")));
                params.put("email", Objects.requireNonNull(credentialsSharedPreferences.getString(email, "0")));


                System.out.println();
                //returning parameters
                return params;
            }
        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(Fare.this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }



}
