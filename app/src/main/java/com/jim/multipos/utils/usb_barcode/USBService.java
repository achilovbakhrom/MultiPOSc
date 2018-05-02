package com.jim.multipos.utils.usb_barcode;

import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbConstants;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.jim.multipos.R;
import com.jim.multipos.utils.RxBus;
import com.jim.multipos.utils.rxevents.main_order_events.ProductEvent;

import javax.inject.Inject;

import dagger.android.AndroidInjection;


public class USBService extends Service {

    @Inject
    RxBus rxBus;


    //for logging
    private static final String TAG = "USBServiceTAG";

    //Thread for reading input
    private USBThreadDataReceiver usbThreadDataReceiver;

    //USB MANAGING
    private UsbManager mUsbManager;
    private UsbInterface intf;
    private UsbEndpoint endPointRead;
    private UsbDeviceConnection connection;
    private UsbDevice device;

    //Intents
    private IntentFilter filter;
    private PendingIntent mPermissionIntent;

    //EventBus
//    protected EventBus eventBus = EventBus.getDefault();

    private final Handler handler = new Handler();
    private int packetSize;


    public USBService(){

    }

    @Override
    public void onCreate() {
        super.onCreate();
        AndroidInjection.inject(this);
        Log.d(TAG, "onCreate: ");
        //init
        mUsbManager = (UsbManager) getSystemService(Context.USB_SERVICE);
        mPermissionIntent = PendingIntent.getBroadcast(this, 0, new Intent(Consts.ACTION_USB_PERMISSION), 0);

        filter = new IntentFilter(Consts.ACTION_USB_PERMISSION);
        filter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
        filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
        registerReceiver(mUsbReceiver, filter);
//        eventBus.register(this);
        findInitScanner();

    }


    private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            device = (UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
            //GOT PERMISSION
            if (Consts.ACTION_USB_PERMISSION.equals(action)) {
                if(deviceChecker(device))
                    setDevice(intent);
            }
            //BARCODE INSERTED
            if (UsbManager.ACTION_USB_DEVICE_ATTACHED.equals(action)) {
                if(deviceChecker(device))
                    setDevice(intent);
            }
            //BARCODE DISCONNECT
            if (UsbManager.ACTION_USB_DEVICE_DETACHED.equals(action)) {
                if (deviceChecker(device)) {
                    device = null;
                    if (usbThreadDataReceiver != null) {
                        usbThreadDataReceiver.stopThis();
                    }
//                    eventBus.post(new DeviceDetachedEvent());
                }
            }
        }

        private void setDevice(Intent intent) {
            device = (UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
            if (device != null && intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {

                //DEVICE SELECTED
                connection = mUsbManager.openDevice(device);
                intf = device.getInterface(0);
                if (null == connection) {
                    // mLog("(unable to establish connection)\n");
                } else {
                    connection.claimInterface(intf, true);
                }
                try {
                    if (UsbConstants.USB_DIR_IN == intf.getEndpoint(0).getDirection()) {
                        endPointRead = intf.getEndpoint(0);
                        packetSize = endPointRead.getMaxPacketSize();
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Device have no endPointRead", e);
                }
                usbThreadDataReceiver = new USBThreadDataReceiver();
                usbThreadDataReceiver.start();
//                eventBus.post(new DeviceAttachedEvent());
            }else if(device != null){
                mUsbManager.requestPermission(device, mPermissionIntent);
            }
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private class USBThreadDataReceiver extends Thread {

        private volatile boolean isStopped;


        @Override
        public void run() {
            try {
                if (connection != null && endPointRead != null) {
                    while (!isStopped) {
                        final byte[] buffer = new byte[packetSize];
                        final int status = connection.bulkTransfer(endPointRead, buffer, packetSize, 100);
                        if (status > 0) {
                            handler.post(() -> onUSBDataReceive(buffer));

                        }
                    }
                }
            } catch (Exception e) {
                Log.e(TAG, "Error in receive thread", e);
            }
        }

        public void stopThis() {
            isStopped = true;
        }
    }

    public void findInitScanner(){
        for (UsbDevice usbDevice : mUsbManager.getDeviceList().values()) {
            if(deviceChecker(usbDevice)){
                mUsbManager.requestPermission(usbDevice, mPermissionIntent);
            }
        }
    }

    StringBuilder stringBuilder = new StringBuilder("");

    public void onUSBDataReceive(byte[] buffer) {
        String msg = fromKeyCode(buffer[0], buffer[2]);
        if(msg.equals("\n")){
            rxBus.send(new BarcodeReadEvent(stringBuilder.toString()));
            stringBuilder = new StringBuilder("");
        }else {
            stringBuilder.append(msg);
        }
    }

    public static String fromKeyCode(byte modifier, byte code){
        switch (code){
            case 0x04:
                return (modifier==2||modifier==32)?"A":"a";
            case 0x05:
                return (modifier==2||modifier==32)?"B":"b";
            case 0x06:
                return (modifier==2||modifier==32)?"C":"c";
            case 0x07:
                return (modifier==2||modifier==32)?"D":"d";
            case 0x08:
                return (modifier==2||modifier==32)?"E":"e";
            case 0x09:
                return (modifier==2||modifier==32)?"F":"f";
            case 0x0a:
                return (modifier==2||modifier==32)?"G":"g";
            case 0x0b:
                return (modifier==2||modifier==32)?"H":"h";
            case 0x0c:
                return (modifier==2||modifier==32)?"I":"i";
            case 0x0d:
                return (modifier==2||modifier==32)?"J":"j";
            case 0x0e:
                return (modifier==2||modifier==32)?"K":"k";
            case 0x0f:
                return (modifier==2||modifier==32)?"L":"l";
            case 0x10:
                return (modifier==2||modifier==32)?"M":"m";
            case 0x11:
                return (modifier==2||modifier==32)?"N":"n";
            case 0x12:
                return (modifier==2||modifier==32)?"O":"o";
            case 0x13:
                return (modifier==2||modifier==32)?"P":"p";
            case 0x14:
                return (modifier==2||modifier==32)?"Q":"q";
            case 0x15:
                return (modifier==2||modifier==32)?"R":"r";
            case 0x16:
                return (modifier==2||modifier==32)?"S":"s";
            case 0x17:
                return (modifier==2||modifier==32)?"T":"t";
            case 0x18:
                return (modifier==2||modifier==32)?"U":"u";
            case 0x19:
                return (modifier==2||modifier==32)?"V":"v";
            case 0x1a:
                return (modifier==2||modifier==32)?"W":"w";
            case 0x1b:
                return (modifier==2||modifier==32)?"X":"x";
            case 0x1c:
                return (modifier==2||modifier==32)?"Y":"y";
            case 0x1d:
                return (modifier==2||modifier==32)?"Z":"z";
            case 0x1e:
                return (modifier==2||modifier==32)?"!":"1";
            case 0x1f:
                return (modifier==2||modifier==32)?"@":"2";
            case 0x20:
                return (modifier==2||modifier==32)?"#":"3";
            case 0x21:
                return (modifier==2||modifier==32)?"$":"4";
            case 0x22:
                return (modifier==2||modifier==32)?"%":"5";
            case 0x23:
                return (modifier==2||modifier==32)?"^":"6";
            case 0x24:
                return (modifier==2||modifier==32)?"&":"7";
            case 0x25:
                return (modifier==2||modifier==32)?"*":"8";
            case 0x26:
                return (modifier==2||modifier==32)?"(":"9";
            case 0x27:
                return (modifier==2||modifier==32)?")":"0";
            case 0x28: // LF
                return "\n";
            case 0x2a: // BS
                return "\b";
            case 0x2b: // TAB
                return "\t";
            case 0x2c:
                return " ";
            case 0x2d:
                return (modifier==2||modifier==32)?"_":"-";
            case 0x2e:
                return (modifier==2||modifier==32)?"+":"=";
            case 0x2f:
                return (modifier==2||modifier==32)?"{":"[";
            case 0x30:
                return (modifier==2||modifier==32)?"}":"]";
            case 0x31:
                return (modifier==2||modifier==32)?"|":"\\";
            case 0x33:
                return (modifier==2||modifier==32)?":":";";
            case 0x34:
                return (modifier==2||modifier==32)?"\"":"'";
            case 0x35:
                return (modifier==2||modifier==32)?"~":"`";
            case 0x36:
                return (modifier==2||modifier==32)?"<":",";
            case 0x37:
                return (modifier==2||modifier==32)?">":".";
            case 0x38:
                return (modifier==2||modifier==32)?"?":"/";
            default:
                return "";

        }
    }
    private boolean deviceChecker(UsbDevice usbDevice){
        if(usbDevice==null && usbDevice.getProductName()==null) return false;
        String[] productNames = getResources().getStringArray(R.array.barcode_device_names);
        for (String productName:productNames){
            if(productName.equals(usbDevice.getProductName()))
                return true;
        }
        return false;
    }
}
