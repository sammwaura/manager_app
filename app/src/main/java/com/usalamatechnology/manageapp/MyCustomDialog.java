package com.usalamatechnology.manageapp;

import android.annotation.SuppressLint;
import android.content.Context;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.alexzh.circleimageview.ItemSelectedListener;

import java.util.ArrayList;

@SuppressLint("ValidFragment")
class MyCustomDialogFragment extends DialogFragment implements TextView.OnEditorActionListener, AdapterView.OnItemSelectedListener {

    public static final String TAG = "MyCustomDialog";

    //widgets
    private EditText number_plate;
    private EditText amount;
    private EditText name_passenger;
    private EditText ID_passenger;
    private EditText destination;
    private Button makePayment;
    private int actionId;
    Spinner spinner;

    public MyCustomDialogFragment(){

    }

    public static MyCustomDialogFragment newInstance(String title) {
        MyCustomDialogFragment myCustomDialogFragment = new MyCustomDialogFragment();
        Bundle args = new Bundle();
        args.putString("Make payment", title);
        myCustomDialogFragment.setArguments(args);
        return myCustomDialogFragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.dialog_make_payment, container);
    }


    @Override
    public void onItemSelected(AdapterView <?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView <?> adapterView) {

    }


    //listener
    public interface CustomDialogueListener{
        void onFinishCustomDialogue(String inputText);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        number_plate = (EditText) view.findViewById(R.id.number_plate);
        String title = getArguments().getString("title" ,"Enter Number plate");
        getDialog().setTitle(title);
        number_plate.requestFocus();
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        spinner = (Spinner) view.findViewById(R.id.spinner1);
        spinner.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.payment, R.layout.support_simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        if (EditorInfo.IME_ACTION_DONE == actionId){
            CustomDialogueListener listener = (CustomDialogueListener) getActivity();
            listener.onFinishCustomDialogue(number_plate.getText().toString());

            dismiss();
            return true;
        }

        return false;
    }


}
