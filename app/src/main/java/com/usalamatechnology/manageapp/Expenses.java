package com.usalamatechnology.manageapp;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.gitonway.lee.niftymodaldialogeffects.lib.Effectstype;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.usalamatechnology.manageapp.Constants.CREDENTIALSPREFERENCES;
import static com.usalamatechnology.manageapp.Constants.addressKey;
import static com.usalamatechnology.manageapp.Constants.getAdminArea;
import static com.usalamatechnology.manageapp.Constants.getLocality;
import static com.usalamatechnology.manageapp.Constants.getSubLocality;
import static com.usalamatechnology.manageapp.Constants.getvehicles;
import static com.usalamatechnology.manageapp.Constants.latKey;
import static com.usalamatechnology.manageapp.Constants.lonKey;
import static com.usalamatechnology.manageapp.Constants.credentialsSharedPreferences;
import static com.usalamatechnology.manageapp.Constants.credentialsEditor;
import static com.usalamatechnology.manageapp.Constants.roles_id;
import static com.usalamatechnology.manageapp.Constants.uploadExpense;
import static com.usalamatechnology.manageapp.Constants.uploadExpenseImage;


public class Expenses extends AppCompatActivity {

    int TAKE_PHOTO_CODE = 0;
    public static int count = 0;

    EditText dateTime;
    EditText amount;
    EditText other;
    Button camera;


    Spinner spinner;
    TextView_Lato save;


    final String dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/picFolder/";
    public static final int MY_PERMISSIONS_REQUEST_CAMERA = 100;
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
    public static final String ALLOW_KEY = "ALLOWED";
    public static final String CAMERA_PREF = "camera_pref";
    private String TAG="TAG";
    private ImageView mImageView,mImageView2,mImageView3,mImageView4,mImageView5;
    private EditText typeText;
    private NiftyDialogBuilder dialogBuilder;

    String type = null;
    private LinearLayout layout;
    private Textview_lato_thin driver_carc;
    private Textview_lato_thin select_Vehicle;
    private ArrayList<String> students;
    private JSONArray result;
    String vehicle_reg_no;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);



        //CheckNetwork.isInternetAvailable(getApplicationContext());
        students = new ArrayList<String>();

        credentialsSharedPreferences = getSharedPreferences(CREDENTIALSPREFERENCES, Context.MODE_PRIVATE);
        driver_carc = (Textview_lato_thin) findViewById(R.id.driver_car);
        select_Vehicle= (Textview_lato_thin) findViewById(R.id.select_Vehicle);
        //driver_carc.setText(credentialsSharedPreferences.getString(vehicle_reg_no,""));

        select_Vehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(CheckNetwork.isInternetAvailable(Expenses.this))
                {
                    getvehicles();
                }
            }
        });

        layout = (LinearLayout)findViewById(R.id.layout_other);
        save = (TextView_Lato)findViewById(R.id.save_expense);
        dateTime = (EditText)findViewById(R.id.dateTime);
        other = (EditText)findViewById(R.id.editTextOther);
        typeText = (EditText)findViewById(R.id.editTextType);
        camera = (Button) findViewById(R.id.button);
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

        mImageView = (ImageView) findViewById(R.id.imageCam);
        mImageView2= (ImageView) findViewById(R.id.imageCam2);
        mImageView3= (ImageView) findViewById(R.id.imageCam3);
        mImageView4= (ImageView) findViewById(R.id.imageCam4);
        mImageView5= (ImageView) findViewById(R.id.imageCam5);

        // Here, we are making a folder named picFolder to store
        // pics taken by the camera using this application.

        File newdir = new File(dir);
        newdir.mkdirs();


        camera.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(Expenses.this,
                        Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {

                    if (getFromPref(Expenses.this, ALLOW_KEY)) {

                        showSettingsAlert();

                    } else if (ContextCompat.checkSelfPermission(Expenses.this,
                            Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED) {
                        // Should we show an explanation?
                        if (ActivityCompat.shouldShowRequestPermissionRationale(Expenses.this,
                                Manifest.permission.CAMERA)) {
                            showAlert();
                        } else {
                            // No explanation needed, we can request the permission.
                            ActivityCompat.requestPermissions(Expenses.this,
                                    new String[]{Manifest.permission.CAMERA},
                                    MY_PERMISSIONS_REQUEST_CAMERA);
                        }
                    }
                } else {
                    if (checkPermissionREAD_EXTERNAL_STORAGE(Expenses.this)) {
                        openCamera();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"Read permission not granted", Toast.LENGTH_LONG).show();
                    }

                }

            }
        });

        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("Y-M-d HH:mm:ss");

        dateTime.setText(sdf.format(c.getTime()));

        amount = (EditText)findViewById(R.id.editTextCash);
        spinner = (Spinner) findViewById(R.id.spinnerType);
        List<String> dataset = new ArrayList<>();
        dataset.add("Fuel");
        dataset.add("Maintenance");
        dataset.add("Sacco");
        dataset.add("Bank");
        dataset.add("Line");
        dataset.add("Repairs");
        dataset.add("Boys");
        dataset.add("Car Wash");
        dataset.add("Salary");
        dataset.add("Miscellaneous");

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


                if (!amount.getText().toString().equals("")) {
                    if (!select_Vehicle.getText().toString().equals("Tap to select vehicle")) {

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, uploadExpense,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String s) {
                                    String expenseid = s;
                                    //System.out.println("////////////////123 "+s);
                                    Toast.makeText(getApplicationContext(), "Successfully saved expense", Toast.LENGTH_LONG).show();

                                    uploadImage(expenseid);

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

                                    dialogBuilder = NiftyDialogBuilder.getInstance(Expenses.this);
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
                            Map<String, String> params = new Hashtable<>();
                            params.put("amount", amount.getText().toString());
                            if (type != null) {
                                params.put("type", typeText.getText().toString());
                            } else {
                                params.put("type", spinner.getSelectedItem().toString());
                            }

                            params.put("other", other.getText().toString());
                            params.put("lat", credentialsSharedPreferences.getString(latKey, "0"));
                            params.put("lon", credentialsSharedPreferences.getString(lonKey, "0"));
                            params.put("user_id", credentialsSharedPreferences.getString(Constants.user_id, ""));
                            params.put("address", credentialsSharedPreferences.getString(getSubLocality, "") + "," + credentialsSharedPreferences.getString(getLocality, "") + "," + credentialsSharedPreferences.getString(getAdminArea, ""));
                            params.put("vehicle_reg_no", select_Vehicle.getText().toString());

                            return params;
                        }
                    };

                    //Creating a Request Queue
                    RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

                    //Adding request to the queue
                    requestQueue.add(stringRequest);
                }
                    else {
                        Toast.makeText(getApplicationContext(), "Please select a vehicle first.", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Please enter the amount of expense first.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    private void getvehicles() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, getvehicles,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject j = null;
                        try {

                            j = new JSONObject(response);
                            result = j.getJSONArray("result");
                            getVehicles(result);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Showing toast
                        Toast.makeText(getApplicationContext(), "Error. Try Reopening App", Toast.LENGTH_LONG).show();

                        dialogBuilder = NiftyDialogBuilder.getInstance(getApplicationContext());
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
                });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }



    private void getVehicles( final JSONArray j) {
        if(j.length() >0)
        {
            for (int i = 0; i < j.length(); i++) {
                try {
                    JSONObject json = j.getJSONObject(i);
                    students.add(json.getString("vehicle_reg_no"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            AlertDialog.Builder b = new AlertDialog.Builder(Expenses.this);
            b.setTitle("Please Select Your Vehicle");
            final String[] types = students.toArray(new String[0]);
            b.setItems(types, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(Expenses.this, types [which], Toast.LENGTH_SHORT).show();
                     vehicle_reg_no = types[which];
                    select_Vehicle.setText(vehicle_reg_no);
                    dialog.dismiss();
                }


            });

            b.show();
        }
        else
        {
            Toast.makeText(this, "No vehicles available,try later.", Toast.LENGTH_LONG).show();
        }
    }


    public boolean checkPermissionREAD_EXTERNAL_STORAGE(
            final Context context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context,
                    Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        (Activity) context,
                        Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    showDialog("External storage", context,
                            Manifest.permission.READ_EXTERNAL_STORAGE);

                } else {
                    ActivityCompat
                            .requestPermissions(
                                    (Activity) context,
                                    new String[] { Manifest.permission.READ_EXTERNAL_STORAGE },
                                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
                return false;
            } else {
                return true;
            }

        } else {
            return true;
        }
    }

    public void showDialog(final String msg, final Context context,
                           final String permission) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
        alertBuilder.setCancelable(true);
        alertBuilder.setTitle("Permission necessary");
        alertBuilder.setMessage(msg + " permission is necessary");
        alertBuilder.setPositiveButton(android.R.string.yes,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions((Activity) context,
                                new String[] { permission },
                                MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                    }
                });
        AlertDialog alert = alertBuilder.create();
        alert.show();
    }

    private void uploadImage(final String expenseid) {

        mImageView.buildDrawingCache();
        mImageView2.buildDrawingCache();
        mImageView3.buildDrawingCache();
        mImageView4.buildDrawingCache();
        mImageView5.buildDrawingCache();

        Bitmap bitmap1 = mImageView.getDrawingCache();
        Bitmap bitmap2 = mImageView2.getDrawingCache();
        Bitmap bitmap3 = mImageView3.getDrawingCache();
        Bitmap bitmap4 = mImageView4.getDrawingCache();
        Bitmap bitmap5 = mImageView5.getDrawingCache();

        final String imageOne = getStringImage(bitmap1);

        final String imageTwo = getStringImage(bitmap2);
        final String imageThree = getStringImage(bitmap3);
        final String imageFour = getStringImage(bitmap4);
        final String imageFive = getStringImage(bitmap5);

        Log.e("Image One", imageOne);

        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.setMessage("Now uploading image Please wait...");
        pDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, uploadExpenseImage,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        System.out.println("////////////////123 "+s);
                        Toast.makeText(getApplicationContext(),"Successfully saved image", Toast.LENGTH_LONG).show();
                        pDialog.hide();

                        Intent intent = new Intent(getApplicationContext(), ExpenseManager.class);
                        intent.putExtra("VEHICLE_SELECTED", vehicle_reg_no);
                        startActivity(intent);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                        System.out.println("volleyError response " + volleyError.getMessage());
                        //Showing toast
                        Toast.makeText(getApplicationContext(), "Error. Try Reopening App", Toast.LENGTH_LONG).show();

                        dialogBuilder = NiftyDialogBuilder.getInstance(Expenses.this);
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
                params.put("getdata", "UploadImage");
                if(!imageOne.equals(""))
                {
                    params.put("insert_image_one", imageOne);
                }

                if(!imageTwo.equals(""))
                {
                    params.put("insert_image_two", imageTwo);
                }

                if(!imageThree.equals(""))
                {
                    params.put("insert_image_three", imageThree);
                }

                if(!imageFour.equals(""))
                {
                    params.put("insert_image_four", imageFour);
                }

                if(!imageFive.equals(""))

                    params.put("insert_image_five", imageFive);
                params.put("expense_id", expenseid);
                return params;
            }
        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }

    public String getStringImage(Bitmap bmp) {
        if(bmp != null)
        {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] imageBytes = baos.toByteArray();
            String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
            return encodedImage;
        }
        else
        {
            return "";
        }

    }

    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG,"Permission is granted");
                return true;
            } else {

                Log.v(TAG,"Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG,"Permission is granted");
            return true;
        }
    }

    public static void saveToPreferences(Context context, String key,
                                         Boolean allowed) {
        SharedPreferences myPrefs = context.getSharedPreferences
                (CAMERA_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = myPrefs.edit();
        prefsEditor.putBoolean(key, allowed);
        prefsEditor.commit();
    }

    public static Boolean getFromPref(Context context, String key) {
        SharedPreferences myPrefs = context.getSharedPreferences
                (CAMERA_PREF, Context.MODE_PRIVATE);
        return (myPrefs.getBoolean(key, false));
    }

    private void showAlert() {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage("App needs to access the Camera.");
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "DONT ALLOW",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "ALLOW",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        ActivityCompat.requestPermissions(Expenses.this,
                                new String[]{Manifest.permission.CAMERA},
                                MY_PERMISSIONS_REQUEST_CAMERA);

                    }
                });
        alertDialog.show();
    }

    private void showSettingsAlert() {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage("App needs to access the Camera.");
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "DONT ALLOW",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        //finish();
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "SETTINGS",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        startInstalledAppDetailsActivity(Expenses.this);

                    }
                });
        alertDialog.show();
    }


    @Override
    public void onRequestPermissionsResult
            (int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CAMERA: {
                for (int i = 0, len = permissions.length; i < len; i++) {
                    String permission = permissions[i];
                    if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                        boolean showRationale =
                                ActivityCompat.shouldShowRequestPermissionRationale
                                        (this, permission);
                        if (showRationale) {
                            showAlert();
                        } else if (!showRationale) {
                            // user denied flagging NEVER ASK AGAIN
                            // you can either enable some fall back,
                            // disable features of your app
                            // or open another dialog explaining
                            // again the permission and directing to
                            // the app setting
                            saveToPreferences(Expenses.this, ALLOW_KEY, true);
                        }
                    }
                }
            }
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // do your stuff
                } else {
                    Toast.makeText(Expenses.this, "GET_ACCOUNTS Denied",
                            Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions,
                        grantResults);

                // other 'case' lines to check for other
                // permissions this app might request

        }
    }

    public static void startInstalledAppDetailsActivity(final Activity context) {
        if (context == null) {
            return;
        }
        final Intent i = new Intent();
        i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        i.addCategory(Intent.CATEGORY_DEFAULT);
        i.setData(Uri.parse("package:" + context.getPackageName()));
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        context.startActivity(i);
    }

    Uri outputFileUri;
    int cameraCounter =0;
    private void openCamera() {

        if(cameraCounter == 0)
        {
            isImageOneSet = true;
            ++cameraCounter;
        }
        else if (cameraCounter == 1)
        {
            isImageTwoSet = true;
            ++cameraCounter;
        }
        else if (cameraCounter == 2)
        {
            isImageThreeSet = true;
            ++cameraCounter;
        }
        else if (cameraCounter == 3)
        {
            isImageFourSet = true;
            ++cameraCounter;
        }
        else if (cameraCounter == 4)
        {
            isImageFiveSet = true;
            ++cameraCounter;
        }

        if(isStoragePermissionGranted())
        {
            count++;
            String file = dir+count+".jpg";
            File newfile = new File(file);
            try {
                //newfile.createNewFile();
                newfile = createImageFile();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

            outputFileUri = FileProvider.getUriForFile(Expenses.this, getApplicationContext().getPackageName() + ".com.usalamatechnology.manageapp.provider",newfile);

            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            cameraIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivityForResult(cameraIntent, TAKE_PHOTO_CODE);

        }

    }

    String imageFilePath;
    String imageFilePath2;
    boolean isImageOneSet = false;
    boolean isImageTwoSet = false;
    boolean isImageThreeSet = false;
    boolean isImageFourSet = false;
    boolean isImageFiveSet = false;
    private File createImageFile() throws IOException {
        String timeStamp =
                new SimpleDateFormat("yyyyMMdd_HHmmss",
                        Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File storageDir =
                getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        imageFilePath = image.getAbsolutePath();

        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TAKE_PHOTO_CODE && resultCode == RESULT_OK) {

            if(isImageOneSet)
            {
                Glide.with(this).load(imageFilePath).into(mImageView);
                Toast.makeText(getApplicationContext(),"Picture Saved", Toast.LENGTH_LONG).show();
                isImageOneSet = false;
                mImageView2.setVisibility(View.VISIBLE);
            }
            else if(isImageTwoSet)
            {
                Glide.with(this).load(imageFilePath).into(mImageView2);
                Toast.makeText(getApplicationContext(),"Picture Saved", Toast.LENGTH_LONG).show();
                isImageTwoSet = false;
                mImageView3.setVisibility(View.VISIBLE);
            }
            else if(isImageThreeSet)
            {
                Glide.with(this).load(imageFilePath).into(mImageView3);
                Toast.makeText(getApplicationContext(),"Picture Saved", Toast.LENGTH_LONG).show();
                isImageThreeSet = false;
                mImageView4.setVisibility(View.VISIBLE);
            }
            else if(isImageFourSet)
            {
                Glide.with(this).load(imageFilePath).into(mImageView4);
                Toast.makeText(getApplicationContext(),"Picture Saved", Toast.LENGTH_LONG).show();
                isImageFourSet = false;
                mImageView5.setVisibility(View.VISIBLE);
            }
            else if(isImageFiveSet)
            {
                Glide.with(this).load(imageFilePath).into(mImageView5);
                Toast.makeText(getApplicationContext(),"Picture Saved", Toast.LENGTH_LONG).show();
                isImageFiveSet = false;
                //mImageView5.setVisibility(View.VISIBLE);
            }

        }
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
