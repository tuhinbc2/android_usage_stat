package com.example.tuhin.usage_statistics;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

/**
 * Created by tuhin on 4/21/17.
 */

public class ScreenEventHandler extends BroadcastReceiver{
    final String TAG = "hellotuhin";

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent serviceIntent = new Intent(context, AService.class);
        context.startService(serviceIntent);
    }

}
