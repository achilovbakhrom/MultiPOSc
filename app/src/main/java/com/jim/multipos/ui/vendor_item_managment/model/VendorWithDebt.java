package com.jim.multipos.ui.vendor_item_managment.model;

import com.jim.multipos.data.db.model.products.Vendor;

/**
 * Created by developer on 20.11.2017.
 */

public class VendorWithDebt {
    private Vendor vendor;
    private double debt;
    private Double getDebt(){
        return debt;
    }
}
