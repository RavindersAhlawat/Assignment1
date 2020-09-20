package com.ravinder.taskproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity2 extends AppCompatActivity implements NetworkReceiver.NetworkChangeListener {
    private NetworkReceiver mNetworkReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
    }

    @Override
    protected void onResume () {

        mNetworkReceiver = new NetworkReceiver(this);
        registerNetworkBroadcastForNougat();
        super.onResume();
    }



    private void registerNetworkBroadcastForNougat() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            registerReceiver(mNetworkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            registerReceiver(mNetworkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
    }

    @Override
    protected void onPause () {
        super.onPause();
        unregisterReceiver(mNetworkReceiver);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (isConnected){
            Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this, "Not Connected", Toast.LENGTH_SHORT).show();

        }
    }
}