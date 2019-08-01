package com.usalamatechnology.manageapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
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
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import static com.usalamatechnology.manageapp.Constants.credentialsSharedPreferences;
import static com.usalamatechnology.manageapp.Constants.getAllVehicles;
import static com.usalamatechnology.manageapp.Constants.getVehiclesCount;
import static com.usalamatechnology.manageapp.Constants.roles_id;


public class ExpensesVehicle extends AppCompatActivity implements HomeIObserver {

    public static final String ARG_PAGE = "ARG_PAGE";
    private int mPageNo;
    public List<HomePost> homePosts;
    private RecyclerView rv;

    private NiftyDialogBuilder dialogBuilder;
    ImageView navdrawer;

    VehiclesRVAdapter adapter;
    private boolean results;
    private TextView_Lato trips_today,total_profits;
    GetHeader getHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.expenses_vehicle);
        homePosts = new ArrayList<>();

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ExpensesVehicle.this, Home.class);
                startActivity(intent);
            }
        });

        if(credentialsSharedPreferences.getString(roles_id, "0").equals("5"))
        {

        }
        else
        {

            dialogBuilder = NiftyDialogBuilder.getInstance(ExpensesVehicle.this);
            dialogBuilder
                    .withTitle("No Permission")
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
                    .withMessage("Your account has no permission to view expenses")
                    .isCancelableOnTouchOutside(true)
                    .setButton2Click(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(ExpensesVehicle.this, Home.class);
                            startActivity(intent);
                        }
                    })
                    .setButton1Click(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //  Toast.makeText(v.getContext(), "Okay", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(ExpensesVehicle.this, Home.class);
                            startActivity(intent);

                        }
                    })
                    .show();
            this.finish();
            Toast.makeText(getApplicationContext(),"No Permission to view expenses", Toast.LENGTH_LONG).show();
        }

        trips_today = (TextView_Lato) findViewById(R.id.total_trips);
        total_profits = (TextView_Lato) findViewById(R.id.total_earned);

        rv = (RecyclerView) findViewById(R.id.rv);
        TextView emptyView = (TextView) findViewById(R.id.empty_view);

        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());

        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);

        if (CheckNetwork.isInternetAvailable(ExpensesVehicle.this)) {
            getAllPostFromOnline();
            getHeader = new GetHeader();
            getHeader.execute("");
        }

    }

    private void getAllPostFromOnline() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, getAllVehicles,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if(s!=null)
                        {
                            JSONArray array = null;
                            try {
                                JSONObject jsonObj = new JSONObject(s);

                                JSONArray posts = jsonObj.getJSONArray("vehicle_details");

                                for (int i = 0; i < posts.length(); i++) {
                                    JSONObject row = posts.getJSONObject(i);

                                    String id = row.getString("vehicle_id");
                                    String amount = row.getString("registration_no");
                                    String time = row.getString("registration_no");
                                    String street = row.getString("registration_no");
                                    String city = row.getString("registration_no");
                                    String type = row.getString("registration_no");
                                    String more = row.getString("registration_no");
                                    String category = "vehicles";
                                    String address = row.getString("registration_no");

                                    homePosts.add(new HomePost(id,amount,time,city,street,type,more,category,address));


                                }

                                initializeData();
                                //refreshFragment();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                        System.out.println("MAIN response " + s);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Dismissing the progress dialog
                        //loading.dismiss();
                        System.out.println("volleyError response " + volleyError.getMessage());
                        //Showing toast
                        Toast.makeText(getApplicationContext(), "Error. Try Reopening App", Toast.LENGTH_LONG).show();

                        dialogBuilder = NiftyDialogBuilder.getInstance(ExpensesVehicle.this);
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
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new Hashtable<>();

                params.put("user_id","1");
                params.put("roles_id","2");
                //returning parameters
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        //Adding request to the queue
        requestQueue.add(stringRequest);
    }

    private void initializeData() {

        adapter = new VehiclesRVAdapter(homePosts);
        rv.setAdapter(adapter);
        adapter.setListener(this);

    }

    @Override
    public void onCardClicked(int pos, String userid, String type, String time, String address, String city, String more, String amount, String category) {
        amount= homePosts.get(pos).amount;
        Intent intent = new Intent(ExpensesVehicle.this, ExpenseManager.class);
        intent.putExtra("VEHICLE_SELECTED", amount);
        startActivity(intent);
    }

    @Override
    public void onCardApproved(int pos, String userid, String type, String time, String address, String city, String more, String amount, String category) {

    }

    @Override
    public void onEditRate(int pos, String userid, String type, String time, String address, String city, String more, String amount, String category) {

    }

    private class GetHeader extends AsyncTask<String, Integer, Boolean> {
        String data = null;




        @Override
        protected Boolean doInBackground(String... views) {
            return getHeaderDetails();
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onPostExecute(Boolean result) {

        }

    }

    private boolean getHeaderDetails() {
        results = false;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, getVehiclesCount,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (s != null) {
                            JSONArray array = null;
                            try {
                                JSONObject jsonObj = new JSONObject(s);
                                JSONArray posts = jsonObj.getJSONArray("vehicle_details");
                                String totalTrips="";
                                String totalProfit="";
                                String totalIncome="";
                                String totalExpense="";
                                String allIncome="";
                                String allProfit="";
                                String allVehicle="";
                                String allExpense="";

                                for (int i = 0; i < 1; i++)
                                {
                                    JSONObject row = posts.getJSONObject(i);
                                    allVehicle = row.getString("vehicle_count");
                                }
                                trips_today.setText(allVehicle);
                                String sv="";

                                try {
                                    s = String.format("%,d", Long.parseLong(totalExpense));
                                } catch (NumberFormatException e) {
                                }
                                //initializeData();
                                //refreshFragment();
                                results = true;
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(ExpensesVehicle.this, "Error Fetching Data", Toast.LENGTH_LONG).show();
                                results = false;
                            }
                        }
                        System.out.println("MAIN response " + s);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Dismissing the progress dialog
                        //loading.dismiss();
                        System.out.println("volleyError response " + volleyError.getMessage());
                        //Showing toast
                        Toast.makeText(getApplicationContext(), "Error. Try Reopening App", Toast.LENGTH_LONG).show();

                        dialogBuilder = NiftyDialogBuilder.getInstance(ExpensesVehicle.this);
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
                        results = false;
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new Hashtable<>();

                params.put("user_id", "1");
                params.put("roles_id", "2");
                //returning parameters
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(ExpensesVehicle.this);
        //Adding request to the queue
        requestQueue.add(stringRequest);
        return results;
    }


}
