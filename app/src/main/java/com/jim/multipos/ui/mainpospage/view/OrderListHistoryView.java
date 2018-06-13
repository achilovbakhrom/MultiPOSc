package com.jim.multipos.ui.mainpospage.view;

import com.jim.multipos.core.BaseView;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.currency.Currency;
import com.jim.multipos.data.db.model.order.Order;
import com.jim.multipos.data.db.model.order.PayedPartitions;
import com.jim.multipos.data.prefs.PreferencesHelper;

import java.util.List;

/**
 * Created by developer on 02.02.2018.
 */

public interface OrderListHistoryView extends BaseView {
    void updateDetials(Order order);
    void initOrderListRecycler(List<Object> productList);
    void notifyList();
    void openPaymentDetailDialog(List<PayedPartitions> payedPartitions, Currency mainCurrency);
    void openEditAccsessDialog();
    void openCancelAccsessDialog();
    void setOrderNumberToToolbar(Long orderNumber);
    void onContinuePressed(Order order);
    void reprint(Order order, DatabaseManager databaseManager, PreferencesHelper preferencesHelper);
}
