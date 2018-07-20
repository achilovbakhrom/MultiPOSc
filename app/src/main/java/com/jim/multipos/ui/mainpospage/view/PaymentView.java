package com.jim.multipos.ui.mainpospage.view;

import com.jim.mpviews.model.PaymentTypeWithService;
import com.jim.multipos.core.BaseView;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.customer.Customer;
import com.jim.multipos.data.db.model.customer.Debt;
import com.jim.multipos.data.db.model.order.Order;
import com.jim.multipos.data.db.model.order.PayedPartitions;
import com.jim.multipos.ui.mainpospage.dialogs.TipsDialog;

import java.util.List;

/**
 * Created by Portable-Acer on 27.10.2017.
 */

public interface PaymentView extends BaseView {
    void openAddDebtDialog(DatabaseManager databaseManager, Order order, Customer customer, double toPay);

    void initPaymentTypes(List<PaymentTypeWithService> paymentTypeWithServices);

    void updatePaymentList(List<PayedPartitions> payedPartitions);

    void updatePaymentList();

    void updateViews(Order order, double payedValue);

    void getDataFromListOrder(Order order, List<PayedPartitions> payedPartitions);

    void updateChangeView(double change);

    void updateBalanceView(double change);

    void updatePaymentText(double payment);

    void updateBalanceZeroText();

    void updateCloseText();

    void closeSelf();

    void onPayedPartition();

    void setCustomer(Customer customer);

    void closeOrder(Order order, List<PayedPartitions> payedPartitions, Debt debt);

    void updateCustomer(Customer customer);

    void showDebtDialog();

    void hideDebtDialog();

    void openTipsDialog(TipsDialog.OnClickListener listener, double change);

    void enableTipsButton();

    void disableTipsButton();

    void updateOrderListDetialsPanel();

    void onNewOrder();

    void sendDataToPaymentFragmentWhenEdit(Order order, List<PayedPartitions> payedPartitions, Debt debt);

    void onHoldOrderClicked();

    void onHoldOrderSendingData(Order order, List<PayedPartitions> payedPartitions, Debt debt);

    void openWarningDialog(String text);
}
