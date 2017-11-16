package com.jim.multipos.ui.mainpospage;

import com.jim.multipos.data.db.model.Discount;
import com.jim.multipos.data.db.model.ServiceFee;

import java.util.List;

/**
 * Created by developer on 07.08.2017.
 */

public interface MainPosPageActivityView {
    List<Discount> getDiscounts();
    void addDiscount(double amount, String description, String amountType);
    List<ServiceFee> getServiceFees();
    void addServiceFee(double amount, String description, String amountType);
}
