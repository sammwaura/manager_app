package com.usalamatechnology.manageapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import static com.usalamatechnology.manageapp.Constants.credentialsSharedPreferences;
import static com.usalamatechnology.manageapp.Constants.getAdminArea;
import static com.usalamatechnology.manageapp.Constants.getLocality;
import static com.usalamatechnology.manageapp.Constants.getSubLocality;
import static com.usalamatechnology.manageapp.Constants.latKey;
import static com.usalamatechnology.manageapp.Constants.lonKey;

public class IncomeEdit extends AppCompatActivity {

    EditText date;
    EditText editTextCash;
    EditText other;
    private Spinner spinner;
    TextView_Lato save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.income_edit);

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(IncomeEdit.this, Home.class);
                startActivity(intent);
                finish();
            }
        });

        Bundle extras = getIntent().getExtras();
        final String user_id = extras.getString(Constants.user_id);
        String type = extras.getString(Constants.type);
        String time = extras.getString(Constants.time);
        String address = extras.getString(Constants.address);
        String city = extras.getString(Constants.city);
        String more = extras.getString(Constants.more);
        String amount = extras.getString(Constants.amount);
        String category = extras.getString(Constants.category);

        date = (EditText) findViewById(R.id.editTextDate);
        date.setKeyListener(null);
        editTextCash = (EditText) findViewById(R.id.editTextCash);
        other = (EditText) findViewById(R.id.editTextNotes);

        save = (TextView_Lato)findViewById(R.id.save_earnings);
        spinner = (Spinner) findViewById(R.id.spinner2);
        List<String> dataset = new ArrayList<>();
        dataset.add("Normal Trip");
        dataset.add("Long Trip");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,dataset);

        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);

        spinner.setSelection(arrayAdapter.getPosition(type));

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://zamzam45.com/tally_driver/editIncome.php",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String s) {
                                System.out.println("////////////////123 "+s);
                                Toast.makeText(getApplicationContext(),"Successfully edited Income", Toast.LENGTH_LONG).show();
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
                        Map<String,String> params = new Hashtable<>();

                        params.put("id", user_id);
                        params.put("amount", editTextCash.getText().toString());
                        params.put("type", spinner.getSelectedItem().toString());
                        params.put("other", other.getText().toString());
                        params.put("lat", credentialsSharedPreferences.getString(latKey,"0"));
                        params.put("lon", credentialsSharedPreferences.getString(lonKey,"0"));
                        params.put("address", credentialsSharedPreferences.getString(getSubLocality,"")+","+credentialsSharedPreferences.getString(getLocality,"")+","+credentialsSharedPreferences.getString(getAdminArea,""));
                        return params;
                    }
                };

                //Creating a Request Queue
                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

                //Adding request to the queue
                requestQueue.add(stringRequest);
            }
        });




        long unixSec = Long.parseLong(time);

        Date dateType = new Date(unixSec*1000L);

        SimpleDateFormat sdf = new SimpleDateFormat("Y-M-d HH:mm:ss");

        date.setText(sdf.format(dateType));

        editTextCash.setText(amount);
        other.setText(more);



    }
}
