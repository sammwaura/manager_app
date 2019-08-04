package com.usalamatechnology.manageapp;

import android.annotation.SuppressLint;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

@SuppressLint("ValidFragment")
class MyCustomDialogFragment extends DialogFragment implements TextView.OnEditorActionListener {

    public static final String TAG = "MyCustomDialog";

    //widgets
    private EditText number_plate;
    private EditText amount;
    private EditText name_passenger;
    private EditText ID_passenger;
    private EditText destination;
    private Button makePayment;
    private int actionId;

    public MyCustomDialogFragment(){

    }

    public static MyCustomDialogFragment newInstance(String title) {
        MyCustomDialogFragment myCustomDialogFragment = new MyCustomDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        myCustomDialogFragment.setArguments(args);
        return myCustomDialogFragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.dialog_make_payment, container);


    }

    //listener
    public interface CustomDialogueListener{
        void onFinishCustomDilaogue(String inputText);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        number_plate = (EditText) view.findViewById(R.id.number_plate);
        String title = getArguments().getString("title" ,"Enter Number plate");
        getDialog().setTitle(title);
        number_plate.requestFocus();
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }

    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        if (EditorInfo.IME_ACTION_DONE == actionId){
            CustomDialogueListener listener = (CustomDialogueListener) getActivity();
            listener.onFinishCustomDilaogue(number_plate.getText().toString());

            dismiss();
            return true;
        }

        return false;
    }
}
