package com.example.admin.worldvisioncable;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;


import com.example.admin.worldvisioncable.Models.UsedObject;
import com.example.admin.worldvisioncable.Session.UserSessionManager;


import java.util.HashMap;

import static android.content.DialogInterface.BUTTON_NEGATIVE;
import static android.content.DialogInterface.BUTTON_POSITIVE;

public class SplashScreen extends AppCompatActivity {

    private final int SPLASH_DISPLAY_LENGTH = 1500;
    private static final int PERMISSION_REQUEST_CODE = 200;

    UserSessionManager session;


    ConnectivityManager cn;
    NetworkInfo nf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Full Screen
        // remove title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        setContentView(R.layout.activity_splash_screen);



        cn=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        if (cn != null) {
            nf = cn.getActiveNetworkInfo();
        }
        if (!checkPermission()) {

            requestPermission();

        }
        else {
            new Handler().postDelayed(new Runnable(){
                @Override
                public void run() {

                    session = new UserSessionManager(SplashScreen.this);

                    // carry on the normal flow, as the case of  permissions  granted.
                    if(nf != null && nf.isConnected() ) {

                        Log.d("Data", " sessionChecking true");
                        HashMap<String, String> user = session.getUserDetails();



                        Log.d("UserId","s"+user.get(UserSessionManager.KEY_USERNAME));

                        UsedObject.setUserName(user.get(UserSessionManager.KEY_USERNAME));
                        UsedObject.setUserEmail(user.get(UserSessionManager.KEY_EMAIL));
                        UsedObject.setUserMobile(user.get(UserSessionManager.KEY_MOBILE));
                        UsedObject.setUserUID(user.get(UserSessionManager.KEY_USERID));
                        UsedObject.setId(user.get(UserSessionManager.KEY_ID));
                        UsedObject.setUserAddress(user.get(UserSessionManager.KEY_ADDRESS));




                        if (!session.checkLogin()) {
                            Intent i=new Intent(SplashScreen.this,HomePageActivity.class);
                            startActivity(i);
                            finish();
                        }
                        else
                        {
                            Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }

                    }
                    else {
                        //  Toast.makeText(SplashScreen.this, "Network Not Available", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(SplashScreen.this,TryAgainActivity.class);
                        startActivity(intent);
                        finish();
                    }


                }

            }, SPLASH_DISPLAY_LENGTH);

        }


    }


    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA);

        int result2 = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_SMS);
        // int result1 = ContextCompat.checkSelfPermission(getApplicationContext(),  Manifest.permission.CAMERA);

        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED
                && result2 == PackageManager.PERMISSION_GRANTED ;
    }

    private void requestPermission() {

        ActivityCompat.requestPermissions(this, new String[]{ Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.CAMERA,Manifest.permission.READ_SMS}, PERMISSION_REQUEST_CODE);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {

                    boolean locationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean cameraAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean readSMS = grantResults[2] == PackageManager.PERMISSION_GRANTED;


                    if (locationAccepted && cameraAccepted  && readSMS) {

                        new Handler().postDelayed(new Runnable(){
                            @Override
                            public void run() {

                                session = new UserSessionManager(SplashScreen.this);

                                // carry on the normal flow, as the case of  permissions  granted.
                                if(nf != null && nf.isConnected() ) {

                                    Log.d("Data", " sessionChecking true");
                                    HashMap<String, String> user = session.getUserDetails();



                                    UsedObject.setUserName(user.get(UserSessionManager.KEY_USERNAME));
                                    UsedObject.setUserEmail(user.get(UserSessionManager.KEY_EMAIL));
                                    UsedObject.setUserMobile(user.get(UserSessionManager.KEY_MOBILE));
                                    UsedObject.setUserUID(user.get(UserSessionManager.KEY_USERID));


                                    if (!session.checkLogin()) {
                                        Intent i=new Intent(SplashScreen.this,HomePageActivity.class);
                                        startActivity(i);
                                        finish();
                                    }
                                    else
                                    {
                                        Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }



                                }
                                else {
                                    //  Toast.makeText(SplashScreen.this, "Network Not Available", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(SplashScreen.this,TryAgainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }


                            }

                        }, SPLASH_DISPLAY_LENGTH);
                    }
                    // Snackbar.make(view, "Permission Granted, Now you can access location data and camera.", Snackbar.LENGTH_LONG).show();
                    else {

                        // Snackbar.make(view, "Permission Denied, You cannot access location data and camera.", Snackbar.LENGTH_LONG).show();

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) || shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) || shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                    || shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE) || shouldShowRequestPermissionRationale(Manifest.permission.RECORD_AUDIO) || shouldShowRequestPermissionRationale(Manifest.permission.READ_SMS)) {
                                showMessageOKCancel("You need to allow access to all the permissions. Otherwise Application will not work.",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    switch (which){
                                                        case BUTTON_POSITIVE:
                                                            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.RECORD_AUDIO,Manifest.permission.READ_SMS},
                                                                    PERMISSION_REQUEST_CODE);
                                                            break;
                                                        case BUTTON_NEGATIVE:
                                                            finish();
                                                            break;

                                                    }


                                                }
                                            }
                                        });
                                return;
                            }
                        }

                    }
                }


                break;
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(SplashScreen.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", okListener)
                .create()
                .show();
    }
}
