package com.ravinder.taskproject;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;

import java.net.InetAddress;

public class NetworkReceiver extends BroadcastReceiver {

    public NetworkChangeListener networkChangeListener;
    private static final String ACTION = ConnectivityManager.CONNECTIVITY_ACTION;
    private boolean isNetworkAlive = true;

    public NetworkReceiver(NetworkChangeListener networkChangeListener) {
        super();
        this.networkChangeListener = networkChangeListener;
    }

    public NetworkReceiver() { }

    @Override
    public void onReceive(final Context context, Intent intent) {
        if (intent.getAction().equals(ACTION)) {
            if (!isNetworkAvailable(context)) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        if (!isNetworkAvailable(context)) {
                            networkChangeListener.onNetworkConnectionChanged(false);
                        }

                    }


                }, 3000);
            } else {
                networkChangeListener.onNetworkConnectionChanged(true);
            }
        }

    }

    public static boolean isNetworkAvailable(Context context) {

        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
        } else
            connected = false;

        return connected;
    }

    public static boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    public interface NetworkChangeListener {
        void onNetworkConnectionChanged(boolean isConnected);
    }

    public boolean isInternetAvailable() {
        try {
            InetAddress ipAddr = InetAddress.getByName("google.com");
            //You can replace it with your name
            return !ipAddr.equals("");

        } catch (Exception e) {
            return false;
        }
    }

}
