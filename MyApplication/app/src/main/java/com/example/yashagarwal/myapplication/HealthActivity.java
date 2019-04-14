package com.example.yashagarwal.myapplication;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.telephony.SmsManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;

public class HealthActivity extends AppCompatActivity {
    public static TextView tvShakeService;
    Context mContext=this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_help);
    }
    public void startservice (View v) {
        EditText mess=(EditText)findViewById(R.id.msg);
        String mail=mess.getText().toString();
        TextView text=(TextView)findViewById(R.id.txtstart);
        if (mail.isEmpty()) {
            Toast.makeText(getBaseContext(), "SMS can't be left empty!!", Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(this,StartActivity.class);
            intent.putExtra("mesg",mail);
            startActivity(intent);
        }
    }

}




