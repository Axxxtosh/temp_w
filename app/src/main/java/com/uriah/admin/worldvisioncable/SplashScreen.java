package com.uriah.admin.worldvisioncable;

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


import com.uriah.admin.worldvisioncable.Models.UsedObject;
import com.uriah.admin.worldvisioncable.Session.UserSessionManager;


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






    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(SplashScreen.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", okListener)
                .create()
                .show();
    }
}
