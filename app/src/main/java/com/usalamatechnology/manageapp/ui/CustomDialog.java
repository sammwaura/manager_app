package com.usalamatechnology.manageapp.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.service.autofill.Dataset;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
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
import com.usalamatechnology.manageapp.models.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.usalamatechnology.manageapp.R.layout.support_simple_spinner_dropdown_item;
import static com.usalamatechnology.manageapp.models.Constants.credentialsSharedPreferences;
import static com.usalamatechnology.manageapp.models.Constants.paymentDetails;
import static com.usalamatechnology.manageapp.models.Constants.savePayment;

import static com.usalamatechnology.manageapp.models.Constants.vehicle_no;
import static com.usalamatechnology.manageapp.models.Constants.recordFare;
import static com.usalamatechnology.manageapp.models.Constants.recordCourier;



public class CustomDialog  extends DialogFragment{

    private static final String TAG = "CustomDialog";

    public interface OnInputListener{
         void sendInput(String input);
    }
    private OnInputListener onInputListener;

    //widgets
    private TextView number_plate, rate, name_passenger, phone_passenger, ID_passenger, destination;
    Button submitDetails;
    private Spinner spinner;
    List<String>dataset;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_make_payment, container, false);
        submitDetails = view.findViewById(R.id.submitDetails);
        number_plate= view.findViewById(R.id.number_plate1);
        rate = view.findViewById(R.id.rate1);
        name_passenger = view.findViewById(R.id.name_passenger1);
        phone_passenger = view.findViewById(R.id.phone_passenger1);
        ID_passenger = view.findViewById(R.id.ID_passenger1);
        destination = view.findViewById(R.id.destination1);


        spinner = view.findViewById(R.id.spinner1);
        dataset = new ArrayList <>();
        dataset.add("Fare");
        dataset.add("Courier");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item, dataset);

        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView <?> parent, View view, int position, long id) {
                String selectedCategory = spinner.getSelectedItem().toString();
                if (!selectedCategory.equals("Fare")) {
                    recordIdFare();
                } else if (selectedCategory.equals("Courier")) {
                    recordIdCourier();
                }
            }

            @Override
            public void onNothingSelected(AdapterView <?> parent) {

            }
        });

        submitDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: capture input");
                savePayment();
            }
        });

        return view;
    }

    private void recordIdCourier() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                recordCourier,
                new Response.Listener <String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getContext(), "Successfully recorded.", Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(getContext(), Courier.class);
                        startActivity(intent);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                System.out.println("volleyError"+ error.getMessage());
                Toast.makeText(getContext(), "Poor network connection", Toast.LENGTH_LONG).show();
            }

        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                //Creating parameters
                Map<String, String> params = new Hashtable<>();
                    if (spinner.getSelectedItem().toString().equals("Courier")){
                        params.put("id", "");
                    }


                params.put("vehicle_id", Objects.requireNonNull(credentialsSharedPreferences.getString(vehicle_no, "0")));
                //returning parameters
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);

    }

    private void recordIdFare() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                recordFare,
                new Response.Listener <String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getContext(), "Successfully recorded", Toast.LENGTH_LONG).show();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                System.out.println("volleyError"+ error.getMessage());
                Toast.makeText(getContext(), "Poor network connection", Toast.LENGTH_LONG).show();
            }

        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                //Creating parameters
                Map<String, String> params = new Hashtable<>();
               if (spinner.getSelectedItem().toString().equals("Fare")){
                       params.put("id", "");
                   }

                params.put("vehicle_id", Objects.requireNonNull(credentialsSharedPreferences.getString(vehicle_no, "0")));
                //returning parameters
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);

    }

    private void savePayment() {

        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Saving data....");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                savePayment,
                new Response.Listener <String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("#################222222222" + response);
                        Toast.makeText(getContext(), "Successfully Saved." + response +"Payments(s)", Toast.LENGTH_LONG).show();

                        number_plate.setText("");
                        rate.setText("");
                        name_passenger.setText("");
                        phone_passenger.setText("");
                        ID_passenger.setText("");
                        destination.setText("");

                        Intent intent = new Intent(getContext(), Home.class);
                        startActivity(intent);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                System.out.println("volleyError"+ error.getMessage());
                Toast.makeText(getContext(), "Poor network connection", Toast.LENGTH_LONG).show();
            }

        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                //Creating parameters
                Map<String, String> params = new Hashtable<>();
                params.put("number_plate", number_plate.getText().toString());
                params.put("rate", rate.getText().toString());
                params.put("name", name_passenger.getText().toString());
                params.put("phone_no",phone_passenger.getText().toString());
                params.put("id_no",ID_passenger.getText().toString());
                params.put("destination", destination.getText().toString());

                params.put("vehicle_id", Objects.requireNonNull(credentialsSharedPreferences.getString(vehicle_no, "0")));
                //returning parameters
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {

        } catch (ClassCastException e){
            Log.e(TAG,"onAttach: ClassCastException: " + e.getMessage());

        }
    }
}
