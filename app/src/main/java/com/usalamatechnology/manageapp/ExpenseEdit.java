package com.usalamatechnology.manageapp;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AlertDialog.Builder;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.frosquivel.magicalcamera.MagicalCamera;
import com.gitonway.lee.niftymodaldialogeffects.lib.Effectstype;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.usalamatechnology.manageapp.Constants.deleteExpense;
import static com.usalamatechnology.manageapp.Constants.editExpense;
import static com.usalamatechnology.manageapp.Constants.imageConstant;
import static com.usalamatechnology.manageapp.Constants.uploadExpenseImage;
import static com.usalamatechnology.manageapp.Constants.vehicle_reg_no;

public class ExpenseEdit extends AppCompatActivity {
    public static final String ALLOW_KEY = "ALLOWED";
    public static final String CAMERA_PREF = "camera_pref";
    public static final int MY_PERMISSIONS_REQUEST_CAMERA = 100;
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
    public static int count = 0;
    private String TAG;
    int TAKE_PHOTO_CODE = 0;
    Button camera;
    int cameraCounter;
    EditText date;
    TextView_Lato delete;
    final String dir;
    EditText editTextCash;
    String id;
    String imageFilePath;
    String imageFilePath2;
    boolean isImageFiveSet;
    boolean isImageFourSet;
    boolean isImageOneSet;
    boolean isImageThreeSet;
    boolean isImageTwoSet;
    private ImageView mImageView;
    private ImageView mImageView2;
    private ImageView mImageView3;
    private ImageView mImageView4;
    private ImageView mImageView5;

    private NiftyDialogBuilder dialogBuilder;

    EditText other;
    Uri outputFileUri;
    TextView_Lato save;
    private Spinner spinner;
    Textview_lato_thin vehicle_reg_no_a;

    public ExpenseEdit() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES));
        stringBuilder.append("/picFolder/");
        this.dir = stringBuilder.toString();
        this.TAG = "TAG";
        this.cameraCounter = 0;
        this.isImageOneSet = false;
        this.isImageTwoSet = false;
        this.isImageThreeSet = false;
        this.isImageFourSet = false;
        this.isImageFiveSet = false;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_expense_edit);
        findViewById(R.id.back).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                ExpenseEdit.this.startActivity(new Intent(ExpenseEdit.this, Home.class));
            }
        });

        //CheckNetwork.isInternetAvailable(ExpenseEdit.this);

        Bundle extras = getIntent().getExtras();
        final String user_id = extras.getString(Constants.user_id);
        this.id = user_id;
        String type = extras.getString(Constants.type);
        String time = extras.getString(Constants.time);
        String address = extras.getString(Constants.address);
        String city = extras.getString(Constants.city);
        String more = extras.getString(Constants.more);
        String amount = extras.getString(Constants.amount);
        String category = extras.getString(Constants.category);
        final String vehicle_reg_no = extras.getString(Constants.vehicle_reg_no);

        this.date = (EditText) findViewById(R.id.dateTime);
        this.date.setKeyListener(null);
        this.editTextCash = (EditText) findViewById(R.id.editTextCash);
        this.other = (EditText) findViewById(R.id.editTextOther);
        this.vehicle_reg_no_a = (Textview_lato_thin) findViewById(R.id.vehicle_reg_no);
        this.camera = (Button) findViewById(R.id.button);
        this.mImageView = (ImageView) findViewById(R.id.imageCam);
        this.mImageView2 = (ImageView) findViewById(R.id.imageCam2);
        this.mImageView3 = (ImageView) findViewById(R.id.imageCam3);
        this.mImageView4 = (ImageView) findViewById(R.id.imageCam4);
        this.mImageView5 = (ImageView) findViewById(R.id.imageCam5);

        new File(this.dir).mkdirs();
        this.camera.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(ExpenseEdit.this, MagicalCamera.CAMERA) != 0) {
                    if (ExpenseEdit.getFromPref(ExpenseEdit.this, "ALLOWED").booleanValue()) {
                        ExpenseEdit.this.showSettingsAlert();
                    } else if (ContextCompat.checkSelfPermission(ExpenseEdit.this, MagicalCamera.CAMERA) == 0) {
                    } else {
                        if (ActivityCompat.shouldShowRequestPermissionRationale(ExpenseEdit.this, MagicalCamera.CAMERA)) {
                            ExpenseEdit.this.showAlert();
                        } else {
                            ActivityCompat.requestPermissions(ExpenseEdit.this, new String[]{MagicalCamera.CAMERA}, 100);
                        }
                    }
                } else if (ExpenseEdit.this.checkPermissionREAD_EXTERNAL_STORAGE(ExpenseEdit.this)) {
                    ExpenseEdit.this.openCamera();
                } else {
                    Toast.makeText(ExpenseEdit.this.getApplicationContext(), "Read permission not granted", Toast.LENGTH_SHORT).show();
                }
            }
        });
        this.save = (TextView_Lato) findViewById(R.id.save_expense);
        this.delete = (TextView_Lato) findViewById(R.id.delete_expense);
        this.spinner = (Spinner) findViewById(R.id.spinnerType);
        List<String> dataset = new ArrayList();
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
        this.spinner.setAdapter(arrayAdapter);
        PrintStream printStream = System.out;
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append("////////////////123 ");
        stringBuilder2.append(type);
        printStream.println(stringBuilder2.toString());
        this.spinner.setSelection(arrayAdapter.getPosition(type));
        this.delete.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {



                Volley.newRequestQueue(ExpenseEdit.this.getApplicationContext()).add(new StringRequest(1, deleteExpense, new Listener<String>() {
                    public void onResponse(String s) {
                        PrintStream printStream = System.out;
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("////////////////123 ");
                        stringBuilder.append(s);
                        printStream.println(stringBuilder.toString());
                        Toast.makeText(ExpenseEdit.this.getApplicationContext(), "Successfully deleted expense", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(getApplicationContext(), ExpenseManager.class);
                        intent.putExtra("VEHICLE_SELECTED", vehicle_reg_no);

                        startActivity(intent);
                    }
                }, new ErrorListener() {
                    public void onErrorResponse(VolleyError volleyError) {
                        //Dismissing the progress dialog
                        //loading.dismiss();

                        System.out.println("volleyError response " + volleyError.getMessage());
                        //Showing toast
                        Toast.makeText(getApplicationContext(), "Error deleting, try again", Toast.LENGTH_LONG).show();

                        dialogBuilder = NiftyDialogBuilder.getInstance(ExpenseEdit.this);
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
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new Hashtable();
                        params.put("id", user_id);
                        return params;
                    }
                });



            }
        });
        this.save.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {



                if (!ExpenseEdit.this.editTextCash.getText().toString().equals("")) {

                    Volley.newRequestQueue(ExpenseEdit.this.getApplicationContext()).add(new StringRequest(1, editExpense, new Listener<String>() {
                        public void onResponse(String s) {
                            PrintStream printStream = System.out;
                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append("////////////////123 ");
                            stringBuilder.append(s);
                            printStream.println(stringBuilder.toString());
                            Toast.makeText(ExpenseEdit.this.getApplicationContext(), "Successfully edited expense", Toast.LENGTH_SHORT).show();

                            ExpenseEdit.this.uploadImage(ExpenseEdit.this.id);

                        }
                    }, new ErrorListener() {
                        public void onErrorResponse(VolleyError volleyError) {
                            //Dismissing the progress dialog
                            //loading.dismiss();
                            System.out.println("volleyError response " + volleyError.getMessage());
                            //Showing toast
                            Toast.makeText(getApplicationContext(), "Error editing, try again", Toast.LENGTH_LONG).show();


                        }
                    }) {
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new Hashtable();
                            params.put("id", user_id);
                            params.put("amount", ExpenseEdit.this.editTextCash.getText().toString());
                            params.put("other", ExpenseEdit.this.other.getText().toString());
                            params.put("lat", Constants.credentialsSharedPreferences.getString(Constants.latKey, "0"));
                            params.put("lon", Constants.credentialsSharedPreferences.getString(Constants.lonKey, "0"));
                            String str = Constants.addressKey;
                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append(Constants.credentialsSharedPreferences.getString(Constants.getSubLocality, ""));
                            stringBuilder.append(",");
                            stringBuilder.append(Constants.credentialsSharedPreferences.getString(Constants.getLocality, ""));
                            stringBuilder.append(",");
                            stringBuilder.append(Constants.credentialsSharedPreferences.getString(Constants.getAdminArea, ""));
                            params.put(str, stringBuilder.toString());
                            return params;
                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(), "Please enter amount of expenses first.", Toast.LENGTH_LONG).show();
                }




            }
        });
        Date dateType = new Date(Long.parseLong(time) * 1000);
        this.date.setText(new SimpleDateFormat("Y-M-d HH:mm:ss").format(dateType));
        this.vehicle_reg_no_a.setText(vehicle_reg_no);
        this.editTextCash.setText(amount);
        this.other.setText(more);
    }

    private void openCamera() {
        if (this.cameraCounter == 0) {
            this.isImageOneSet = true;
            this.cameraCounter++;
        } else if (this.cameraCounter == 1) {
            this.isImageTwoSet = true;
            this.cameraCounter++;
        } else if (this.cameraCounter == 2) {
            this.isImageThreeSet = true;
            this.cameraCounter++;
        } else if (this.cameraCounter == 3) {
            this.isImageFourSet = true;
            this.cameraCounter++;
        } else if (this.cameraCounter == 4) {
            this.isImageFiveSet = true;
            this.cameraCounter++;
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

            outputFileUri = FileProvider.getUriForFile(ExpenseEdit.this, getApplicationContext().getPackageName() + ".com.usalamatechnology.manageapp.provider",newfile);

            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            cameraIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivityForResult(cameraIntent, TAKE_PHOTO_CODE);

        }
    }

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

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode != this.TAKE_PHOTO_CODE || resultCode != -1) {
            return;
        }
        if (this.isImageOneSet) {
            Glide.with( this).load(this.imageFilePath).into(this.mImageView);
            Toast.makeText(getApplicationContext(), "Picture Saved", Toast.LENGTH_LONG).show();
            this.isImageOneSet = false;
            this.mImageView2.setVisibility(View.VISIBLE);
        } else if (this.isImageTwoSet) {
            Glide.with( this).load(this.imageFilePath).into(this.mImageView2);
            Toast.makeText(getApplicationContext(), "Picture Saved", Toast.LENGTH_LONG).show();
            this.isImageTwoSet = false;
            this.mImageView3.setVisibility(View.VISIBLE);
        } else if (this.isImageThreeSet) {
            Glide.with( this).load(this.imageFilePath).into(this.mImageView3);
            Toast.makeText(getApplicationContext(), "Picture Saved", Toast.LENGTH_LONG).show();
            this.isImageThreeSet = false;
            this.mImageView4.setVisibility(View.VISIBLE);
        } else if (this.isImageFourSet) {
            Glide.with( this).load(this.imageFilePath).into(this.mImageView4);
            Toast.makeText(getApplicationContext(), "Picture Saved", Toast.LENGTH_LONG).show();
            this.isImageFourSet = false;
            this.mImageView5.setVisibility(View.VISIBLE);
        } else if (this.isImageFiveSet) {
            Glide.with( this).load(this.imageFilePath).into(this.mImageView5);
            Toast.makeText(getApplicationContext(), "Picture Saved", Toast.LENGTH_LONG).show();
            this.isImageFiveSet = false;
        }
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
                        //Dismissing the progress dialog
                        //loading.dismiss();
                        System.out.println("volleyError response " + volleyError.getMessage());
                        //Showing toast
                        Toast.makeText(getApplicationContext(), "Error. Try Reopening App", Toast.LENGTH_LONG).show();

                        dialogBuilder = NiftyDialogBuilder.getInstance(ExpenseEdit.this);
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

    private void showAlert() {
        AlertDialog alertDialog = new Builder(this).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage("App needs to access the Camera.");
        alertDialog.setButton(-2, (CharSequence) "DONT ALLOW", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                ExpenseEdit.this.finish();
            }
        });
        alertDialog.setButton(-1, (CharSequence) "ALLOW", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                ActivityCompat.requestPermissions(ExpenseEdit.this, new String[]{MagicalCamera.CAMERA}, 100);
            }
        });
        alertDialog.show();
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

    public static Boolean getFromPref(Context context, String key) {
        return Boolean.valueOf(context.getSharedPreferences("camera_pref", 0).getBoolean(key, false));
    }

    private void showSettingsAlert() {
        AlertDialog alertDialog = new Builder(this).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage("App needs to access the Camera.");
        alertDialog.setButton(-2, (CharSequence) "DONT ALLOW", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.setButton(-1, (CharSequence) "SETTINGS", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                ExpenseEdit.startInstalledAppDetailsActivity(ExpenseEdit.this);
            }
        });
        alertDialog.show();
    }

    public boolean checkPermissionREAD_EXTERNAL_STORAGE(Context context) {
        if (VERSION.SDK_INT < 23 || ContextCompat.checkSelfPermission(context, MagicalCamera.EXTERNAL_STORAGE) == 0) {
            return true;
        }
        if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, MagicalCamera.EXTERNAL_STORAGE)) {
            showDialog("External storage", context, MagicalCamera.EXTERNAL_STORAGE);
        } else {
            ActivityCompat.requestPermissions((Activity) context, new String[]{MagicalCamera.EXTERNAL_STORAGE}, 123);
        }
        return false;
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
}