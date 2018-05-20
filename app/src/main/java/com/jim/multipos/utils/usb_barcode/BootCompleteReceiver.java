package com.jim.multipos.utils.usb_barcode;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootCompleteReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.wtf("BOOTEVENT","BROADCAST ONRECIVE");
        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
            Log.wtf("BOOTEVENT","BROADCAST CATCH ACTION");
            Intent pushIntent = new Intent(context, BootCompleteService.class);
            context.startService(pushIntent);
        }
    }

}