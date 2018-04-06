package com.jim.multipos.ui.reports.product_profit.data;

import lombok.Data;

@Data
public class ProductLog {
    String name;
    long date;
    long orderId;
    double qty;
    double costEach;
    double priceEach;
    String discription;
    public double getTotal(){
        return qty*priceEach;
    }
}
