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
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.usalamatechnology.manageapp.R;
import com.usalamatechnology.manageapp.TextView_Lato;
import com.usalamatechnology.manageapp.models.Constants;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


import static com.usalamatechnology.manageapp.models.Constants.CREDENTIALSPREFERENCES;


public class Expenses extends AppCompatActivity {



    EditText dateTime;
    EditText amount;
    EditText other;


    Spinner spinner;
    TextView_Lato save;


    private EditText typeText;

    String type = null;
    private LinearLayout layout;
    private SharedPreferences credentialsSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        credentialsSharedPreferences = getSharedPreferences(CREDENTIALSPREFERENCES, Context.MODE_PRIVATE);

        layout = (LinearLayout)findViewById(R.id.layout_other);
        save = (TextView_Lato)findViewById(R.id.save_expense);
        dateTime = (EditText)findViewById(R.id.dateTime);
        other = (EditText)findViewById(R.id.editTextOther);
        typeText = (EditText)findViewById(R.id.editTextType);
        dateTime.setKeyListener(null);
        typeText.setKeyListener(null);

        Bundle extras = getIntent().getExtras();

        if(extras != null)
        {
            type = extras.getString(Constants.type);
            layout.setVisibility(View.GONE);
        }

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Expenses.this, Home.class);
                startActivity(intent);
            }
        });



        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("Y-M-d HH:mm:ss");

        dateTime.setText(sdf.format(c.getTime()));

        amount = (EditText)findViewById(R.id.editTextCash);
        spinner = (Spinner) findViewById(R.id.spinnerType);
        List<String> dataset = new ArrayList<>();
        dataset.add("Fuel");
        dataset.add("Sacco");
        dataset.add("Boys");
        dataset.add("Car Wash");
        dataset.add("Parking");
        dataset.add("Puncture");
        dataset.add("Services/Greasing");
        dataset.add("Battery");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,dataset);

        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);

        if(type != null)
        {
            typeText.setText("Accident");
            typeText.setVisibility(View.VISIBLE);
            spinner.setVisibility(View.GONE);
            layout.setVisibility(View.GONE);
        }
        else
        {
            typeText.setVisibility(View.GONE);
            spinner.setVisibility(View.VISIBLE);
            layout.setVisibility(View.VISIBLE);
        }

        /*Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        myToolbar.setTitle("Expenses");*/

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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
