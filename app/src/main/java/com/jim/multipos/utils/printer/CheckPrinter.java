package com.jim.multipos.utils.printer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.hardware.usb.UsbDevice;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.zj.usbsdk.UsbController;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Sirojiddin on 29.01.2018.
 */

public class CheckPrinter {

    private int[][] info;
    private UsbController usbController;
    private UsbDevice device;
    private Activity parent;

    public CheckPrinter(Activity activity) {
        this.parent = activity;
        info = new int[8][2];
        initBytes();
        usbController = new UsbController(this.parent, handler);
    }

    public void connectDevice() {
        usbController.close();
        for (int i = 0; i < 8; i++) {
            device = usbController.getDev(info[i][0], info[i][1]);
            if (device != null)
                break;
        }
        if (device != null) {
            if (!(usbController.isHasPermission(device))) {
                usbController.getPermission(device);
            } else {
                Toast.makeText(parent.getBaseContext(), "Connected",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void printCheck() {
        if (usbController != null && device != null) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd/ HH:mm:ss ");
            Date curDate = new Date(System.currentTimeMillis());//获取当前时间
            String str = formatter.format(curDate);
            String date = str + "\n\n\n\n\n\n";
            try {
                byte[] qrcode = PrinterCommand.getBarCommand("Zijiang Electronic Thermal Receipt Printer!", 0, 3, 6);//
                Command.ESC_Align[2] = 0x01;
                SendDataByte(Command.ESC_Align);
                SendDataByte(qrcode);

                SendDataByte(Command.ESC_Align);
                Command.GS_ExclamationMark[2] = 0x11;
                SendDataByte(Command.GS_ExclamationMark);
                SendDataByte("NIKE Shop\n".getBytes("GBK"));
                Command.ESC_Align[2] = 0x00;
                SendDataByte(Command.ESC_Align);
                Command.GS_ExclamationMark[2] = 0x00;
                SendDataByte(Command.GS_ExclamationMark);
                SendDataByte("Number:  888888\nReceipt  S00003333\nCashier：1001\nDate：xxxx-xx-xx\nPrint Time：xxxx-xx-xx  xx:xx:xx\n".getBytes("GBK"));
                SendDataByte("Name    Quantity    price  Money\nShoes   10.00       899     8990\nBall    10.00       1599    15990\n".getBytes("GBK"));
                SendDataByte("Quantity：             20.00\ntotal：                16889.00\npayment：              17000.00\nKeep the change：      111.00\n".getBytes("GBK"));
                SendDataByte("company name：NIKE\nSite：www.xxx.xxx\naddress：ShenzhenxxAreaxxnumber\nphone number：0755-11111111\nHelpline：400-xxx-xxxx\n================================\n".getBytes("GBK"));
                Command.ESC_Align[2] = 0x01;
                SendDataByte(Command.ESC_Align);
                Command.GS_ExclamationMark[2] = 0x11;
                SendDataByte(Command.GS_ExclamationMark);
                SendDataByte("Welcome again!\n".getBytes("GBK"));
                Command.ESC_Align[2] = 0x00;
                SendDataByte(Command.ESC_Align);
                Command.GS_ExclamationMark[2] = 0x00;
                SendDataByte(Command.GS_ExclamationMark);

                SendDataByte("(The above information is for testing template, if agree, is purely coincidental!)\n".getBytes("GBK"));
                Command.ESC_Align[2] = 0x02;
                SendDataByte(Command.ESC_Align);
                SendDataString(date);
                SendDataByte(Command.GS_i);
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            Toast.makeText(parent.getBaseContext(), "Printer isn't connected",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void closeController() {
        usbController.close();
    }

    @SuppressLint("HandlerLeak")
    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UsbController.USB_CONNECTED:
                    Toast.makeText(parent.getBaseContext(), "Connected",
                            Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };

    private void initBytes() {
        info[0][0] = 0x1CBE;
        info[0][1] = 0x0003;
        info[1][0] = 0x1CB0;
        info[1][1] = 0x0003;
        info[2][0] = 0x0483;
        info[2][1] = 0x5740;
        info[3][0] = 0x0493;
        info[3][1] = 0x8760;
        info[4][0] = 0x0416;
        info[4][1] = 0x5011;
        info[5][0] = 0x0416;
        info[5][1] = 0xAABB;
        info[6][0] = 0x1659;
        info[6][1] = 0x8965;
        info[7][0] = 0x0483;
        info[7][1] = 0x5741;
    }

    private void SendDataByte(byte[] data) {
        if (data.length > 0)
            usbController.sendByte(data, device);
    }

    private void SendDataString(String data) {
        if (data.length() > 0)
            usbController.sendMsg(data, "GBK", device);
    }
}
