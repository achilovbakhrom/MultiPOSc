package com.jim.multipos.ui.service_fee;

import java.util.HashMap;

/**
 * Created by user on 28.08.17.
 */

public interface ServiceFeePresenter {
    void getSpTypes();
    void getCurrencies();
    void getAppTypes();
    void getServiceFeeData();
    void addItem(String name, String amount, int type, int currency, int appType, boolean isTaxed, boolean isActive, int paymentType);
    void saveData();
    void changeType(int position);
    void getPaymentType();
    void openUsageTypeDialog(String name, String amount, int type, int currency, int appType, boolean isTaxed, boolean isActive);
    void setCheckedTaxed(boolean state, int position);
    void setCheckedActive(boolean state, int position);
}
