package com.jim.multipos.ui.cash_management.view;

import com.jim.mpviews.model.PaymentTypeWithService;
import com.jim.multipos.core.BaseView;
import com.jim.multipos.data.db.model.PaymentType;
import com.jim.multipos.data.db.model.till.Till;

import java.util.ArrayList;

/**
 * Created by Sirojiddin on 11.01.2018.
 */

public interface CashOperationsView extends BaseView{
    void initPaymentTypes(ArrayList<PaymentTypeWithService> paymentTypeWithServices);
    void showWarningDialog(String warningText);
    void updateDetails();
    void openCashOperationDialog(Till till, PaymentType currentPaymentType, int type, double amount);
    void changeAccount(Long accountId);
    void setBankDropVisibility(int visibility);
    void getTillStatus(int status);
    void showCloseTillDialog();
    void showOpenTillDialog();
}
