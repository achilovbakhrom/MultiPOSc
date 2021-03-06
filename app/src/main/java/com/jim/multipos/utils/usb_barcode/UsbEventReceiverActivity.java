package com.jim.multipos.utils.usb_barcode;

import android.app.Activity;
import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

public class UsbEventReceiverActivity extends Activity {
    public static final String CUSTOM_ACTION_USB_DEVICE_ATTACHED = "com.jim.multipos.utils.USB_DEVICE_ATTACHED";
    private static final String TAG= "USBServiceTAG";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        Intent intent = getIntent();


        if (intent != null)
        {
            if (intent.getAction().equals(UsbManager.ACTION_USB_DEVICE_ATTACHED))
            {
                Parcelable usbDevice = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                UsbDevice usbDevice1 = getIntent().getParcelableExtra(UsbManager.EXTRA_DEVICE);
                Log.wtf(TAG, "UsbEventReceiverActivity onResume: "+ usbDevice1.getProductId());

                // Create OnItemClickListener new intent and put the usb device in as an extra
                Intent broadcastIntent = new Intent(CUSTOM_ACTION_USB_DEVICE_ATTACHED);
                broadcastIntent.putExtra(UsbManager.EXTRA_DEVICE, usbDevice);

                // Broadcast this event so we can receive it
                sendBroadcast(broadcastIntent);
            }
        }

        // Close the activity
        finish();
    }
}
