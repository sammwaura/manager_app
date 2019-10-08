package com.usalamatechnology.manageapp.ui;

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

import static com.usalamatechnology.manageapp.models.Constants.CREDENTIALSPREFERENCES;
import static com.usalamatechnology.manageapp.models.Constants.credentialsEditor;
import static com.usalamatechnology.manageapp.models.Constants.credentialsSharedPreferences;
import static com.usalamatechnology.manageapp.models.Constants.getExpenses;

public  class ViewExpenses extends AppCompatActivity implements  ExpensesObserver{

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

        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        adapter = new ExpensesAdapter(this,this, expenseDetails);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

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
        StringRequest stringRequest = new StringRequest(Request.Method.POST, getExpenses,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        JSONArray posts = null;
                        if (s != null) {
                            JSONArray array = null;
                            try {
                                JSONObject jsonObj = new JSONObject(s);

                                posts = jsonObj.getJSONArray("expenses");

                                if(posts.length() >0)
                                {
                                    for (int i = 0; i < posts.length(); i++) {
                                        JSONObject row = posts.getJSONObject(i);

                                        String id = row.getString("id");
                                        String amount = row.getString("amount");
                                        String time = row.getString("time");
                                        String type = row.getString("type");
                                        String notes = row.getString("notes");
                                        String category = row.getString("category");

                                        expenseDetails.add(new ExpenseDetails(id, amount, time, type, notes, category));
                                    }
                                }
                                initializeData();
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(getApplicationContext(), "Error Fetching Data", Toast.LENGTH_LONG).show();

                            }

                        }
                        System.out.println("MAIN response first " + posts.length());

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Dismissing the progress dialog
                        //loading.dismiss();
                        System.out.println("volleyError response " + volleyError.getMessage());
                        //Showing toast
                        Toast.makeText(getApplicationContext(), "Error Fetching Data", Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new Hashtable<>();

                params.put("user_id", "1");
                params.put("roles_id", "2");
                //returning parameters
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        //Adding request to the queue
        requestQueue.add(stringRequest);

    }

    private void initializeData() {
        ExpensesAdapter adapter = new ExpensesAdapter(this, this, expenseDetails);
        recyclerView.setAdapter(adapter);
        adapter.setListener(this);
    }





    @Override
    public void onCardClicked(int pos, String name) {

    }
}
