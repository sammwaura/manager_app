package com.usalamatechnology.manageapp.ui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import com.usalamatechnology.manageapp.R;
import com.usalamatechnology.manageapp.adapter.PassengerAdapter;
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

import static com.usalamatechnology.manageapp.models.Constants.credentialsSharedPreferences;
import static com.usalamatechnology.manageapp.models.Constants.vehicle_no;

public class PassengerActivity extends AppCompatActivity  {

    private ArrayList<PassengerDetails>passengerDetails ;


    RecyclerView recyclerView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger);

        passengerDetails = new ArrayList <>();

        recyclerView =  findViewById(R.id.recyclerview2);
        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(llm);
        recyclerView.setHasFixedSize(true);

        retrievePassengerDetails();

    }

    private void retrievePassengerDetails() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Retrieving data....");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                "https://zamzam45.com/tally_driver_copy/get_passenger_details.php",
                new Response.Listener <String>() {
                    @Override
                    public void onResponse(String s) {
                        progressDialog.dismiss();

                        System.out.println("Retrieve########################## " + s);

                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            JSONArray array = jsonObject.getJSONArray("passengers");

                            for (int i = 0; i < array.length(); i++) {
                                JSONObject row = array.getJSONObject(i);



                                String name = row.getString("name");
                                String phone_no = row.getString("phone_no");
                                String seat_no = row.getString("seat_no");

                                passengerDetails.add(new PassengerDetails(name, phone_no, seat_no));

                            }

                            initializeData();
                            findViewById(R.id.no_internet).setVisibility(View.GONE);
                            findViewById(R.id.progressBar).setVisibility(View.GONE);
                            findViewById(R.id.empty_view).setVisibility(View.GONE);
                            findViewById(R.id.recyclerview2).setVisibility(View.VISIBLE);

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
        RequestQueue requestQueue = Volley.newRequestQueue(PassengerActivity.this);
        requestQueue.add(stringRequest);
    }

    private void initializeData() {

        PassengerAdapter passengerAdapter = new PassengerAdapter(this, passengerDetails);
        recyclerView.setAdapter(passengerAdapter);
    }


}
