package com.usalamatechnology.manageapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gitonway.lee.niftymodaldialogeffects.lib.Effectstype;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import static com.usalamatechnology.manageapp.Constants.CREDENTIALSPREFERENCES;
import static com.usalamatechnology.manageapp.Constants.credentialsEditor;
import static com.usalamatechnology.manageapp.Constants.credentialsSharedPreferences;
import static com.usalamatechnology.manageapp.Constants.currentTripId;
import static com.usalamatechnology.manageapp.Constants.getvehicles;
import static com.usalamatechnology.manageapp.Constants.login_driver;
import static com.usalamatechnology.manageapp.Constants.tripStatus;
import static com.usalamatechnology.manageapp.Constants.user_id;


public class PinActivity extends AppCompatActivity {

    EditText pin;
    private ArrayList<String> students;
    private JSONArray result;
    private NiftyDialogBuilder dialogBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin);
        pin = (EditText) findViewById(R.id.editText);

        students = new ArrayList<String>();



        findViewById(R.id.textView5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(PinActivity.this, "Call your administrator", Toast.LENGTH_LONG).show();
            }
        });

        findViewById(R.id.textView7).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pin.getText().toString().length() == 6) {
                    findViewById(R.id.progressBar2).setVisibility(View.VISIBLE);
                    findViewById(R.id.textView4).setVisibility(View.VISIBLE);

                    if(CheckNetwork.isInternetAvailable(PinActivity.this))
                    {
                        validateUser();
                    }


                }
                else
                {
                    Toast.makeText(PinActivity.this, "Please enter 6 digit pin", Toast.LENGTH_LONG).show();
                }
            }
        });




        credentialsSharedPreferences = getSharedPreferences(CREDENTIALSPREFERENCES, Context.MODE_PRIVATE);
        credentialsEditor = credentialsSharedPreferences.edit();

        if (credentialsSharedPreferences.getString(user_id, "0").equals("0")) {
            //fp.setVisibility(View.GONE);
        } else {
            //fp.setVisibility(View.VISIBLE);
        }
        if(!credentialsSharedPreferences.getString(user_id, "0").equals("0"))
        {
            Intent intent = new Intent(PinActivity.this, Home.class);
            startActivity(intent);
        }
        pin.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //here is your code
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                if (s.toString().length() == 6) {
                    findViewById(R.id.textView7).setVisibility(View.VISIBLE);
                    findViewById(R.id.progressBar2).setVisibility(View.VISIBLE);
                    findViewById(R.id.textView4).setVisibility(View.VISIBLE);

                    if(CheckNetwork.isInternetAvailable(PinActivity.this))
                    {
                        validateUser();
                    }

                }
            }
        });


    }


    private void validateUser() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, login_driver,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        findViewById(R.id.progressBar2).setVisibility(View.GONE);
                        findViewById(R.id.textView4).setVisibility(View.GONE);

                        System.out.println("sssssssssssssssssssssssssssssssssssss " + s);

                        //dialogBuilder.dismiss();
                        if (s.equalsIgnoreCase("Already Logged In")) {
                            pin.setText("");
                            Toast.makeText(PinActivity.this, "You are already logged in. Log out first", Toast.LENGTH_LONG).show();
                        }
                        else if (s.equalsIgnoreCase("Failed")) {
                            pin.setText("");
                            Toast.makeText(PinActivity.this, "Wrong pin, try again", Toast.LENGTH_LONG).show();
                        } else {
                            JSONArray posts = null;
                            if (s != null) {
                                JSONArray array = null;
                                try {
                                    JSONObject jsonObj = new JSONObject(s);

                                    posts = jsonObj.getJSONArray("user_details");


                                    if (posts.length() > 0) {
                                        for (int i = 0; i < posts.length(); i++) {
                                            JSONObject row = posts.getJSONObject(i);
                                            String user_id = row.getString("user_id");
                                            String name = row.getString("name");
                                            String phone = row.getString("phone");
                                            String email = row.getString("email");
                                            String roles_id = row.getString("roles_id");

                                            System.out.println("user_id ?????????????????????????? " + roles_id);

                                            credentialsEditor.putString(Constants.user_id, user_id);
                                            credentialsEditor.putString(Constants.name, name);
                                            credentialsEditor.putString(Constants.phone, phone);
                                            credentialsEditor.putString(Constants.email, email);
                                            credentialsEditor.putString(Constants.roles_id, roles_id);
                                            credentialsEditor.apply();


                                            Intent intent = new Intent(PinActivity.this, Home.class);
                                            startActivity(intent);

//                                            Intent intent = new Intent(PinActivity.this, Home.class);
//                                            startActivity(intent);

                                        }
                                    }

                                } catch (JSONException e) {
                                    pin.setText("");
                                    e.printStackTrace();
                                    Toast.makeText(PinActivity.this, "Server did not return any useful data", Toast.LENGTH_LONG).show();

                                }

                            }

                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                        findViewById(R.id.progressBar2).setVisibility(View.GONE);
                        findViewById(R.id.textView4).setVisibility(View.GONE);
                        //Dismissing the progress dialog
                        //loading.dismiss();
                        System.out.println("volleyError response " + volleyError.getMessage());
                        //Showing toast
                        Toast.makeText(getApplicationContext(), "Error. Try Reopening App", Toast.LENGTH_LONG).show();

                        dialogBuilder = NiftyDialogBuilder.getInstance(PinActivity.this);
                        dialogBuilder
                                .withTitle("Re-open App")
                                .withTitleColor("#00B873")                                  //def
                                .withDividerColor("#00B873")                              //def//.withMessage(null)  no Msg
                                .withMessageColor("#00B873")                              //def  | withMessageColor(int resid)
                                .withDialogColor("#FFFFFF")                               //def  | withDialogColor(int resid)
                                .withDuration(700)
                                .isCancelableOnTouchOutside(false)
                                .isCancelable(false)
                                .withEffect(Effectstype.Fadein)
                                .withButton2Text("CANCEL")//def Effectstype.Slidetop
                                .withButton1Text("CONTINUE")
                                .withMessage("The server is not responding. Close the app and try again")
                                .isCancelableOnTouchOutside(true)
                                .setButton2Click(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialogBuilder.dismiss();
                                    }
                                })
                                .setButton1Click(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        //  Toast.makeText(v.getContext(), "Okay", Toast.LENGTH_SHORT).show();

                                        ExitActivity.exitApplication(v.getContext());


                                    }
                                })
                                .show();



                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                //Creating parameters
                Map<String, String> params = new Hashtable<>();

                params.put("pin", pin.getText().toString());


                //returning parameters
                return params;
            }
        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(PinActivity.this);

        //Adding request to the queue
        requestQueue.add(stringRequest);

    }



    @Override
    public void onBackPressed() {
        this.finish();
        System.exit(0);
    }

}
