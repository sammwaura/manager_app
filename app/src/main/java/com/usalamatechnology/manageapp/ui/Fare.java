package com.usalamatechnology.manageapp.ui;

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
import com.usalamatechnology.manageapp.models.Constants;
import com.usalamatechnology.manageapp.adapter.FareAdapter;
import com.usalamatechnology.manageapp.models.Faredetails;
import com.usalamatechnology.manageapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class Fare extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    private ArrayList<Faredetails>faredetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fare);

        faredetails = new ArrayList <>();

        recyclerView.findViewById(R.id.recyclerview3);
        adapter = new FareAdapter(faredetails);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        getAllFare();

    }

    private void getAllFare() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.getFare,
                new Response.Listener <String>() {
                    @Override
                    public void onResponse(String s) {
                        System.out.println("123RETRIEEEVE" + s);
//                        textView.setText("retrieve details");
                        Toast.makeText(getApplicationContext(), "successfully retrieved", Toast.LENGTH_SHORT).show();
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            JSONArray array = jsonObject.getJSONArray("fare");

                            for (int i =0; i< array.length(); i++){
                                JSONObject row = array.getJSONObject(i);
                                Faredetails faredetail = new Faredetails(
                                        row.getString("number_plate")


                                );
                                faredetails.add(faredetail);


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
