package com.example.kavi.mobileverification;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.util.Log;


public class PhoneStateReceiver extends BroadcastReceiver {

    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    @Override
    public void onReceive(final Context context, Intent intent) {

        Log.d("flag1 ", "flag1");
        try {
            String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);

//        Log.e("flag2",state);
            if (state !=null) {
                if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {

                    Log.d("Ringing", "Phone is ringing");

                    final Intent i = new Intent(context, CustomPhoneStateListener.class);
                    i.putExtras(intent);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

                    new android.os.Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            context.startActivity(i);
                        }
                    }, 1000);

                }

                if (state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
                    Log.d("attend", "basjdnasjfasjfhasfjlasfklafklajfklajfklajfklasjf");

                    context.startService(new Intent(context, Myservice.class));
                }

                if (state.equals(TelephonyManager.EXTRA_STATE_IDLE)){
                    context.stopService(new Intent(context, Myservice.class));
                }
            }


        } catch (NullPointerException e) {
            Log.e("null", Log.getStackTraceString(e));
        }

    }
}


