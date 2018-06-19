package com.jim.multipos.utils.rxevents.main_order_events;

import com.jim.multipos.data.db.model.Discount;


/**
 * Created by developer on 27.02.2018.
 */
public class DiscountEvent {
    private Discount discount;
    private int type;

    public DiscountEvent(Discount discount, int type) {
        this.discount = discount;
        this.type = type;
    }

    public Discount getDiscount() {
        return discount;
    }

    public void setDiscount(Discount discount) {
        this.discount = discount;
    }


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
