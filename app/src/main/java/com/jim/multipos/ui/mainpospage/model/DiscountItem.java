package com.jim.multipos.ui.mainpospage.model;

import com.jim.multipos.data.db.model.Discount;
import com.jim.multipos.data.db.model.order.OrderProduct;

import lombok.Data;

/**
 * Created by developer on 27.12.2017.
 */
@Data
public class DiscountItem {
    private Discount discount;
    private double ammount;
}
