package com.usalamatechnology.manageapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import static com.usalamatechnology.manageapp.Constants.CREDENTIALSPREFERENCES;
import static com.usalamatechnology.manageapp.Constants.addressKey;
import static com.usalamatechnology.manageapp.Constants.getAdminArea;
import static com.usalamatechnology.manageapp.Constants.getLocality;
import static com.usalamatechnology.manageapp.Constants.getSubLocality;
import static com.usalamatechnology.manageapp.Constants.latKey;
import static com.usalamatechnology.manageapp.Constants.lonKey;
import static com.usalamatechnology.manageapp.Constants.credentialsSharedPreferences;
import static com.usalamatechnology.manageapp.Constants.credentialsEditor;

public class Earnings extends AppCompatActivity {

    EditText dateTime;
    EditText amount;
    EditText notes;
    Spinner spinner;



    TextView_Lato save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_earning);

        credentialsSharedPreferences = getSharedPreferences(CREDENTIALSPREFERENCES, Context.MODE_PRIVATE);

        save = (TextView_Lato)findViewById(R.id.save_earnings);
        dateTime = (EditText)findViewById(R.id.editTextDate);
        notes = (EditText)findViewById(R.id.editTextNotes);
        dateTime.setKeyListener(null);

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Earnings.this, Home.class);
                startActivity(intent);
                finish();
            }
        });

        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("Y-m-d HH:mm:ss");

        dateTime.setText(sdf.format(c.getTime()));

        amount = (EditText)findViewById(R.id.editTextCash);
        spinner = (Spinner) findViewById(R.id.spinner2);
        List<String> dataset = new ArrayList<>();
        dataset.add("Normal Trip");
        dataset.add("Long Trip");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,dataset);

        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Toast.makeText(getApplicationContext(),"Clicked", Toast.LENGTH_LONG).show();

                StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://zamzam45.com/tally_driver/uploadIncome.php",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String s) {
                               System.out.println("////////////////123 "+s);
                                Toast.makeText(getApplicationContext(),"Successfully saved income", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(getApplicationContext(), Home.class);
                                startActivity(intent);

                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {

                            }
                        }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {

                        //Creating parameters
                        Map<String,String> params = new Hashtable<>();
                        //Adding parameters

                        params.put("amount", amount.getText().toString());
                        params.put("type", spinner.getSelectedItem().toString());
                        params.put("notes", notes.getText().toString());
                        params.put("lat", credentialsSharedPreferences.getString(latKey,"0"));
                        params.put("lon", credentialsSharedPreferences.getString(lonKey,"0"));
                        params.put("address", credentialsSharedPreferences.getString(getSubLocality,"")+","+credentialsSharedPreferences.getString(getLocality,"")+","+credentialsSharedPreferences.getString(getAdminArea,""));

                        //returning parameters
                        return params;
                    }
                };

                //Creating a Request Queue
                RequestQueue requestQueue = Volley.newRequestQueue(Earnings.this);

                //Adding request to the queue
                requestQueue.add(stringRequest);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_tick:
                // User chose the "Settings" item, show the app settings UI...
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
}
