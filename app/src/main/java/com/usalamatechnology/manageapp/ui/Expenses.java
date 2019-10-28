package com.usalamatechnology.manageapp.ui;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.usalamatechnology.manageapp.R;
import com.usalamatechnology.manageapp.TextView_Lato;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;


import static com.usalamatechnology.manageapp.models.Constants.CREDENTIALSPREFERENCES;

public class Expenses extends AppCompatActivity {

  private TextView time, amount, notes, expense_type ;
    TextView_Lato save;
    private LinearLayout layout;
    private SharedPreferences credentialsSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        credentialsSharedPreferences = getSharedPreferences(CREDENTIALSPREFERENCES, Context.MODE_PRIVATE);

        layout = (LinearLayout)findViewById(R.id.layout_expense);
        save = (TextView_Lato)findViewById(R.id.save_expense);
        time = findViewById(R.id.dateTime);
        expense_type = findViewById(R.id.editExpenseType);
        notes = findViewById(R.id.editTextNotes);

        time.setKeyListener(null);


        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Expenses.this, Home.class);
                startActivity(intent);
            }
        });

        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("Y-M-d HH:mm:ss");

        time.setText(sdf.format(c.getTime()));

        amount = findViewById(R.id.editTextCash);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            saveExpense();
            }
        });
    }


    private void saveExpense() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Saving expense....");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                "https://zamzam45.com/tally_driver_copy/save_expenses.php",
                new Response.Listener <String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        System.out.println("SAAAVVEEEE&&&&&&&&&&&&&&&&&&&" + response);

                        amount.setText("");
                        time.setText("");
                        notes.setText("");
                        expense_type.setText("");

                        Intent it = new Intent(getApplicationContext(), ViewExpenses.class);
                        startActivity(it);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("volleyError response " + error.getMessage());
                        Toast.makeText(getApplicationContext(), "Poor network connection.", Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> params = new Hashtable<>();
                //Creating parameters
                params.put("time", time.getText().toString());
                params.put("amount", amount.getText().toString());
                params.put("expense_type", expense_type.getText().toString());
                params.put("notes", notes.getText().toString());

                //returning params
                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(Expenses.this);
        requestQueue.add(stringRequest);

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
