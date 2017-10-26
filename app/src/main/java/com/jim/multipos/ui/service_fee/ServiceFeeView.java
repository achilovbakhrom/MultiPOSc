package com.jim.multipos.ui.service_fee;

import com.jim.multipos.core.BaseView;
import com.jim.multipos.data.db.model.ServiceFee;
import com.jim.multipos.data.db.model.PaymentType;
import com.jim.multipos.data.db.model.currency.Currency;

import java.util.List;

/**
 * Created by user on 28.08.17.
 */

public interface ServiceFeeView extends BaseView {
    void showSpType(String[] types);
    void showSpCurrency(List<Currency> currencies);
    void showAppType(String[] appTypes);
    void showRVServiceData(List<ServiceFee> serviceFeeList, List<Currency> currencies, String[] types, String[] appTypes);
    void updateRecyclerView();
    void clearViews();
    void showNameError(String message);
    void showAmountError(String message);
    void enableCurrency(boolean enable);
    void openAutoApplyDialog(List<PaymentType> paymentTypes);
    void openUsageTypeDialog();
    void closeActivity();
}
