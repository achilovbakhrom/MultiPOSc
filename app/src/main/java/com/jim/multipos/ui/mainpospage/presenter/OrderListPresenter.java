package com.jim.multipos.ui.mainpospage.presenter;

import com.jim.multipos.core.Presenter;
import com.jim.multipos.data.db.model.Discount;
import com.jim.multipos.data.db.model.ServiceFee;

import java.util.List;

/**
 * Created by Portable-Acer on 27.10.2017.
 */

public interface OrderListPresenter extends Presenter {
    List<Discount> getDiscounts();
    List<ServiceFee> getServiceFees();
    void addDiscount(double amount, String description, int amountType);
    void addServiceFee(double amount, String description, int amountType);
    void onPlusCount(int position);
    void onMinusCount(int position);
    void onOrderProductClick(int position);
    void onOrderDiscountClick();
    void onOrderServiceFeeClick();
    void addProductToList(Long productId);
}
