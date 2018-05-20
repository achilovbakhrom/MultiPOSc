package com.jim.multipos.utils;

import android.util.Log;

import com.jim.multipos.utils.usb_barcode.BarcodeReadEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class BarcodeStack {

    private RxBus rxBus;
    private static final String TAG = "USBServiceTAG";

    public BarcodeStack(RxBus rxBus){
        this.rxBus = rxBus;
        stackListners = new Stack<>();
        init();
    }
    public void init(){
        rxBus.toObservable().observeOn(AndroidSchedulers.mainThread()).subscribe(o->{
            if(o instanceof BarcodeReadEvent){
                Log.wtf(TAG, "USBService onUSBDataReceive rxBus listner is EMPTY?: " +stackListners.size());
                Log.wtf(TAG, "USBService onUSBDataReceive rxBus: " +((BarcodeReadEvent) o).getBarcode());

                if(!stackListners.empty()){
                    stackListners.lastElement().barcodeScaned(((BarcodeReadEvent) o).getBarcode());
                }
            }
        });
    }
    public void register(OnBarcodeReaderListner onBarcodeReaderListner){
        stackListners.push(onBarcodeReaderListner);
    }
    public void unregister(){
        stackListners.pop();
    }

    public interface OnBarcodeReaderListner{
        void barcodeScaned(String barcode);
    }
    Stack<OnBarcodeReaderListner> stackListners;

}
