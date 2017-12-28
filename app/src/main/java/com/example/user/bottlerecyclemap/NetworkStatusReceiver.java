package com.example.user.bottlerecyclemap;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * Created by user on 2017-04-17.
 */

public class NetworkStatusReceiver extends BroadcastReceiver {

    private LocationManager locManager;
    private LocationListener locListener;

    public NetworkStatusReceiver() {
    }

    public NetworkStatusReceiver(LocationManager locManager, LocationListener locListener) {
        this.locManager = locManager;
        this.locListener = locListener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if(action.equals(ConnectivityManager.CONNECTIVITY_ACTION)){
            try{
                ConnectivityManager connMgr = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo mobile = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
                NetworkInfo wifi = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

                if(!wifi.isConnected() && !mobile.isConnected()){
                    locManager.removeUpdates(locListener);
                    Log.d("TAG", "null");
                }

                Log.d("TAG", String.valueOf(wifi.isConnected()));

                Log.d("TAG", "action");
            }catch (Exception ex) {}
        }
    }
}
