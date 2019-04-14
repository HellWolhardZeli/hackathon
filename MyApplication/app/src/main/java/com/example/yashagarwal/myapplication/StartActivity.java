package com.example.yashagarwal.myapplication;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class StartActivity extends AppCompatActivity {
    Context mContext=this;
    public static String mail="Empty";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        Bundle extras = getIntent().getExtras();
        if (extras != null){
            mail= extras.getString("mesg");
        }
        Intent intent = new Intent(this,ShakeService.class);
        startService(intent);
    }
    public String start(){
        return mail;
    }

}
