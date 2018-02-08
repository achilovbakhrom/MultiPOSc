package com.jim.multipos.ui.cash_management.connection;

import android.content.Context;

import com.jim.multipos.ui.cash_management.view.CashDetailsView;
import com.jim.multipos.ui.cash_management.view.CashLogView;

/**
 * Created by Sirojiddin on 03.02.2018.
 */

public class CashManagementConnection {
    private CashDetailsView cashDetailsView;
    private CashLogView cashLogView;
    private Context context;

    public CashManagementConnection(Context context) {
        this.context = context;
    }

    public void updateCashDetails(){
        if (cashDetailsView != null){
            cashDetailsView.updateDetails();
        }
    }

    public void changeAccount(Long accountId){
        if (cashLogView != null){
            cashLogView.changeAccount(accountId);
        }
    }

    public void setCashDetailsView(CashDetailsView cashDetailsView) {
        this.cashDetailsView = cashDetailsView;
    }

    public void setCashLogView(CashLogView cashLogView) {
        this.cashLogView = cashLogView;
    }
}
