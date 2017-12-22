package com.example.admin.worldvisioncable;

import android.app.Application;

import com.facebook.stetho.Stetho;
import com.google.firebase.FirebaseApp;


public class MyApplication extends Application {
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);

        //Firebase registration
        //FirebaseApp.initializeApp(this);

    }
}