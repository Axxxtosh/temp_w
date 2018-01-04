package com.example.admin.worldvisioncable;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;


import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import android.util.Log;
import android.view.View;

import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import java.util.Arrays;

import dmax.dialog.SpotsDialog;

public class WebContent extends AppCompatActivity {

    private static final String TAG = "WebContent";
    SharedPreferences sp;
    static Context mContext;
    public static final String KEY_ATOM2REQUEST = "Atom2Request";
    String Atom2Request;
    Intent intent;
    private SpotsDialog home_dialog;
    boolean loadingFinished = true;
    boolean redirect = false;
    Button backPayment,closeView;
    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_content);

        mContext = this;

        closeView= findViewById(R.id.close_payment);
        closeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                AlertDialog.Builder builder1 = new AlertDialog.Builder(view.getContext());
                builder1.setMessage("Are you sure you want to exit?");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                finish();
                                Intent i=new Intent(WebContent.this,HomePageActivity.class);
                startActivity(i);

                            }
                        });

                builder1.setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();
            }
        });

/*
        backPayment=(Button)findViewById(R.id.backPayment);
        backPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent i=new Intent(WebContent.this,HomePageActivity.class);
                startActivity(i);
                finish();
            }
        });*/
        Bundle extras = getIntent().getExtras();
        if (extras != null)
            Atom2Request = extras.getString("Keyatomrequest");
        Log.d("ATOM2Request webview", Atom2Request);
        WebView webView = findViewById(R.id.webView);
        webView.setWebViewClient(new MyWebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        //Extra

        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setDefaultTextEncodingName("utf-8");
        //
        
        webView.addJavascriptInterface(new WebAppInterface(this), "Android");

        webView.loadUrl(Atom2Request);

    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String urlNewString) {
            if (!loadingFinished) {
                redirect = true;
            }

            loadingFinished = false;
            view.loadUrl(urlNewString);
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap facIcon) {
            loadingFinished = false;

            
            Log.w(TAG, "Loading");
        }

        @Override
        public void onPageFinished(WebView view, String url) {

            if (!redirect) {
                loadingFinished = true;
            }

            if (loadingFinished && !redirect) {

                Log.w(TAG, "Finish Loading");

            } else {
                redirect = false;
            }

        }
    }
    public class WebAppInterface {
        Context mContext;
        WebAppInterface(Context c) {
            mContext = c;
        }
        @android.webkit.JavascriptInterface
        public void onResponse(String reponseText) {
            Intent returnIntent = new Intent();
            returnIntent.putExtra("Result", reponseText);
            setResult(RESULT_OK, returnIntent);
            Toast.makeText(mContext, "in Web content", Toast.LENGTH_SHORT).show();
            Log.d("Payment",reponseText);
            //Response From Payment gateway
            String[] parts = reponseText.split(",");

            Log.d("Payment", Arrays.toString(parts));

            if(parts[6].equals("F")){
                Toast.makeText(mContext, "Payment Failure", Toast.LENGTH_SHORT).show();

            }else if(parts[6].equals("C")){
                Toast.makeText(mContext, "Canceled by user", Toast.LENGTH_SHORT).show();

            }else if(parts[6].equals("OK")){
                Toast.makeText(mContext, "Payment Success", Toast.LENGTH_SHORT).show();

            }


            //finish();

        }
    }


    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

}
