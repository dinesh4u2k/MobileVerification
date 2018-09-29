package com.example.kavi.mobileverification;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.Log;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class PhoneStateReceiver extends BroadcastReceiver {


    private static int lastState = TelephonyManager.CALL_STATE_IDLE;
    private static Date callStartTime;
    private static boolean isIncoming;
    private static String savedNumber;




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
                SharedPreferences spbroad = context.getSharedPreferences("cc", Context.MODE_PRIVATE);
                final SharedPreferences.Editor editor = spbroad.edit();
                editor.clear();
                editor.apply();
            }
        }



//        if (intent.getAction().equals("android.intent.action.NEW_OUTGOING_CALL")) {
//
//            String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
//
//            if (state.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
//
//                final Intent i = new Intent(context, CustomPhoneStateListener.class);
//                i.putExtras(intent);
//                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
//
//
//                new android.os.Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        context.startActivity(i);
//                    }
//                }, 1000);
//
//            }
//
//
//            Log.d("out","outgoingggggg");
//
//        }else if (!intent.getAction().equals("android.intent.action.NEW_OUTGOING_CALL")){
//            try {
//                String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
//
////        Log.e("flag2",state);
//                if (state != null) {
//                    if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
//
//                        Log.d("Ringing", "Phone is ringing");
//
//
//                        final Intent i = new Intent(context, CustomPhoneStateListener.class);
//                        i.putExtras(intent);
//                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                        i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
//
//
//                        new android.os.Handler().postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                context.startActivity(i);
//                            }
//                        }, 1000);
//
//
//                    }
//
//                    if (state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
//
//                        context.startService(new Intent(context, Myservice.class));
//                        Log.d("attend", "basjdnasjfasjfhasfjlasfklafklajfklajfklajfklasjf");
//
//                        SharedPreferences spbroad = context.getSharedPreferences("cc", Context.MODE_PRIVATE);
//                        final SharedPreferences.Editor editor = spbroad.edit();
//
//                        callcount = spbroad.getInt("count",0);
//
//                        if (callcount == 0){
//                            callcount =1;
//                            editor.putInt("count",callcount);
//                            editor.apply();
//
//                        }else if(callcount==30) {
//
//                            Log.d("cc","level reached");
//                        }else {
//
//                            callcount=callcount+1;
//                            editor.putInt("count",callcount);
//                            editor.apply();
//
//                        }
//
//
//
//                    }
//
//                    if (state.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
//
//                        context.stopService(new Intent(context, Myservice.class));

//                        final Intent i = new Intent(context, CustomPhoneStateListener.class);
//                        i.putExtras(intent);
//                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                        i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
//
//
//                        new android.os.Handler().postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                context.startActivity(i);
//                            }
//                        }, 1000);
//
//                    }
//
//                }
//                //}
//                // }
//
//
//            } catch (NullPointerException e) {
//                Log.e("null", Log.getStackTraceString(e));
//            }



        if (intent.getAction().equals("android.intent.action.NEW_OUTGOING_CALL")) {
            savedNumber = intent.getExtras().getString("android.intent.extra.PHONE_NUMBER");
            Log.d("outgoing number",savedNumber);
        }
        else {
            try {
                String test = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
                if (!test.equals(null)) {
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
                }

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
                final Intent i = new Intent(context, CustomPhoneStateListener.class);
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
                //Went to idle-  this is the end of a call.  What type depends on previous state(s)
                if(lastState == TelephonyManager.CALL_STATE_RINGING){
                    //Ring but no pickup-  a miss
                    Log.d("no","no answer");
                    final Intent i4 = new Intent(context, CustomPhoneStateListener.class);
//                        i1.putExtras(i1);
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
                    //onIncomingCallEnded(context, savedNumber, callStartTime, new Date());
                    Log.d("end","call ended");
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                        context.stopService(new Intent(context, Myservice.class));
//                    } else {
                        context.stopService(new Intent(context, Myservice.class));
//                    }
                    final Intent i1 = new Intent(context, CustomPhoneStateListener.class);
//                        i1.putExtras(i1);
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
                    final Intent i2 = new Intent(context, CustomPhoneStateListener.class);
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



