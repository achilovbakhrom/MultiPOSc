package com.jim.multipos.utils.usb_barcode;

import lombok.Data;

@Data
public class BarcodeReadEvent {
    String barcode;

    public BarcodeReadEvent(String barcode) {
        this.barcode = barcode;
    }
}
