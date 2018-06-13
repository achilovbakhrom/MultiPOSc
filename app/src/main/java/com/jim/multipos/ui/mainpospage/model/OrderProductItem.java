package com.jim.multipos.ui.mainpospage.model;

import com.jim.multipos.data.db.model.Discount;
import com.jim.multipos.data.db.model.ServiceFee;
import com.jim.multipos.data.db.model.inventory.OutcomeProduct;
import com.jim.multipos.data.db.model.order.OrderProduct;


/**
 * Created by developer on 27.12.2017.
 */
public class OrderProductItem {
    private OrderProduct orderProduct;
    private Discount discount;
    private double discountAmmount;
    private ServiceFee serviceFee;
    private double serviceFeeAmmount;
    private OutcomeProduct outcomeProduct;

    private boolean haveInStock = true;

    public OutcomeProduct getOutcomeProduct() {
        return outcomeProduct;
    }

    public void setOutcomeProduct(OutcomeProduct outcomeProduct) {
        this.outcomeProduct = outcomeProduct;
    }

    public boolean isHaveInStock() {
        return haveInStock;
    }

    public void setHaveInStock(boolean haveInStock) {
        this.haveInStock = haveInStock;
    }

    public OrderProduct getOrderProduct() {
        return orderProduct;
    }

    public void setOrderProduct(OrderProduct orderProduct) {
        this.orderProduct = orderProduct;
    }

    public Discount getDiscount() {
        return discount;
    }

    public void setDiscount(Discount discount) {
        this.discount = discount;
    }

    public double getDiscountAmmount() {
        return discountAmmount;
    }

    public void setDiscountAmmount(double discountAmmount) {
        this.discountAmmount = discountAmmount;
    }

    public ServiceFee getServiceFee() {
        return serviceFee;
    }

    public void setServiceFee(ServiceFee serviceFee) {
        this.serviceFee = serviceFee;
    }

    public double getServiceFeeAmmount() {
        return serviceFeeAmmount;
    }

    public void setServiceFeeAmmount(double serviceFeeAmmount) {
        this.serviceFeeAmmount = serviceFeeAmmount;
    }
}
