package com.example.kavi.mobileverification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.util.Log;

/**
 * Created by kavi on 12/8/18.
 */

public class PhoneStateReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(final Context context, Intent intent) {

        Log.d("flag1 ", "flag1");

        String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
        Log.d("flag2", state);
        if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
//                ) {|| state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)

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
            },1000);

        }
        if (state.equals(TelephonyManager.EXTRA_STATE_IDLE)){
            finishAffinity(CustomPhoneStateListener.class);
        }
    }

    private void finishAffinity(Class<CustomPhoneStateListener> customPhoneStateListenerClass) {
        finishAffinity(CustomPhoneStateListener.class);
    }
}
