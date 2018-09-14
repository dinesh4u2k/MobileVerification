package com.example.kavi.mobileverification;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.TelephonyManager;
import android.util.Log;
import java.text.SimpleDateFormat;
import java.util.Calendar;



public class PhoneStateReceiver extends BroadcastReceiver {


   public Integer callcount;

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
            if (strDate == "15:25") {
                SharedPreferences spbroad = context.getSharedPreferences("cc", Context.MODE_PRIVATE);
                final SharedPreferences.Editor editor = spbroad.edit();
                editor.clear();
                editor.apply();
            }
        }

        if (intent.getAction().equals("android.intent.action.NEW_OUTGOING_CALL")) {

            Log.d("out","outgoingggggg");

        }else {
            try {
                String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);

//        Log.e("flag2",state);
                if (state != null) {
                    if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {

                        Log.d("Ringing", "Phone is ringing");


                        final Intent i = new Intent(context, CustomPhoneStateListener.class);
                        i.putExtras(intent);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);


                    }

                    if (state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
                        Log.d("attend", "basjdnasjfasjfhasfjlasfklafklajfklajfklajfklasjf");

                        SharedPreferences spbroad = context.getSharedPreferences("cc", Context.MODE_PRIVATE);
                        final SharedPreferences.Editor editor = spbroad.edit();

                        callcount = spbroad.getInt("count",0);

                        if (callcount == 0){
                            callcount =1;
                            editor.putInt("count",callcount);
                            editor.apply();

                        }else if(callcount==30) {

                            Log.d("cc","level reached");
                        }else {

                            callcount=callcount+1;
                            editor.putInt("count",callcount);
                            editor.apply();

                        }

                        context.startService(new Intent(context, Myservice.class));

                    }

                    if (state.equals(TelephonyManager.EXTRA_STATE_IDLE)) {

                        context.stopService(new Intent(context, Myservice.class));

                        final Intent i = new Intent(context, CustomPhoneStateListener.class);
                        i.putExtras(intent);
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

                    }

                }
                //}
                // }


            } catch (NullPointerException e) {
                Log.e("null", Log.getStackTraceString(e));
            }

        }

    }
}


