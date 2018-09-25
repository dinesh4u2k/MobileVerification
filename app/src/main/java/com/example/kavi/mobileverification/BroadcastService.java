package com.example.kavi.mobileverification;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.telephony.TelephonyManager;
import android.util.Log;



public class BroadcastService extends Service {

    public Notification notification;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {

        super.onCreate();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            startMyOwnForeground();
        else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O);
//            startForeground(1, new Notification());

    }





    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent,flags,startId);

        Log.d("service","service1 is running");


        sendBroadcast(new Intent("fromservice"));


        return START_STICKY;
    }
    @TargetApi(Build.VERSION_CODES.O)
    private void startMyOwnForeground(){
        String NOTIFICATION_CHANNEL_ID = "com.example.simpleapp";
        String channelName = "My Background Service";
        NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_MIN);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_SECRET);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        notification= notificationBuilder.setOngoing(false)
                .setAutoCancel(true)
                .build();
//                .setSmallIcon(R.drawable.common_google_signin_btn_icon_dark_focused)
//                .setContentTitle("App is running in background")
//                .setPriority(NotificationManager.IMPORTANCE_UNSPECIFIED)
//                .setCategory(Notification.CATEGORY_SERVICE)
//                .build();
//        notification.flags |= Notification.FLAG_NO_CLEAR;
//        notification.flags |= Notification.FLAG_AUTO_CANCEL;


        startForeground(2, notification);
        notistop();
    }

    public void notistop(){
        startForeground(2, notification);

        stopSelf();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
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

