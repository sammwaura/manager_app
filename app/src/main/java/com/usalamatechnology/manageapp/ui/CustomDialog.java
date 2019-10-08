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
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.usalamatechnology.manageapp.R.layout.support_simple_spinner_dropdown_item;
import static com.usalamatechnology.manageapp.models.Constants.credentialsSharedPreferences;
import static com.usalamatechnology.manageapp.models.Constants.paymentDetails;
import static com.usalamatechnology.manageapp.models.Constants.savePayment;
import static com.usalamatechnology.manageapp.models.Constants.uploadCourier;
import static com.usalamatechnology.manageapp.models.Constants.uploadFare;
import static com.usalamatechnology.manageapp.models.Constants.vehicle_no;

public class CustomDialog  extends DialogFragment{

    private static final String TAG = "CustomDialog";

    public interface OnInputListener{
         void sendInput(String input);
    }
    private OnInputListener onInputListener;

    //widgets
    private EditText number_plate1;
    private EditText rate1;
    private EditText name_passenger1;
    private EditText phone_passenger1;
    private EditText ID_passenger1;
    private EditText destination1;
    private Button submitDetails;

    private Spinner spinner;
    String type = null;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_make_payment, container, false);
        submitDetails = view.findViewById(R.id.submitDetails);
        number_plate1 = view.findViewById(R.id.number_plate1);
        rate1 = view.findViewById(R.id.rate1);
        name_passenger1 = view.findViewById(R.id.name_passenger1);
        phone_passenger1 = view.findViewById(R.id.phone_passenger1);
        ID_passenger1 = view.findViewById(R.id.ID_passenger1);
        destination1 = view.findViewById(R.id.destination1);


        spinner = (Spinner) view.findViewById(R.id.spinner1);
        final List<String> dataset = new ArrayList<>();
        dataset.add("Fare");
        dataset.add("Courier");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item, dataset);

        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);

        if(type != null)
        {
            spinner.setVisibility(View.GONE);
        }
        else
        {
            spinner.setVisibility(View.VISIBLE);
        }



        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView <?> parent, View view, int position, long id) {
                Toast.makeText(getContext(), dataset.get(position)+ "Selected", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView <?> parent) {

            }
        });

        if(type != null)
        {
            spinner.setVisibility(View.GONE);
        }
        else
        {
            spinner.setVisibility(View.VISIBLE);
        }

//
//        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(final AdapterView <?> parent, View view, final int position, long id) {
//                String selectedCategory = spinner.getSelectedItem().toString();
//                if (selectedCategory.equals("Fare")) {
//                    saveFare();
//                } else {
//                    if (selectedCategory.equals("Courier")) {
//                        saveCourier();
//                    }
//                }
//            }
//            @Override
//            public void onNothingSelected(AdapterView <?> parent) {
//
//            }
//
//    private void saveFare() {
//            StringRequest stringRequest = new StringRequest(Request.Method.POST, uploadFare,
//                    new Response.Listener <String>() {
//                        @Override
//                        public void onResponse(String response) {
//                            System.out.println("###SAVED! " + response);
//                            Toast.makeText(getContext(), "Saved", Toast.LENGTH_LONG).show();
//
//                            Intent intent = new Intent(getContext(), CustomDialog.class);
//                            startActivity(intent);
//                        }
//
//                    }, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                    System.out.println("volleyError response " + error.getMessage());
//                    Toast.makeText(getContext(), "Poor network connection.", Toast.LENGTH_LONG).show();
//                }
//            }){
//                @Override
//                protected Map <String, String> getParams() throws AuthFailureError {
//                    getParams().put("spinner", spinner.toString());
//                    return super.getParams();
//                }
//            };
//
//            RequestQueue requestQueue = Volley.newRequestQueue(getContext());
//            requestQueue.add(stringRequest);
//        }
//
//
//    private void saveCourier() {
//            StringRequest stringRequest = new StringRequest(Request.Method.POST, uploadCourier,
//                    new Response.Listener <String>() {
//                        @Override
//                        public void onResponse(String response) {
//                            System.out.println("###SAVED! " + response);
//                            Toast.makeText(getContext(), "Saved", Toast.LENGTH_LONG).show();
//
//                            Intent intent = new Intent(getContext(), CustomDialog.class);
//                            startActivity(intent);
//                        }
//
//                    }, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                    System.out.println("volleyError response " + error.getMessage());
//                    Toast.makeText(getContext(), "Poor network connection.", Toast.LENGTH_LONG).show();
//                }
//            }) {
//
//                @Override
//                protected Map <String, String> getParams() throws AuthFailureError {
//                    getParams().put("spinner", spinner.toString());
//                    return super.getParams();
//                }
//            };
//        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
//        requestQueue.add(stringRequest);
//        }
//
//    });

        submitDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: capture input");
                savePayment();


            }
        });


        return view;
    }

    private void savePayment() {
       final String number_plate = number_plate1.getText().toString().trim();
        final String rate = rate1.getText().toString().trim();
        final String name = name_passenger1.getText().toString().trim();
        final String phone_no = phone_passenger1.getText().toString().trim();
        final String id_no = ID_passenger1.getText().toString().trim();
        final String destination = destination1.getText().toString().trim();


        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Saving data....");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                savePayment,
                new Response.Listener <String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getContext(), "Successfully Saved.", Toast.LENGTH_LONG).show();

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
            protected Map<String, String> getParams() {
                Map <String, String> params = new HashMap<>();
                params.put("number_plate", number_plate);
                params.put("type", spinner.getSelectedItem().toString());
                params.put("rate",rate);
                params.put("name",name);
                params.put("phone_no",phone_no);
                params.put("id_no",id_no);
                params.put("destination",destination);

                params.put("vehicle_id", Objects.requireNonNull(credentialsSharedPreferences.getString(vehicle_no, "0")));

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
