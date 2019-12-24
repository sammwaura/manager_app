package com.usalamatechnology.manageapp.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.usalamatechnology.manageapp.models.Constants;
import com.usalamatechnology.manageapp.adapter.CourierAdapter;
import com.usalamatechnology.manageapp.models.Courierdetails;
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


public class Courier extends AppCompatActivity {

    private ArrayList <Courierdetails> courierdetails;
    RecyclerView recyclerView;
    TextView total_courier;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courier);

        courierdetails = new ArrayList <>();

        recyclerView = findViewById(R.id.recyclerview4);
        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(llm);
        recyclerView.setHasFixedSize(true);


        total_courier = findViewById(R.id.total_courier);

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Courier.this, Home.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.fab_printC).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                printAllCourier();
            }
        });


        getAllCourier();
    }

    private void getAllCourier() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Retrieving data....");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                "https://zamzam45.com/tally_driver_copy/get_courier.php",
                new Response.Listener <String>() {
                    @Override
                    public void onResponse(String s) {
                        System.out.println("123RETRIEEEVE$$$$$$$$$$$$$$$$$$$$$$$$$$$$" + s);

                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            JSONArray array = jsonObject.getJSONArray("courier");

                            for (int i = 0; i < array.length(); i++) {
                                JSONObject row = array.getJSONObject(i);

                                String courier_id = row.getString("courier_id");
                                String number_plate = row.getString("number_plate");
                                String amount = row.getString("amount");

                                courierdetails.add(new Courierdetails(courier_id, number_plate, amount));
                            }

                            total_courier.setText(String.valueOf(array.length()));

                            initializeData();
                            findViewById(R.id.no_internet).setVisibility(View.GONE);
                            findViewById(R.id.progressBar).setVisibility(View.GONE);
                            findViewById(R.id.empty_view).setVisibility(View.GONE);
                            findViewById(R.id.recyclerview4).setVisibility(View.VISIBLE);


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
        CourierAdapter courierAdapter = new CourierAdapter(this, courierdetails);
        recyclerView.setAdapter(courierAdapter);
    }

    private void printAllCourier() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Retrieving data....");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                "https://zamzam45.com/tally_driver_copy/print_courier.php",
                new Response.Listener <String>() {
                    @Override
                    public void onResponse(String s) {
                        progressDialog.dismiss();

                        System.out.println("Check your email" + s);
                        Toast.makeText(getApplicationContext(), "Check your email within 5 minutes.", Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                        System.out.println("volleyError response " + volleyError.getMessage());
                        Toast.makeText(getApplicationContext(), "Poor network connection.", Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map <String, String> getParams() throws AuthFailureError {
                //Creating parameters
                Map <String, String> params = new Hashtable <>();
                params.put("vehicle_id", Objects.requireNonNull(credentialsSharedPreferences.getString(vehicle_no, "0")));
                params.put("email", Objects.requireNonNull(credentialsSharedPreferences.getString(email, "0")));


                System.out.println();
                //returning parameters
                return params;
            }
        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(Courier.this);

        //Adding request to the queue
        requestQueue.add(stringRequest);

    }

}
