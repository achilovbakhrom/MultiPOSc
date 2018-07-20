package com.jim.multipos.ui.mainpospage.presenter;

import com.jim.multipos.core.Presenter;
import com.jim.multipos.data.db.model.Discount;
import com.jim.multipos.data.db.model.ServiceFee;

import java.util.List;

/**
 * Created by Portable-Acer on 27.10.2017.
 */

public interface ProductInfoPresenter extends Presenter {
    Double getProductQuantity();

    int getCurrentProductQuantity();

    int incrementQuantity();

    int decrementQuantity();

    void setCurrentQuantity(int quantity);

    List<ServiceFee> getServiceFees();

    List<Discount> getDiscount(String[] discountType);

    void addDiscount(double amount, String description, int amountType);

    void addServiceFee(double amount, String description, int amountType);

    void initProduct(Long productId);
}
