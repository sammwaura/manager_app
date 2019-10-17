package com.usalamatechnology.manageapp.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.usalamatechnology.manageapp.R;
import com.usalamatechnology.manageapp.adapter.PassengerAdapter;
import com.usalamatechnology.manageapp.models.Constants;
import com.usalamatechnology.manageapp.models.PassengerDetails;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;
import java.util.Objects;

import static com.usalamatechnology.manageapp.models.Constants.credentialsSharedPreferences;
import static com.usalamatechnology.manageapp.models.Constants.vehicle_no;

public class PassengerActivity extends AppCompatActivity  {

    private ArrayList<PassengerDetails>passengerDetails ;


    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;

    String passenger_name;
    String phone_no;


    public static String PASSENGER_DETAILS = "_details";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger);

        passengerDetails = new ArrayList <>();

        recyclerView = findViewById(R.id.recyclerview2);
        adapter = new PassengerAdapter(passengerDetails);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        retrievePassengerDetails();

    }

    private void retrievePassengerDetails() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.passengerDetails,
                new Response.Listener <String>() {
                    @Override
                    public void onResponse(String s) {
                        if (s != null) {
                            JSONArray array = null;
                            try {
                                JSONObject jsonObj = new JSONObject(s);

                                JSONArray posts = jsonObj.getJSONArray("passengers");
                                passengerDetails.clear();
                                if (posts.length() > 0) {
                                    for (int i = 0; i < posts.length(); i++) {
                                        JSONObject row = posts.getJSONObject(i);

                                        String passenger_name = row.getString("passenger_name");
                                        String phone_no = row.getString("phone_no");
                                        String no_of_passengers = row.getString("no_of_passengers");

                                        passengerDetails.add(new PassengerDetails(passenger_name, phone_no, no_of_passengers));
                                    }
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
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
            return params;
        }
    };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);

    }

}
