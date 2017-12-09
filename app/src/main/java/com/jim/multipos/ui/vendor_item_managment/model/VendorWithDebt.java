package com.jim.multipos.ui.vendor_item_managment.model;

import com.jim.multipos.data.db.model.products.Vendor;

import lombok.Data;

/**
 * Created by developer on 20.11.2017.
 */
@Data
public class VendorWithDebt {
    private Vendor vendor;
    private double debt;
    public Double getDebt(){
        return debt;
    }
}