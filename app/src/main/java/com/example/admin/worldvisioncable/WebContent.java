package com.example.admin.worldvisioncable;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

public class WebContent extends AppCompatActivity {

    private static final String TAG = "WebContent";
    SharedPreferences sp;
    static Context mContext;
    public static final String KEY_ATOM2REQUEST = "Atom2Request";
    String Atom2Request;
    Intent intent;
    boolean loadingFinished = true;
    boolean redirect = false;
    Button backPayment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_content);

        mContext = this;

        backPayment=(Button)findViewById(R.id.backPayment);
        backPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        Bundle extras = getIntent().getExtras();
        if (extras != null)
            Atom2Request = extras.getString("Keyatomrequest");
        Log.d("ATOM2Request webview", Atom2Request);
        WebView webView = (WebView) findViewById(R.id.webView);
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
            //SHOW LOADING IF IT ISNT ALREADY VISIBLE
            Log.w(TAG, "Loading");
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            if (!redirect) {
                loadingFinished = true;
            }

            if (loadingFinished && !redirect) {
                //HIDE LOADING IT HAS FINISHED
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
            finish();

        }
    }

}
