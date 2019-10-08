package com.usalamatechnology.manageapp.ui;

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
    RecyclerView.Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courier);

        courierdetails = new ArrayList <>();
        recyclerView = findViewById(R.id.recyclerview4);
        adapter = new CourierAdapter(courierdetails);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

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
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.getCourier,
                new Response.Listener <String>() {
                    @Override
                    public void onResponse(String s) {
                        System.out.println("123RETRIEEEVE" + s);
//                        textView.setText("retrieve details");
                        Toast.makeText(getApplicationContext(), "successfully retrieved", Toast.LENGTH_SHORT).show();
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            JSONArray array = jsonObject.getJSONArray("courier");

                            for (int i =0; i< array.length(); i++){
                                JSONObject row = array.getJSONObject(i);
                                Courierdetails courierdetail = new Courierdetails(
                                        row.getString("number_plate"),
                                        row.getString("amount"),
                                        row.getString("courier_id")

                                );
                                courierdetails.add(courierdetail);


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
