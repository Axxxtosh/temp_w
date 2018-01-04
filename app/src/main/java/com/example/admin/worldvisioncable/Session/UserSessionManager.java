package com.example.admin.worldvisioncable.Session;

/**
 * Created by Ani on 1/25/2017.
 */

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.widget.Toast;


import com.example.admin.worldvisioncable.MainActivity;
import com.example.admin.worldvisioncable.Models.UsedObject;

import java.util.HashMap;

import es.dmoral.toasty.Toasty;

public class UserSessionManager {

 // Shared Preferences reference
    SharedPreferences pref;

    // Editor reference for Shared preferences
    Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREFER_NAME = "UserSession";

    // All Shared Preferences Keys
    private static final String IS_USER_LOGIN = "IsUserLoggedIn";

    // User name (make variable public to access from outside)
    public static final String KEY_USERNAME = "username";

    // Email address (make variable public to access from outside)
    public static final String KEY_PASSWORD = "password";

    public  static final String KEY_ADDRESS = "Address";
    public  static final String KEY_CARDNO  = "CardNo";
    public  static final String KEY_CITYNAME  = "CityName";

    public  static final String KEY_STATENAME = "StateName";
    public  static final String KEY_COUNTRYNAME  = "CountryName";
    public  static final String KEY_ID  = "id";

    public  static final String KEY_EMAIL = "email";
    public  static final String KEY_MOBILE  = "mobile";
    public  static final String KEY_USERID  = "user_id";





    // Constructor
    public UserSessionManager(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREFER_NAME, PRIVATE_MODE);
         editor = pref.edit();

    }

    //Create login session
    public void createUserLoginSession(String username, String password,String email, String mobile,String user_id,String Address,String CardNo,String CityName,
                                       String StateName,String CountryName,String id){
        // Storing login value as TRUE
        editor.putBoolean(IS_USER_LOGIN, true);



        // Storing name in pref
        editor.putString(KEY_USERNAME, username);

        // Storing email in pref
        editor.putString(KEY_PASSWORD, password);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_MOBILE, mobile);
        editor.putString(KEY_USERID, user_id);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_MOBILE, mobile);
        editor.putString(KEY_USERID, user_id);

        editor.putString(KEY_ADDRESS, Address);
        editor.putString(KEY_CARDNO, CardNo);
        editor.putString(KEY_CITYNAME, CityName);
        editor.putString(KEY_STATENAME, StateName);
        editor.putString(KEY_COUNTRYNAME, CountryName);
        editor.putString(KEY_ID, id);


        // commit changes
        editor.commit();

        Toasty.success(_context, "Welcome " + username, Toast.LENGTH_SHORT, true).show();

        //Toast.makeText(this,"Commited",Toast.LENGTH_SHORT).show();
    }

    /**
     * Check login method will check user login status
     * If false it will redirect user to login page
     * Else do anything
     * */
    public boolean checkLogin(){
        // Check login status
        if(!this.isUserLoggedIn()){


            return true;
        }
        return false;
    }



    /**
     * Get stored session data
     * */
    public HashMap<String, String> getUserDetails(){


        HashMap<String, String> user = new HashMap<String, String>();


        user.put(KEY_USERNAME, pref.getString(KEY_USERNAME, null));


        user.put(KEY_PASSWORD, pref.getString(KEY_PASSWORD, null));
        user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));
        user.put(KEY_MOBILE, pref.getString(KEY_MOBILE, null));
        user.put(KEY_USERID, pref.getString(KEY_USERID, null));

        user.put(KEY_ADDRESS, pref.getString(KEY_ADDRESS, null));
        user.put(KEY_CARDNO, pref.getString(KEY_CARDNO, null));
        user.put(KEY_CITYNAME, pref.getString(KEY_CITYNAME, null));
        user.put(KEY_STATENAME, pref.getString(KEY_STATENAME, null));
        user.put(KEY_COUNTRYNAME, pref.getString(KEY_COUNTRYNAME, null));
        user.put(KEY_ID, pref.getString(KEY_ID, null));












        return user;
    }


    public void logoutUser(){


        editor.clear();
        editor.commit();

        UsedObject.setUserUID("null");


        Intent intent = new Intent(_context,MainActivity.class);
       intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
      intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
       _context.startActivity(intent);

    }

    public void StorePaybillLoginStatus(String podStatus) {
        editor = pref.edit();
        editor.putString("paybill_status_login", podStatus);
        editor.commit();
    }

    public String getPaybillLoginStatus() {
        return pref.getString("paybill_status_login","");
    }


    // Check for login
    public boolean isUserLoggedIn(){
        return pref.getBoolean(IS_USER_LOGIN, false);
    }
}