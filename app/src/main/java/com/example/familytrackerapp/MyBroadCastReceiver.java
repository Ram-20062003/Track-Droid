package com.example.familytrackerapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class MyBroadCastReceiver extends BroadcastReceiver {
    private static final String TAG = "MyBroadCastReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "Broadcastreceiver class is working ");
        Intent intent_exist=new Intent(context,MyService.class);
        if(intent.getAction().equals("action_stop"))
        {
            context.stopService(intent_exist);
        }
    }
}
