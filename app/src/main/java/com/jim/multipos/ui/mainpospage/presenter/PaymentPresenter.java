package com.jim.multipos.ui.mainpospage.presenter;

import com.jim.multipos.core.Presenter;
import com.jim.multipos.data.db.model.customer.Customer;
import com.jim.multipos.data.db.model.customer.Debt;
import com.jim.multipos.data.db.model.order.Order;
import com.jim.multipos.data.db.model.order.PayedPartitions;

import java.util.List;

/**
 * Created by Portable-Acer on 27.10.2017.
 */

public interface PaymentPresenter extends Presenter {
    void onDebtBorrowClicked();
    void changePayment(int positionPayment);
    void removePayedPart(int removedPayedPart);
    void incomeNewData(Order order, List<PayedPartitions> payedPartitions);
    void typedPayment(double paymentTyped);
    void pressFirstOptional();
    void pressSecondOptional();
    void pressAllAmount();
    void payButtonPressed();
    void setCustomer(Customer customer);
    void onDebtSave(Debt debt);
    void onClickedTips();
}
