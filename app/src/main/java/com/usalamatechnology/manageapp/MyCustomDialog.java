package com.usalamatechnology.manageapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.usalamatechnology.manageapp.Constants.savePayment;

@SuppressLint("ValidFragment")
class MyCustomDialogFragment extends DialogFragment implements  AdapterView.OnItemSelectedListener {

    public static final String TAG = "MyCustomDialog";
    List<String> data;

    //widgets
//    private EditText number_plate;
     EditText number_plate;
     EditText amount;
     EditText name_of_passenger;
     EditText phone_no_of_passenger;
     EditText id_no_of_passenger;
     EditText destination;

    private int actionId;
    Spinner spinner;

    public MyCustomDialogFragment(){

    }

    public static MyCustomDialogFragment getInstance() {
        MyCustomDialogFragment myCustomDialogFragment = new MyCustomDialogFragment();
        Bundle args = new Bundle();
//        args.putString("Make payment", title);
        myCustomDialogFragment.setArguments(args);
        return myCustomDialogFragment;


    }


    View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.dialog_make_payment, container, false);



        return view;

    }



    private void savePayment() {

      final String Numberplate = number_plate.getText().toString().trim();
      final String Amount = amount.getText().toString().trim();
      final String Name = name_of_passenger.getText().toString().trim();
      final String PhoneNo = phone_no_of_passenger.getText().toString().trim();
      final String Id = id_no_of_passenger.getText().toString().trim();
      final String Destination = destination.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, savePayment,
                new Response.Listener <String>() {
                    @Override
                    public void onResponse(String s) {
                        System.out.println("###SAVED! " + s);
                        Toast.makeText(getContext(), "Saved", Toast.LENGTH_LONG).show();


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
                Map <String, String> params = new HashMap <>();
                params.put("number_plate", Numberplate);
                params.put("spinner", spinner.getSelectedItem().toString());
                params.put("amount", Amount);
                params.put("name_of_passenger", Name);
                params.put("phone_no_of_passenger", PhoneNo);
                params.put("id_no_of_passenger", Id);
                params.put("destination", Destination);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);

    }

    @Override
    public void onAttach(Context context) {
        try {
            CustomDialogueListener listener = (CustomDialogueListener) getTargetFragment();
        } catch (ClassCastException e){
            Log.e(TAG, "onAttach: ClassCastException: " + e.getMessage() );
        }
        super.onAttach(context);
    }

    /**
     * <p>Callback method to be invoked when an item in this view has been
     * selected. This callback is invoked only when the newly selected
     * position is different from the previously selected position or if
     * there was no selected item.</p>
     * <p>
     * Implementers can call getItemAtPosition(position) if they need to access the
     * data associated with the selected item.
     *
     * @param parent   The AdapterView where the selection happened
     * @param view     The view within the AdapterView that was clicked
     * @param position The position of the view in the adapter
     * @param id       The row id of the item that is selected
     */
    @Override
    public void onItemSelected(AdapterView <?> parent, View view, int position, long id) {

    }

    /**
     * Callback method to be invoked when the selection disappears from this
     * view. The selection can disappear for instance when touch is activated
     * or when the adapter becomes empty.
     *
     * @param parent The AdapterView that now contains no selected item.
     */
    @Override
    public void onNothingSelected(AdapterView <?> parent) {

    }




    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        number_plate = view.findViewById(R.id.number_plate1);
        amount = view.findViewById(R.id.amount1);
        name_of_passenger = view.findViewById(R.id.name_passenger1);
        phone_no_of_passenger = view.findViewById(R.id.phone_passenger1);
        id_no_of_passenger = view.findViewById(R.id.ID_passenger1);
        destination = view.findViewById(R.id.destination1);

        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        spinner = (Spinner) view.findViewById(R.id.spinner1);
        spinner.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.payment, R.layout.support_simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        view.findViewById(R.id.submitDetails).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    savePayment();
            }
        });

    }



}
