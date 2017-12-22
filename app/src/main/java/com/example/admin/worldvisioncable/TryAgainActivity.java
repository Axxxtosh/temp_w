package com.example.admin.worldvisioncable;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class TryAgainActivity extends AppCompatActivity {

    Button btn_tryagain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_try_again);

        btn_tryagain = (Button) findViewById(R.id.btn_tryAgain);

        btn_tryagain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent obj = new Intent(TryAgainActivity.this,SplashScreen.class);
                startActivity(obj);
                finish();
            }
        });
    }
}
