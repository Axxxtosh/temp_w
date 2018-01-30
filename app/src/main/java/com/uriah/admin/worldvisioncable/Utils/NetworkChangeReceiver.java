package com.uriah.admin.worldvisioncable.Utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;


/**
 * Created by root on 29/1/18.
 */

public class NetworkChangeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, final Intent intent) {

        boolean IsConnected = NetworkUtil.getConnectivityStatusString(context);
        // Toast in here, you can retrieve other value like String from NetworkUtil


        if (!IsConnected) {

            Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT).show();
            Intent in = new Intent(context, NoInternetActivity.class);
            context.startActivity(in);

        }


        // but you need some change in NetworkUtil Class
    }
}