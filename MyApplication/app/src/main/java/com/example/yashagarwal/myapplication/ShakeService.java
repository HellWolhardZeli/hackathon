package com.example.yashagarwal.myapplication;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.telephony.SmsManager;
import android.widget.Toast;

import java.util.ArrayList;


public class ShakeService extends Service implements SensorEventListener {

    StartActivity starty;
    Context mContext = this;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private float mAccel; // acceleration apart from gravity
    private float mAccelCurrent; // current acceleration including gravity
    private float mAccelLast; // last acceleration including gravity

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(this, mAccelerometer,
                SensorManager.SENSOR_DELAY_UI, new Handler());
        return START_STICKY;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        starty = new StartActivity();
        String temp=starty.start();
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];
        mAccelLast = mAccelCurrent;
        mAccelCurrent = (float) Math.sqrt((double) (x * x + y * y + z * z));
        float delta = mAccelCurrent - mAccelLast;
        mAccel = mAccel * 0.9f + delta;
        // perform low-cut filter
        if (mAccel > 50){
            sendSMS("9984789430",temp);
            mAccel=0;
        }
    }
    private void sendSMS(String phoneNumber, String message) {
            ArrayList<PendingIntent> sentPendingIntents = new ArrayList<PendingIntent>();
            ArrayList<PendingIntent> deliveredPendingIntents = new ArrayList<PendingIntent>();
            PendingIntent sentPI = PendingIntent.getBroadcast(mContext, 0,
                    new Intent(mContext, SmsSentReceiver.class), 0);
            PendingIntent deliveredPI = PendingIntent.getBroadcast(mContext, 0,
                    new Intent(mContext, SmsDeliveredReceiver.class), 0);
            try {
                SmsManager sms = SmsManager.getDefault();
                ArrayList<String> mSMSMessage = sms.divideMessage(message);
                for (int i = 0; i < mSMSMessage.size(); i++) {
                    sentPendingIntents.add(i, sentPI);
                    deliveredPendingIntents.add(i, deliveredPI);
                }
                sms.sendMultipartTextMessage(phoneNumber, null, mSMSMessage,
                        sentPendingIntents, deliveredPendingIntents);

            } catch (Exception e) {

                e.printStackTrace();
                Toast.makeText(getBaseContext(), "SMS sending failed...", Toast.LENGTH_SHORT).show();
            }

        }
        public class SmsDeliveredReceiver extends BroadcastReceiver {
            @Override
            public void onReceive(Context context, Intent arg1) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Toast.makeText(context, "SMS delivered", Toast.LENGTH_SHORT).show();
                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(context, "SMS not delivered", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }
        public class SmsSentReceiver extends BroadcastReceiver {
            @Override
            public void onReceive(Context context, Intent arg1) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Toast.makeText(context, "SMS Sent", Toast.LENGTH_SHORT).show();

                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(context, "SMS generic failure", Toast.LENGTH_SHORT)
                                .show();

                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(context, "SMS no service", Toast.LENGTH_SHORT)
                                .show();

                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(context, "SMS null PDU", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(context, "SMS radio off", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }
    }




