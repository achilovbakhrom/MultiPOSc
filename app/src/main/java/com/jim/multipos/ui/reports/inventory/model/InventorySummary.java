package com.jim.multipos.ui.reports.inventory.model;

import com.jim.multipos.data.db.model.products.Product;
import com.jim.multipos.data.db.model.products.Vendor;

import lombok.Data;

@Data
public class InventorySummary {
    private Product product;
    private Vendor vendor;
    private double sold;
    private double receivedFromVendor;
    private double retrunToVendor;
    private double retrunFromCustomer;
    private double voidIncome;
    private double wasted;
}
