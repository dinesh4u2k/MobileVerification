package com.example.kavi.mobileverification;

import android.app.Notification;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class BroadcastService extends Service {

    public Integer callcount;

    public BroadcastReceiver receiver;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
//        startForeground(4756,new Notification());

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent,flags,startId);
//        onTaskRemoved(intent);
//        new CountDownTimer(100000,2000)
//        {
//            @Override
//            public void onTick(long millisUntilFinished) {
//                sendBroadcast(new Intent("fromservice"));
//
//            }
//
//            @Override
//            public void onFinish() {
//                start();
//
//            }
//        }.start();

        Log.d("service","service1 is running");


        sendBroadcast(new Intent("fromservice"));


        return START_STICKY;
    }

//    @Override
//    public void onTaskRemoved(Intent rootIntent) {
//
//        Intent bcs = new Intent(getApplicationContext(),this.getClass());
//        bcs.setPackage(getPackageName());
//        startService(bcs);
//        super.onTaskRemoved(rootIntent);
//    }
//
    @Override
    public void onDestroy() {
        super.onDestroy();

//

        sendBroadcast(new Intent("Restartservice"));
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(new Intent(this, BroadcastService.class));
        } else {
            startService(new Intent(this, BroadcastService.class));
        }


    }
}

