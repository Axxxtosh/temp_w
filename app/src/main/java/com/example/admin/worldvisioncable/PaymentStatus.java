package com.example.admin.worldvisioncable;

import android.graphics.drawable.Animatable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class PaymentStatus extends AppCompatActivity {
    private String tra_id,tra_number,amount,datetime,package_name;

    TextView tv_tra_id,tv_tra_number,tv_amount,tv_datetime,tv_package_name;
    private ImageView checkView;
    Button close;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_status);

        tv_tra_id=(TextView)findViewById(R.id.tra_id);
        tv_package_name=(TextView)findViewById(R.id.package_name);
        close=(Button) findViewById(R.id.close_button);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            tra_number = extras.getString("transaction_number");
            tra_id = extras.getString("transaction_id");
            amount= extras.getString("amount");
            datetime = extras.getString("date_time");
            package_name = extras.getString("package_name");
            //The key argument here must match that used in the other activity


        }
        //Check
        checkView = (ImageView) findViewById(R.id.check);

        ((Animatable) checkView.getDrawable()).start();
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });





    }

}
