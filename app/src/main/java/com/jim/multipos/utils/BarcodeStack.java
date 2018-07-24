package com.jim.multipos.utils;

import com.jim.multipos.utils.usb_barcode.BarcodeReadEvent;

import java.util.Stack;

import io.reactivex.android.schedulers.AndroidSchedulers;

/** Created by developer
 *  ------------------
 *  This class responsible for work Barcode scanner Input sending to Fragments which inject it
 *  It works as Stack, Last listener will gets BarcodeReadEvent
 * */

public class BarcodeStack {

    private RxBus rxBus;
    private static final String TAG = "USBServiceTAG";

    public BarcodeStack(RxBus rxBus) {
        this.rxBus = rxBus;
        stackListners = new Stack<>();
        init();
    }


    public void init() {
        rxBus.toObservable().observeOn(AndroidSchedulers.mainThread()).subscribe(o -> {
            if (o instanceof BarcodeReadEvent) {
                if (!stackListners.empty()) {
                    stackListners.lastElement().barcodeScaned(((BarcodeReadEvent) o).getBarcode());
                }
            }
        });
    }

    public void register(OnBarcodeReaderListner onBarcodeReaderListner) {
        stackListners.push(onBarcodeReaderListner);
    }

    public void unregister() {
        stackListners.pop();
    }

    public interface OnBarcodeReaderListner {
        void barcodeScaned(String barcode);
    }

    Stack<OnBarcodeReaderListner> stackListners;

}
