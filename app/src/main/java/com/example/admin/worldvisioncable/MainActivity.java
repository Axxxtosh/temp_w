package com.example.admin.worldvisioncable;

/*
*
* Cleaned Final -- by Ashutosh on 20th Dec 2017
*
* */

import android.app.Activity;
import android.content.Intent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Toast;


import com.daimajia.slider.library.SliderTypes.BaseSliderView;

import com.example.admin.worldvisioncable.Session.UserSessionManager;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;


public class MainActivity extends AppCompatActivity implements BaseSliderView.OnSliderClickListener{


    CardView hathway,cabletv,newConnection,login,contactus;

    UserSessionManager sessionManager;
    public static Activity fa;
    Animation animation1,animation2,animation3;
    Button viewoffers;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //For Full Screen Activity
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        fa=this;
        //
        setContentView(R.layout.activity_main_verticle);

        sessionManager = new UserSessionManager(MainActivity.this);
      //  mSlider = (SliderLayout)findViewById(R.id.slider_home);
        hathway = (CardView) findViewById(R.id.hathway);
        cabletv = (CardView) findViewById(R.id.cabletv);
        newConnection = (CardView) findViewById(R.id.newConnection);
        contactus=(CardView) findViewById(R.id.contact_us);
        viewoffers = (Button) findViewById(R.id.view_plans);

        login=(CardView) findViewById(R.id.login);

        animation1 = AnimationUtils.loadAnimation(this, R.anim.swing_up_right);
        animation2 = AnimationUtils.loadAnimation(this, R.anim.swing_up_left);
        animation3 = AnimationUtils.loadAnimation(this, R.anim.swing_up_right);



        newConnection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i=new Intent(MainActivity.this,NewConnectionActivity.class);
                startActivity(i);
            }
        });
        contactus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //contactus.setAnimation(AnimationUtils.loadAnimation(view.getContext(),R.anim.zoom_in));
                Intent i=new Intent(MainActivity.this,PaymentStatus.class);
                startActivity(i);
            }
        });



        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Login","Clicked");
                //login.setAnimation(AnimationUtils.loadAnimation(view.getContext(),R.anim.zoom_in));
                Intent i=new Intent(MainActivity.this,LoginActivity.class);
                startActivity(i);

            }
        });

        hathway.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //hathway.setAnimation(AnimationUtils.loadAnimation(view.getContext(),R.anim.zoom_in));
                Intent i=new Intent(MainActivity.this,InternetPacksActivity.class);
                startActivity(i);
            }
        });

        cabletv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                //cabletv.setAnimation(AnimationUtils.loadAnimation(view.getContext(),R.anim.zoom_in));
                Intent i=new Intent(MainActivity.this,CablePlansActivity.class);
                startActivity(i);
            }
        });
        //Firebase
        subscribeToPushService();




    }
    private void subscribeToPushService() {
        FirebaseMessaging.getInstance();

        Log.d("AndroidBash", "Subscribed");
        Toast.makeText(MainActivity.this, "Subscribed", Toast.LENGTH_SHORT).show();

        String token = FirebaseInstanceId.getInstance().getToken();

        // Log and toast
        Log.d("AndroidBash", token);
        Toast.makeText(MainActivity.this, token, Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onWindowFocusChanged (boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus)

        login.startAnimation(animation2);
        newConnection.startAnimation(animation1);
        hathway.startAnimation(animation2);
        cabletv.startAnimation(animation2);
        contactus.startAnimation(animation2);
        viewoffers.startAnimation(animation3);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menuwithout_drawer, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {

    }
}
