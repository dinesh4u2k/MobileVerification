package com.example.kavi.adkoin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

/**
 * Created by batman on 22/9/18.
 */

public class Restartservice extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            context.startForegroundService(new Intent(context, BroadcastService.class));
        } else {
            context.startService(new Intent(context, BroadcastService.class));
        }
    }
}
