package com.usalamatechnology.manageapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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



import static com.usalamatechnology.manageapp.Constants.passengerDetails;

public class PassengerActivity extends AppCompatActivity  {

    private ArrayList<PassengerDetails>passengerDetails ;


    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;

    String passenger_name;
    int phone_no;
    int seat_no;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger);

        passengerDetails = new ArrayList <>();

        recyclerView.findViewById(R.id.recyclerview2);
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
                        System.out.println("123RETRIEEEVE" + s);
//                        textView.setText("retrieve details");
                        Toast.makeText(getApplicationContext(), "successfully retrieved", Toast.LENGTH_SHORT).show();
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            JSONArray array = jsonObject.getJSONArray("passengers");

                            for (int i =0; i< array.length(); i++){
                                JSONObject row = array.getJSONObject(i);
                                PassengerDetails passengerDetail = new PassengerDetails(
                                        row.getString("passenger_name"),
                                        row.getInt("phone_no"),
                                        row.getInt("seat_no")

                                );
                                passengerDetails.add(passengerDetail);


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

}
