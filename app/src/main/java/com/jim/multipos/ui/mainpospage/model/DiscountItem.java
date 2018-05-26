package com.jim.multipos.ui.mainpospage.model;

import com.jim.multipos.data.db.model.Discount;
import com.jim.multipos.data.db.model.order.OrderProduct;


/**
 * Created by developer on 27.12.2017.
 */
public class DiscountItem {
    private Discount discount;
    private double ammount;

    public Discount getDiscount() {
        return discount;
    }

    public void setDiscount(Discount discount) {
        this.discount = discount;
    }

    public double getAmmount() {
        return ammount;
    }

    public void setAmmount(double ammount) {
        this.ammount = ammount;
    }
}
