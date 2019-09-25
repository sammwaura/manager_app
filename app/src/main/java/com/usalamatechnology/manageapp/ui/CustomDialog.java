package com.usalamatechnology.manageapp.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.usalamatechnology.manageapp.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.usalamatechnology.manageapp.models.Constants.savePayment;

public class CustomDialog  extends DialogFragment{

    private static final String TAG = "CustomDialog";

    public interface OnInputListener{
         void sendInput(String input);
    }
    private OnInputListener onInputListener;

    //widgets
    private EditText number_plate1;
    private EditText amount1;
    private EditText name_passenger1;
    private EditText phone_passenger1;
    private EditText ID_passenger1;
    private EditText destination1;
    private Button submitDetails;

    private Spinner spinner;




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_make_payment, container, false);
        submitDetails = view.findViewById(R.id.submitDetails);
        number_plate1 = view.findViewById(R.id.number_plate1);
        amount1 = view.findViewById(R.id.amount1);
        name_passenger1 = view.findViewById(R.id.name_passenger1);
        phone_passenger1 = view.findViewById(R.id.phone_passenger1);
        ID_passenger1 = view.findViewById(R.id.ID_passenger1);
        destination1 = view.findViewById(R.id.destination1);

        spinner = view.findViewById(R.id.spinner1);
        List<String> dataset = new ArrayList <>();
        dataset.add("Fare");
        dataset.add("Courier");
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.payment, R.layout.support_simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView <?> parent, View view, int position, long id) {
                String selectedCategory = spinner.getSelectedItem().toString();

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

    private void savePayment() {
       final String number_plate = number_plate1.getText().toString().trim();
        final String amount = amount1.getText().toString().trim();
        final String name_of_passenger = name_passenger1.getText().toString().trim();
        final String phone_no_of_passenger = phone_passenger1.getText().toString().trim();
        final String id_no_of_passenger = ID_passenger1.getText().toString().trim();
        final String destination = destination1.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, savePayment,
                new Response.Listener <String>() {
                    @Override
                    public void onResponse(String s) {
                        System.out.println("###SAVED! " + s);
                        Toast.makeText(getContext(), "Saved", Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(getContext(), Home.class);
                        startActivity(intent);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("volleyError response " + error.getMessage());
                Toast.makeText(getContext(), "Poor network connection.", Toast.LENGTH_LONG).show();
            }

        }) {
            @Override
            protected Map<String, String> getParams() {
                Map <String, String> params = new HashMap<>();
                params.put("number_plate", number_plate);
                params.put("spinner", spinner.getSelectedItem().toString());
                params.put("amount",amount);
                params.put("name_of_passenger",name_of_passenger);
                params.put("phone_no_of_passenger",phone_no_of_passenger);
                params.put("id_no_of_passenger",id_no_of_passenger);
                params.put("destination",destination);


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
