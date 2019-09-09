package com.usalamatechnology.manageapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
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
class MyCustomDialogFragment extends DialogFragment implements TextView.OnEditorActionListener, AdapterView.OnItemSelectedListener {

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

        number_plate = view.findViewById(R.id.number_plate1);
        amount = view.findViewById(R.id.amount1);
        name_of_passenger = view.findViewById(R.id.name_passenger1);
        phone_no_of_passenger = view.findViewById(R.id.phone_passenger1);
        id_no_of_passenger = view.findViewById(R.id.ID_passenger1);
        destination = view.findViewById(R.id.destination1);

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
                        Intent intent = FirstFragment.newIntent(s);
                        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
                        number_plate.setText("");
                        amount.setText("");
                        name_of_passenger.setText("");
                        phone_no_of_passenger.setText("");
                        id_no_of_passenger.setText("");
                        destination.setText("");
                        spinner.setSelection(0);

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
    public void onItemSelected(AdapterView <?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView <?> adapterView) {

    }

    public void show(FragmentManager fragmentManager, String tag) {
    }


    //listener
    public interface CustomDialogueListener{
        void onFinishCustomDialogue(String inputText);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String title = getArguments().getString("title" ,"make payments");
        getDialog().setTitle(title);
//        number_plate.requestFocus();
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

    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        if (EditorInfo.IME_ACTION_DONE == actionId){
            CustomDialogueListener listener = (CustomDialogueListener) getActivity();
//            listener.onFinishCustomDialogue(number_plate.getText().toString());

            dismiss();
            return true;
        }

        return false;
    }


}
