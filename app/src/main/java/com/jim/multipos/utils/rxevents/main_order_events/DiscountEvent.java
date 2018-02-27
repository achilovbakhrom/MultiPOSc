package com.jim.multipos.utils.rxevents.main_order_events;

import com.jim.multipos.data.db.model.Discount;

import lombok.Data;

/**
 * Created by developer on 27.02.2018.
 */
@Data
public class DiscountEvent {
    private Discount discount;
    private Discount newDiscount;
    private int type;
}
