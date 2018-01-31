package com.uriah.admin.worldvisioncable;


import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;


public class MyApplication extends MultiDexApplication {
    public void onCreate() {
        super.onCreate();

        // Stetho.initializeWithDefaults(this);


    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}