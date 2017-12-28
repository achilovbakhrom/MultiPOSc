package com.jim.multipos.ui.mainpospage.model;

import com.jim.multipos.data.db.model.Discount;
import com.jim.multipos.data.db.model.ServiceFee;
import com.jim.multipos.data.db.model.order.OrderProduct;

import lombok.Data;

/**
 * Created by developer on 27.12.2017.
 */
@Data
public class OrderProductItem {
    private OrderProduct orderProduct;
    private Discount discount;
    private double discountAmmount;
    private ServiceFee serviceFee;
    private double serviceFeeAmmount;

}
