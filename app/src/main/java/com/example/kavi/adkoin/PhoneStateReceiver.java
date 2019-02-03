package com.example.kavi.adkoin;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.api.cache.http.HttpCachePolicy;
import com.apollographql.apollo.cache.http.ApolloHttpCache;
import com.apollographql.apollo.cache.http.DiskLruHttpCacheStore;
import com.apollographql.apollo.exception.ApolloException;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import javax.annotation.Nonnull;

import okhttp3.OkHttpClient;


public class PhoneStateReceiver extends BroadcastReceiver {


    private static int lastState = TelephonyManager.CALL_STATE_IDLE;
    private static Date callStartTime;
    private static boolean isIncoming;
    private static String savedNumber;
    public ApolloClient apolloClient;
    private int Listsize=0;
    private String baaba="kkkkkkkkk";
    final ArrayList<String> imagesFromURL = new ArrayList<String>();





    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    @Override
    public void onReceive(final Context context, Intent intent) {



        Log.d("flag1 ", "flag1");

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("HH:mm");
        String strDate = mdformat.format(calendar.getTime());
        System.out.println(strDate);
        int a = calendar.get(Calendar.AM_PM);
        System.out.println(a);
        System.out.println(Calendar.PM);

        if(a == Calendar.PM) {
            if (strDate == "24:00") {
                System.out.println("12 midnight");
//                SharedPreferences spbroad = context.getSharedPreferences("cc", Context.MODE_PRIVATE);
//                final SharedPreferences.Editor editor = spbroad.edit();
//                editor.clear();
//                editor.apply();

            }
        }





        if (intent.getAction().equals("android.intent.action.NEW_OUTGOING_CALL")) {
            savedNumber = intent.getExtras().getString("android.intent.extra.PHONE_NUMBER");
            Log.d("outgoing number",savedNumber);
        }
        else {
            try {
               // String test = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
                String stateStr = intent.getExtras().getString(TelephonyManager.EXTRA_STATE);
                String number = intent.getExtras().getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
                Log.d("State",stateStr);
                Log.d("INcoming number",number);
                int state = 0;
                if (stateStr.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
                    state = TelephonyManager.CALL_STATE_IDLE;
                } else if (stateStr.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
                    state = TelephonyManager.CALL_STATE_OFFHOOK;
                } else if (stateStr.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                    state = TelephonyManager.CALL_STATE_RINGING;
                }


                onCallStateChanged(context, state, number);

            }catch (Exception e){
                e.printStackTrace();
            }


        }
    }




    //Incoming call-  goes from IDLE to RINGING when it rings, to OFFHOOK when it's answered, to IDLE when its hung up
    //Outgoing call-  goes from IDLE to OFFHOOK when it dials out, to IDLE when hung up
    public void onCallStateChanged(final Context context, int state, String number) {
        if(lastState == state){
            //No change, debounce extras
            return;
        }
        switch (state) {
            case TelephonyManager.CALL_STATE_RINGING:
                isIncoming = true;
                callStartTime = new Date();
                savedNumber = number;
//                callQuery();
                final Intent i = new Intent(context, CustomPhoneStateListener.class);

//                i.putExtra("url", baaba);

//                        i.putExtras(intent);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);


                        new android.os.Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                context.startActivity(i);
                            }
                        }, 1000);
                break;
            case TelephonyManager.CALL_STATE_OFFHOOK:
                //Transition of ringing->offhook are pickups of incoming calls.  Nothing done on them
                if(lastState != TelephonyManager.CALL_STATE_RINGING){
                    isIncoming = false;
                    callStartTime = new Date();
                   Log.d("out","outgoing call started");
                }
                else
                {
                    isIncoming = true;
                    callStartTime = new Date();
                    Log.d("incoming","incoming call started");
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                        context.startForegroundService(new Intent(context, Myservice.class));
//                    } else {
                        context.startService(new Intent(context, Myservice.class));
//                    }

                }

                break;
            case TelephonyManager.CALL_STATE_IDLE:
                if(lastState == TelephonyManager.CALL_STATE_RINGING){

                    Log.d("no","no answer");

                    final Intent i4 = new Intent(context, CustomPhoneStateListener.class);

                    i4.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    i4.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    i4.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    i4.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    i4.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);


                    new android.os.Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            context.startActivity(i4);
                        }
                    }, 1000);
                }
                else if(isIncoming){

                    Log.d("end","call ended");

                    context.stopService(new Intent(context, Myservice.class));

                    final Intent i1 = new Intent(context, CustomPhoneStateListener.class);

                    i1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    i1.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    i1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    i1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    i1.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);


                    new android.os.Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            context.startActivity(i1);
                        }
                    }, 1000);


                }
                else{
                   // onOutgoingCallEnded(context, savedNumber, callStartTime, new Date());
//                    callQuery();
                    final Intent i2 = new Intent(context, CustomPhoneStateListener.class);
//                    i2.putExtra("url", baaba);

//                        i2.putExtras(i2);
                    i2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    i2.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    i2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    i2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    i2.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);


                    new android.os.Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            context.startActivity(i2);
                        }
                    }, 1000);
                }
                break;
        }
        lastState = state;


        }

    }



