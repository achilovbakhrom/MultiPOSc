package com.jim.multipos.utils.usb_barcode;


public class BarcodeReadEvent {
    String barcode;

    public BarcodeReadEvent(String barcode) {
        this.barcode = barcode;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }
}
