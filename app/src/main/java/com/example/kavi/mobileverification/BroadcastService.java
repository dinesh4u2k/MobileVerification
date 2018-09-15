package com.example.kavi.mobileverification;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.annotation.Nullable;



public class BroadcastService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        onTaskRemoved(intent);
        new CountDownTimer(100000,2000)
        {
            @Override
            public void onTick(long millisUntilFinished) {
                sendBroadcast(new Intent("fromservice"));

            }

            @Override
            public void onFinish() {
                start();

            }
        }.start();
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
}

