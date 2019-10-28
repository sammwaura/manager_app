package com.usalamatechnology.manageapp.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.usalamatechnology.manageapp.R;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.usalamatechnology.manageapp.adapter.PaymentAdapter;
import com.usalamatechnology.manageapp.models.Constants;
import com.usalamatechnology.manageapp.models.ExpenseDetails;

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
import static com.usalamatechnology.manageapp.models.Constants.getExpenses;
import static com.usalamatechnology.manageapp.models.Constants.vehicle_no;

public  class ViewExpenses extends AppCompatActivity{

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    ExpensesAdapter expensesAdapter;
    private ArrayList<ExpenseDetails>expenseDetails;
    DrawerLayout drawer;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_expenses);

        credentialsSharedPreferences =getSharedPreferences(CREDENTIALSPREFERENCES, Context.MODE_PRIVATE);
        credentialsEditor = credentialsSharedPreferences.edit();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        expenseDetails = new ArrayList <>();


        recyclerView =  findViewById(R.id.recyclerviewX);
        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(llm);
        recyclerView.setHasFixedSize(true);

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewExpenses.this, Home.class);
                startActivity(intent);
            }
        });

        retrieveExpenses();
    }


    private void retrieveExpenses() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Retrieving data....");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                "https://zamzam45.com/tally_driver_copy/get_expenses.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        progressDialog.dismiss();

                        System.out.println("Retrieve!!!!!!!!!!!!!!!!!!!" + s);

                        try {
                                JSONObject jsonObj = new JSONObject(s);
                                 JSONArray array = jsonObj.getJSONArray("expenses");

                                    for (int i=0; i<array.length(); i++) {
                                        JSONObject row = array.getJSONObject(i);

                                        String id = row.getString("id");
                                        String amount = row.getString("amount");
                                        String time = row.getString("time");
                                        String expense_type = row.getString("expense_type");
                                        String notes = row.getString("notes");

                                        expenseDetails.add(new ExpenseDetails(id, amount, time, expense_type, notes));
                                    }

                                initializeData();
                                findViewById(R.id.no_internet).setVisibility(View.GONE);
                                findViewById(R.id.progressBar).setVisibility(View.GONE);
                                findViewById(R.id.empty_view).setVisibility(View.GONE);
                                findViewById(R.id.recyclerviewX).setVisibility(View.VISIBLE);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        System.out.println("volleyError response " + volleyError.getMessage());
                        Toast.makeText(getApplicationContext(), "Poor data connection", Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //creating parameters
                Map<String, String> params = new Hashtable<>();

                params.put("vehicle_id", Objects.requireNonNull(credentialsSharedPreferences.getString(vehicle_no, "0")));

                //returning parameters
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        //Adding request to the queue
        requestQueue.add(stringRequest);

    }

    private void initializeData() {
        ExpensesAdapter adapter = new ExpensesAdapter(this, expenseDetails);
        recyclerView.setAdapter(adapter);
    }

}
