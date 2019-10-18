package com.usalamatechnology.manageapp.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

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


public class Courier extends AppCompatActivity {

    private ArrayList<Courierdetails>courierdetails;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courier);

        courierdetails = new ArrayList <>();

        recyclerView =  findViewById(R.id.recyclerview4);
        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(llm);
        recyclerView.setHasFixedSize(true);

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Courier.this, Home.class);
                startActivity(intent);
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

                            for (int i =0; i< array.length(); i++){
                                JSONObject row = array.getJSONObject(i);

                                String courier_id = row.getString("courier_id");
                                String number_plate = row.getString("number_plate");
                                String amount = row.getString("amount");

                                courierdetails.add(new Courierdetails(courier_id, number_plate, amount));
                            }

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
}
