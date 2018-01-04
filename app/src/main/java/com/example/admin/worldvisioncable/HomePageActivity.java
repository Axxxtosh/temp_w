package com.example.admin.worldvisioncable;


import android.content.Intent;

import android.os.Handler;
import android.support.design.widget.NavigationView;

import android.support.v4.app.FragmentTransaction;

import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.admin.worldvisioncable.Session.UserSessionManager;

public class HomePageActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{


    static NavigationView navigationView;
    static Toolbar toolbar;
    UserSessionManager sessionManager;

    View navHeader;
    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        sessionManager = new UserSessionManager(HomePageActivity.this);

        toolbar = findViewById(R.id.toolbar_homepage);
        toolbar.setTitle("World Vision Cable");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));



        setSupportActionBar(toolbar);

        navigationView = findViewById(R.id.nav_view);
        navHeader = navigationView.getHeaderView(0);

        if(sessionManager.getPaybillLoginStatus().equalsIgnoreCase("Y"))
        {
            sessionManager.StorePaybillLoginStatus("N");
            InternetPackFragment payBillFragment = new InternetPackFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(R.id.content_main, payBillFragment,"PayBil");
            ft.commit();
        }
        else {


            NewHomeFragment categoryFragment = new NewHomeFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(R.id.content_main, categoryFragment,"HomeFragment");
            ft.commit();
        }


        final DrawerLayout drawer = findViewById(R.id.drawer_layout);


        //drawer.setHomeAsUpIndicator(drawable);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);


        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawer.isDrawerVisible(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    drawer.openDrawer(GravityCompat.START);
                }
            }
        });

        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }





    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Log.d("ID",String.valueOf(id));
        //bottomBar.setVisibility(View.GONE);
        if (id == R.id.nav_home) {

            NewHomeFragment newHomeFragment = new NewHomeFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            // ft.addToBackStack(null);
            ft.setCustomAnimations(R.anim.enter_left, R.anim.exit_right, R.anim.enter_right, R.anim.exit_left);
            ft.replace(R.id.content_main, newHomeFragment, "HomeFragment");
            ft.commit();

        }


        if(id==R.id.nav_complaints)
        {
            CustomerSupport complaintsFragment = new CustomerSupport();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            // ft.addToBackStack(null);
            ft.setCustomAnimations(R.anim.enter_left, R.anim.exit_right, R.anim.enter_right, R.anim.exit_left);
            ft.replace(R.id.content_main, complaintsFragment, "ComplaintsFragment");
            ft.commit();
        }

        if(id==R.id.nav_CableTvPacks)
        {
            CableMainFragment cableTvFragment = new CableMainFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            // ft.addToBackStack(null);
            ft.setCustomAnimations(R.anim.enter_left, R.anim.exit_right, R.anim.enter_right, R.anim.exit_left);
            ft.replace(R.id.content_main, cableTvFragment, "Cable Fragment");
            ft.commit();
        }
        if(id==R.id.nav_InternetPacks)
        {
            InternetPackFragment internetPackFragment = new InternetPackFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            // ft.addToBackStack(null);
            ft.setCustomAnimations(R.anim.enter_left, R.anim.exit_right, R.anim.enter_right, R.anim.exit_left);
            ft.replace(R.id.content_main, internetPackFragment, "Internet Fragment");
            ft.commit();
        }

        if(id==R.id.nav_userAccount)
        {
            ProfileFragment profileFragment = new ProfileFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            // ft.addToBackStack(null);
            ft.setCustomAnimations(R.anim.enter_left, R.anim.exit_right, R.anim.enter_right, R.anim.exit_left);
            ft.replace(R.id.content_main, profileFragment, "Profile Fragment");
            ft.commit();
        }
        if(id==R.id.nav_ViewBills)
        {
            BillDetails billDetails = new BillDetails();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            // ft.addToBackStack(null);
            ft.setCustomAnimations(R.anim.enter_left, R.anim.exit_right, R.anim.enter_right, R.anim.exit_left);
            ft.replace(R.id.content_main, billDetails, "Bill Details Fragment");
            ft.commit();
        }
        if(id==R.id.nav_logout)
        {
            sessionManager.logoutUser();
        }
        if(id==R.id.nav_contactus)
        {
            Intent i=new Intent(HomePageActivity.this,ContactUsActivity.class);
            startActivity(i);
        }




        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /*@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {


        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            AlertDialog.Builder alertbox = new AlertDialog.Builder(HomePageActivity.this);
            alertbox.setIcon(R.drawable.ic_warning);
            alertbox.setTitle("Are you sure you want to logout?");
            alertbox.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {

                    // finish used for destroyed activity
                    finish();
                    sessionManager.logoutUser();

                }
            });

            alertbox.setNegativeButton("No", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {
                    // Nothing will be happened when clicked on no button

                    // of Dialog

                }
            });

            alertbox.show();
        }
        return super.onKeyDown(keyCode, event);
    }*/
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }

        else {

            if (doubleBackToExitPressedOnce) {
                finish();
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
}
