package com.example.tuhin.usage_statistics;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;

import java.util.Calendar;

/**
 * Created by tuhin on 4/21/17.
 */

public class AService extends Service {
    final String TAG = "hellotuhin";
    SharedPreferences.Editor editor = null;
    SharedPreferences pref = null;
    String duration_msg = ".duration";
    String last_msg = ".last";
    boolean started = false;
    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        public long on_time = -1, off_time = -1;
        int start_date = -1;
        public void start(){
            on_time = System.currentTimeMillis();
            Calendar c = Calendar.getInstance();
            start_date = c.get(Calendar.DAY_OF_MONTH);
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent == null){
                start();
                return;
            }
            if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
                Log.d(TAG, "screen is off" + Intent.ACTION_SCREEN_OFF);
                Calendar c = Calendar.getInstance();
                int date = c.get(Calendar.DAY_OF_MONTH);
                if(pref.getInt("today", -1) != date){
                    editor.putInt("today", date);
                    editor.putInt(date + duration_msg, 0);
                    editor.commit();
                    Log.i(TAG, "another day come");
                }
                off_time = System.currentTimeMillis();
                if(on_time != -1){
                    long duration = (off_time - on_time) / 1000;
                    if(duration > 5){
                        int d = pref.getInt(date + duration_msg, -1) + (int)duration;
                        Log.i(TAG, "on time " + d);
                        editor.putInt(date + duration_msg, d);
                    }
                }else{
                    start_date = date;
                }

                if(start_date != date){
                    date--;
                }

                editor.putString(date + last_msg, c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE));
                editor.apply();

            } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
                Log.d(TAG, Intent.ACTION_SCREEN_ON);
                start();
            }
        }
    };
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        Log.w(TAG, "onBind callback called");
        //throw new UnsupportedOperationException("Not yet implemented");
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(!started) {
            started = true;
            pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
            editor = pref.edit();


            Log.w(TAG, "onStartCommand callback called");
            IntentFilter intentFilter = new IntentFilter(Intent.ACTION_SCREEN_ON);
            intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
            receiver.onReceive(null, null);
            registerReceiver(receiver, intentFilter);
        }else{
            Log.i(TAG, "service already running..");
        }

        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.w(TAG, "onCreate callback called");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.w(TAG, "onDestroy callback called");
        unregisterReceiver(receiver);
        started = false;
    }
}
