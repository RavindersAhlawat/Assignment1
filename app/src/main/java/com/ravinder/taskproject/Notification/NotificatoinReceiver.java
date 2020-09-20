package com.ravinder.taskproject.Notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class NotificatoinReceiver extends BroadcastReceiver {
    private String YES_ACTION="YES_ACTION";
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if(YES_ACTION.equals(action)) {
            Log.v("shuffTest","Pressed YES");
            Toast.makeText(context, "Notificatoin clicked", Toast.LENGTH_SHORT).show();
        }
    }
}
