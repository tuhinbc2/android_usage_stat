package com.example.tuhin.usage_statistics;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    SharedPreferences pref = null;
    SharedPreferences.Editor editor = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent serviceIntent = new Intent(this, AService.class);
        startService(serviceIntent);
        create();
    }

    void create(){
        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();

        if(!pref.contains("today")){

            Calendar c = Calendar.getInstance();

            editor.putInt("today", c.get(Calendar.DAY_OF_MONTH));

            for(int i=0;i<33;i++){
                String duration = i + ".duration";
                String last     = i + ".last";

                editor.putInt(duration, 0);
                editor.putString(last, "not yet");
            }

            editor.apply();
        }
    }

    public void on_click(View v){
        EditText et = (EditText) findViewById(R.id.editText);
        et.setText("");

        for(int i=0;i<33;i++){
            String duration = i + ".duration";
            String last     = i + ".last";

            et.append("#" + i + ": " + (pref.getInt(duration, -420) / 60) + " min <--> " +
                    pref.getString(last, "r u nuts?") + "\n");
        }
    }
}
