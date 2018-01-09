package com.uriah.admin.worldvisioncable;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class CablePlansActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cable_plans);
        CableMainFragment cableTvFragment = new CableMainFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        // ft.addToBackStack(null);
        ft.setCustomAnimations(R.anim.enter_left, R.anim.exit_right, R.anim.enter_right, R.anim.exit_left);
        ft.replace(R.id.cable_plans_activity, cableTvFragment, "Cable Fragment");
        ft.commit();
    }
}
