package com.jim.multipos.utils.printer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.usb.UsbDevice;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.jim.multipos.config.common.BaseAppModule;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.customer.Customer;
import com.jim.multipos.data.db.model.order.Order;
import com.jim.multipos.data.db.model.order.OrderProduct;
import com.jim.multipos.data.prefs.PreferencesHelper;
import com.jim.multipos.ui.mainpospage.model.OrderProductItem;
import com.jim.multipos.utils.CyrillicLatinConverter;
import com.zj.usbsdk.UsbController;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.jim.multipos.R;

/**
 * Created by Sirojiddin on 29.01.2018.
 */

public class CheckPrinter {
    public static final int PRINETER_58mm_WIDTH = 384;
    private int[][] info;
    private UsbController usbController;
    private UsbDevice device;
    private Activity parent;
    private PreferencesHelper preferencesHelper;
    private DatabaseManager databaseManager;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
    private DecimalFormat decimalFormat;
    private DecimalFormat decimalFormatWeight;
    public CheckPrinter(Activity activity, PreferencesHelper preferencesHelper, DatabaseManager databaseManager) {
        this.parent = activity;
        this.preferencesHelper = preferencesHelper;
        this.databaseManager = databaseManager;
        info = new int[8][2];
        initBytes();
        usbController = new UsbController(this.parent, handler);
        decimalFormat = BaseAppModule.getFormatterGroupingPattern("#,##0.00");
        decimalFormatWeight = BaseAppModule.getFormatterWithoutGroupingPattern("0.###");
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
//                Toast.makeText(parent.getBaseContext(), "Connected",
//                        Toast.LENGTH_SHORT).show();
            }
        }
    }
    public boolean checkConnect(){
        return (usbController != null && device != null);
    }

    public void stockChek(long tillId, long orderNumber, long now, List<OrderProductItem> orderProducts, Customer customer)  {
//        if (usbController != null && device != null) {
            sendDataByte( Command.ESC_Init);
            sendDataByte(PrinterCommand.POS_Set_CodePage(73));


            //Change Type font to bigger
            Command.ESC_ExclamationMark[2] = 0x00;
            sendDataByte( Command.ESC_ExclamationMark);


            //Align to center
            Command.ESC_Align[2] = 0x01;
            sendDataByte(Command.ESC_Align);

            //Line interval (short)
            sendDataByte( Command.ESC_Three);

            //BOLD
            sendDataByte(PrinterCommand.POS_Set_Bold(1));

            //ORGANIZATION NAME
            sendDataString(String.format("%.32s",preferencesHelper.getPosDetailAlias().toUpperCase()));

            //Change type font to smaller
            Command.ESC_ExclamationMark[2] = 0x01;
            sendDataByte( Command.ESC_ExclamationMark);

            //Turn off bold style
            sendDataByte(PrinterCommand.POS_Set_Bold(0));

            //Organization Adress
            sendDataString( String.format("%.42s", CyrillicLatinConverter.transliterate(preferencesHelper.getPosDetailAddress())));



            //Organization PhoneNumber
            if(!preferencesHelper.getPosPhoneNumber().isEmpty()){
                //Line interval (shorter)
                sendDataByte( Command.ESC_Three);
                sendDataString(String.format("%.42s", parent.getString(com.jim.multipos.R.string.tel)+" " +CyrillicLatinConverter.transliterate(preferencesHelper.getPosPhoneNumber())));
                sendDataByte( Command.ESC_Two);
            }

            //enter
            sendDataString(" ");

            //Line interval (longer)
            sendDataByte( Command.ESC_Two);

            //Align right
            Command.ESC_Align[2] = 0x00;
            sendDataByte(Command.ESC_Align);

            //ORDER №: 12         17:50 27/02/2017
            sendDataString(String.format("%-20.20s",parent.getString(R.string.order_number_title)+": "+String.valueOf(orderNumber))+"  "+String.format("%20.20s",simpleDateFormat.format(now)));

            //POS ID: 1            Till №: 1
            sendDataString(String.format("%-20.20s",parent.getString(R.string.pos_number)+": "+String.valueOf(preferencesHelper.getPosDetailPosId()))+"  "+String.format("%20.20s",parent.getString(R.string.till_number_titl)+": "+String.valueOf(tillId)));

            //Customer: Anvarjon   Currency: Uzs
            sendDataString(String.format("%-20.20s",parent.getString(R.string.customer)+": "+CyrillicLatinConverter.transliterate((customer==null?"-":customer.getName())))+"  "+String.format("%20.20s",parent.getString(R.string.currency) +": " + databaseManager.getMainCurrency().getAbbr()));


            //Divider
            sendDataString("..........................................");

            double subtotal = 0;

            //ALL PRODUCTS WITH PRICE
            for (int i = 0; i < orderProducts.size(); i++) {
                if(orderProducts.get(i).getOrderProduct().getCount() == 1 ){
                    //Coca Cola 1.5L          |         12 000.00|
                    sendDataString(String.format("%-24.24s",CyrillicLatinConverter.transliterate(orderProducts.get(i).getOrderProduct().getProduct().getName()))+String.format("%18s",decimalFormat.format(orderProducts.get(i).getOrderProduct().getPrice())));
                    subtotal += orderProducts.get(i).getOrderProduct().getPrice();
                }else {
                    //Coca Cola 1.5L    |    0.800x18952=12 000.00|
                    sendDataString(String.format("%-18.18s", CyrillicLatinConverter.transliterate(orderProducts.get(i).getOrderProduct().getProduct().getName()))+String.format("%24s",decimalFormatWeight.format(orderProducts.get(i).getOrderProduct().getCount())+"x"+decimalFormatWeight.format(orderProducts.get(i).getOrderProduct().getPrice())+" = "+decimalFormat.format(orderProducts.get(i).getOrderProduct().getPrice()*orderProducts.get(i).getOrderProduct().getCount())));
                    subtotal += orderProducts.get(i).getOrderProduct().getPrice()*orderProducts.get(i).getOrderProduct().getCount();

                }
            }

            //Divider
            sendDataString("..........................................");

            //BOLD
            sendDataByte(PrinterCommand.POS_Set_Bold(0));



            //FOR PAY
            sendDataString(String.format("%-24.24s",parent.getString(R.string.subtotalvalue)+": ") + String.format("%18s",decimalFormat.format(subtotal)));

            //Align to center
            Command.ESC_Align[2] = 0x01;
            sendDataByte(Command.ESC_Align);



            sendDataString(" ");
            sendDataString(" ");
            sendDataString(" ");





//        } else {
//            Toast.makeText(parent.getBaseContext(), "Printer isn't connected",
//                    Toast.LENGTH_SHORT).show();
//        }
    }
    public void examplePrint(){
        if (usbController != null && device != null) {
            sendDataByte( Command.ESC_Init);
            sendDataByte(PrinterCommand.POS_Set_CodePage(73));


            //Align to center
            Command.ESC_Align[2] = 0x01;
            sendDataByte(Command.ESC_Align);

            //print Picture to check
            if(preferencesHelper.isPrintPictureInCheck()){
                try {
                    Bitmap bitmap;
                    if(!preferencesHelper.isDefaultPicture())
                        bitmap = MediaStore.Images.Media.getBitmap(parent.getContentResolver(), preferencesHelper.getUriPathCheckPicture());
                    else bitmap = BitmapFactory.decodeResource(parent.getResources(), R.drawable.multipos);
                    printPicture(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }



            //Change Type font to bigger
            Command.ESC_ExclamationMark[2] = 0x00;
            sendDataByte( Command.ESC_ExclamationMark);


            //Align to center
            Command.ESC_Align[2] = 0x01;
            sendDataByte(Command.ESC_Align);

            //Line interval (short)
            sendDataByte( Command.ESC_Three);

            //BOLD
            sendDataByte(PrinterCommand.POS_Set_Bold(1));

            //ORGANIZATION NAME
            sendDataString(String.format("%.32s",CyrillicLatinConverter.transliterate(preferencesHelper.getPosDetailAlias().toUpperCase())));

            //Change type font to smaller
            Command.ESC_ExclamationMark[2] = 0x01;
            sendDataByte( Command.ESC_ExclamationMark);

            //Turn off bold style
            sendDataByte(PrinterCommand.POS_Set_Bold(0));

            //Organization Adress
            sendDataString( String.format("%.42s", CyrillicLatinConverter.transliterate(preferencesHelper.getPosDetailAddress())));



            //Organization PhoneNumber
            if(!preferencesHelper.getPosPhoneNumber().isEmpty()){
                //Line interval (shorter)
                sendDataByte( Command.ESC_Three);
                sendDataString(String.format("%.42s", parent.getString(com.jim.multipos.R.string.tel)+" " +CyrillicLatinConverter.transliterate(preferencesHelper.getPosPhoneNumber())));
                sendDataByte( Command.ESC_Two);
            }


            //enter
            sendDataString(" ");

            //Line interval (longer)
            sendDataByte( Command.ESC_Two);

            //Align right
            Command.ESC_Align[2] = 0x00;
            sendDataByte(Command.ESC_Align);

            //ORDER №: 12         17:50 27/02/2017
            sendDataString(String.format("%-20.20s",parent.getString(R.string.order_number_title)+": "+String.valueOf(1))+"  "+String.format("%20.20s",simpleDateFormat.format(new Date())));

            //POS ID: 1            Till №: 17:50 27w/02/2017
            sendDataString(String.format("%-20.20s",parent.getString(R.string.pos_number)+": "+String.valueOf(preferencesHelper.getPosDetailPosId()))+"  "+String.format("%20.20s",parent.getString(R.string.till_number_titl)+": "+String.valueOf(1)));

            //Customer: Anvarjon   Currency: Uzs
            sendDataString(String.format("%-20.20s",parent.getString(R.string.customer)+": "+CyrillicLatinConverter.transliterate("Oliver"))+"  "+String.format("%20.20s",parent.getString(R.string.currency) +": " + "$"));


            //Divider
            sendDataString("..........................................");

            sendDataString(String.format("%-24.24s",CyrillicLatinConverter.transliterate("Hoodie Sweatshirt"))+String.format("%18s","51.94"));
            sendDataString(String.format("%-24.24s",CyrillicLatinConverter.transliterate("*Paul Evans Wholecut"))+String.format("%18s","2 x 399 = 798.00"));




            //Divider
            sendDataString("..........................................");

            //BOLD
            sendDataByte(PrinterCommand.POS_Set_Bold(0));


            //DISCOUNTS
                sendDataString(String.format("%-24.24s",parent.getString(R.string.total_discount_check)+": ") + String.format("%18s",decimalFormat.format(48)));

            //SERVICE FEE
                sendDataString(String.format("%-24.24s",parent.getString(R.string.total_service_fee_check)+": ") + String.format("%18s","+"+decimalFormat.format(0)));

            //FOR PAY
            sendDataString(String.format("%-24.24s",parent.getString(R.string.to_pay_check)+": ") + String.format("%18s",decimalFormat.format(801.94)));

            //PAYED
            sendDataString(String.format("%-24.24s",parent.getString(R.string.payed_check)+": ") + String.format("%18s",decimalFormat.format(802)));

            //CHANGE
            sendDataString(String.format("%-24.24s",parent.getString(R.string.change_check)+": ") + String.format("%18s", decimalFormat.format(0.06)));

            //Align to center
            Command.ESC_Align[2] = 0x01;
            sendDataByte(Command.ESC_Align);

            //Divider
            sendDataString("..........................................");


            //Turn off bold style
            sendDataByte(PrinterCommand.POS_Set_Bold(0));

            if(preferencesHelper.isHintAbout()) {
                //discription
                sendDataString("* - products with discount");
                sendDataString("! - products with service fee");

                //Divider
                sendDataString("..........................................");
            }
            //FIN
            sendDataString("THANK YOU! - SPASIBO!");

            sendDataString(" ");
            sendDataString(" ");
            sendDataString(" ");





        } else {
            Toast.makeText(parent.getBaseContext(), "Printer isn't connected",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void printCheck(Order order,boolean reprint)  {
        if (usbController != null && device != null) {
            sendDataByte( Command.ESC_Init);
            sendDataByte(PrinterCommand.POS_Set_CodePage(73));


            //Align to center
            Command.ESC_Align[2] = 0x01;
            sendDataByte(Command.ESC_Align);

            //print Picture to check
            if(preferencesHelper.isPrintPictureInCheck()){
                try {
                    Bitmap bitmap;
                    if(!preferencesHelper.isDefaultPicture())
                            bitmap = MediaStore.Images.Media.getBitmap(parent.getContentResolver(), preferencesHelper.getUriPathCheckPicture());
                    else bitmap = BitmapFactory.decodeResource(parent.getResources(), R.drawable.multipos);
                    printPicture(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }



            //Change Type font to bigger
            Command.ESC_ExclamationMark[2] = 0x00;
            sendDataByte( Command.ESC_ExclamationMark);


            //Align to center
            Command.ESC_Align[2] = 0x01;
            sendDataByte(Command.ESC_Align);

            //Line interval (short)
            sendDataByte( Command.ESC_Three);

            //BOLD
            sendDataByte(PrinterCommand.POS_Set_Bold(1));

            //ORGANIZATION NAME
            sendDataString(String.format("%.32s",preferencesHelper.getPosDetailAlias().toUpperCase()));

            //Change type font to smaller
            Command.ESC_ExclamationMark[2] = 0x01;
            sendDataByte( Command.ESC_ExclamationMark);

            //Turn off bold style
            sendDataByte(PrinterCommand.POS_Set_Bold(0));

            //Organization Adress
            sendDataString( String.format("%.42s", CyrillicLatinConverter.transliterate(preferencesHelper.getPosDetailAddress())));



            //Organization PhoneNumber
            if(!preferencesHelper.getPosPhoneNumber().isEmpty()){
                //Line interval (shorter)
                sendDataByte( Command.ESC_Three);
                sendDataString(String.format("%.42s", parent.getString(com.jim.multipos.R.string.tel)+" " +CyrillicLatinConverter.transliterate(preferencesHelper.getPosPhoneNumber())));
                sendDataByte( Command.ESC_Two);
            }

            if(reprint)
                sendDataString(String.format("%.42s", "Reprinted: "+simpleDateFormat.format(new Date())));

            //enter
            sendDataString(" ");

            //Line interval (longer)
            sendDataByte( Command.ESC_Two);

            //Align right
            Command.ESC_Align[2] = 0x00;
            sendDataByte(Command.ESC_Align);

            //ORDER №: 12         17:50 27/02/2017
            sendDataString(String.format("%-20.20s",parent.getString(R.string.order_number_title)+": "+String.valueOf(order.getId()))+"  "+String.format("%20.20s",simpleDateFormat.format(order.getCreateAt())));

            //POS ID: 1            Till №: 17:50 27w/02/2017
            sendDataString(String.format("%-20.20s",parent.getString(R.string.pos_number)+": "+String.valueOf(preferencesHelper.getPosDetailPosId()))+"  "+String.format("%20.20s",parent.getString(R.string.till_number_titl)+": "+String.valueOf(order.getTillId())));

            //Customer: Anvarjon   Currency: Uzs
            sendDataString(String.format("%-20.20s",parent.getString(R.string.customer)+": "+CyrillicLatinConverter.transliterate((order.getCustomer()==null?"-":order.getCustomer().getName())))+"  "+String.format("%20.20s",parent.getString(R.string.currency) +": " + databaseManager.getMainCurrency().getAbbr()));


            //Divider
            sendDataString("..........................................");


            //ALL PRODUCTS WITH PRICE
            for (int i = 0; i < order.getOrderProducts().size(); i++) {
                if(order.getOrderProducts().get(i).getCount() == 1 ){
                    //Coca Cola 1.5L          |         12 000.00|
                    sendDataString(String.format("%-24.24s",(order.getOrderProducts().get(i).getDiscount()==null?"":"*") +(order.getOrderProducts().get(i).getServiceFee()==null?"":"!") +CyrillicLatinConverter.transliterate(order.getOrderProducts().get(i).getProduct().getName()))+String.format("%18s",decimalFormat.format(order.getOrderProducts().get(i).getPrice())));
                }else {
                    //Coca Cola 1.5L    |    0.800x18952=12 000.00|
                    sendDataString(String.format("%-18.18s",(order.getOrderProducts().get(i).getDiscount()==null?"":"*") +(order.getOrderProducts().get(i).getServiceFee()==null?"":"!")+  CyrillicLatinConverter.transliterate(order.getOrderProducts().get(i).getProduct().getName()))+String.format("%24s",decimalFormatWeight.format(order.getOrderProducts().get(i).getCount())+"x"+decimalFormatWeight.format(order.getOrderProducts().get(i).getPrice())+" = "+decimalFormat.format(order.getOrderProducts().get(i).getPrice()*order.getOrderProducts().get(i).getCount())));
                }
            }

            //Divider
            sendDataString("..........................................");

            //BOLD
            sendDataByte(PrinterCommand.POS_Set_Bold(0));


            //DISCOUNTS
            if(order.getDiscountTotalValue()!=0)
            sendDataString(String.format("%-24.24s",parent.getString(R.string.total_discount_check)+": ") + String.format("%18s",decimalFormat.format(order.getDiscountTotalValue())));

            //SERVICE FEE
            if(order.getServiceTotalValue()!=0)
                sendDataString(String.format("%-24.24s",parent.getString(R.string.total_service_fee_check)+": ") + String.format("%18s","+"+decimalFormat.format(order.getServiceTotalValue())));

            //FOR PAY
            sendDataString(String.format("%-24.24s",parent.getString(R.string.to_pay_check)+": ") + String.format("%18s",decimalFormat.format(order.getForPayAmmount())));

            //PAYED
            sendDataString(String.format("%-24.24s",parent.getString(R.string.payed_check)+": ") + String.format("%18s",decimalFormat.format(order.getTotalPayed())));

            //CHANGE
            sendDataString(String.format("%-24.24s",parent.getString(R.string.change_check)+": ") + String.format("%18s", decimalFormat.format(order.getChange())));

            //Align to center
            Command.ESC_Align[2] = 0x01;
            sendDataByte(Command.ESC_Align);

            //Divider
            sendDataString("..........................................");


            //Turn off bold style
            sendDataByte(PrinterCommand.POS_Set_Bold(0));

            if(preferencesHelper.isHintAbout()) {
                //discription
                sendDataString("* - products with discount");
                sendDataString("! - products with service fee");

                //Divider
                sendDataString("..........................................");
            }
            //FIN
            sendDataString("THANK YOU! - SPASIBO!");

            sendDataString(" ");
            sendDataString(" ");
            sendDataString(" ");





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

    private void sendDataByte(byte[] data) {
        try {
            if (data.length > 0)
                usbController.sendByte(data, device);
        }catch (Exception e){
        }
    }

    private void sendDataString(String data) {
        if (data.length() > 0)
            usbController.sendMsg(data, "Cp1251", device);
    }
    public void printPicture(Bitmap bitmap){
        byte[] logo = PrintPicture.POS_PrintBMP(bitmap,PRINETER_58mm_WIDTH,0);
        sendDataByte(logo);
    }
}
