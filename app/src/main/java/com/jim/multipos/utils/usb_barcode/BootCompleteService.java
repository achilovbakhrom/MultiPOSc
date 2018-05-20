package com.jim.multipos.utils.usb_barcode;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.jim.multipos.utils.RxBus;
import com.jim.multipos.utils.rxevents.main_order_events.RebootedEvent;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

public class BootCompleteService extends Service {

    @Inject
    RxBus rxBus;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.wtf("BOOTEVENT","BootCompleteService onCreate");

        AndroidInjection.inject(this);
        rxBus.send(new RebootedEvent());
        stopSelf();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
