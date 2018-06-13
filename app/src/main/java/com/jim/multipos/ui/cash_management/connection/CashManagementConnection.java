package com.jim.multipos.ui.cash_management.connection;

import android.content.Context;

import com.jim.multipos.data.db.model.till.TillManagementOperation;
import com.jim.multipos.ui.cash_management.view.CashDetailsView;
import com.jim.multipos.ui.cash_management.view.CashLogView;
import com.jim.multipos.ui.cash_management.view.CashOperationsView;
import com.jim.multipos.ui.cash_management.view.CloseTillDialogFragmentView;
import com.jim.multipos.ui.cash_management.view.CloseTillFirstStepView;
import com.jim.multipos.ui.cash_management.view.CloseTillSecondStepView;
import com.jim.multipos.ui.cash_management.view.CloseTillThirdStepView;

import java.util.List;

/**
 * Created by Sirojiddin on 03.02.2018.
 */

public class CashManagementConnection {
    private CashDetailsView cashDetailsView;
    private CashLogView cashLogView;
    private CashOperationsView cashOperationsView;
    private CloseTillDialogFragmentView closeTillDialogView;
    private CloseTillFirstStepView closeTillFirstStepView;
    private CloseTillSecondStepView closeTillSecondStepView;
    private CloseTillThirdStepView closeTillThirdStepView;
    private Context context;

    public CashManagementConnection(Context context) {
        this.context = context;
    }

    public void updateCashDetails() {
        if (cashDetailsView != null) {
            cashDetailsView.updateDetails();
        }
    }

    public void changeAccount(Long accountId) {
        if (cashLogView != null) {
            cashLogView.changeAccount(accountId);
        }
    }

    public void checkFirstStepCompletion() {
        if (closeTillFirstStepView != null)
            closeTillFirstStepView.checkCompletion();
    }

    public void checkSecondStepCompletion() {
        if (closeTillSecondStepView != null)
            closeTillSecondStepView.checkCompletion();
    }

    public void checkThirdStepCompletion() {
        if (closeTillThirdStepView != null)
            closeTillThirdStepView.checkCompletion();
    }

    public void setFirstStepCompletionStatus(boolean status) {
        if (closeTillDialogView != null)
            closeTillDialogView.setFirstStepCompletionStatus(status);
    }


    public void setSecondStepCompletionStatus(boolean status) {
        if (closeTillDialogView != null)
            closeTillDialogView.setSecondStepCompletionStatus(status);
    }

    public void setThirdStepCompletionStatus(boolean status) {
        if (closeTillDialogView != null)
            closeTillDialogView.setThirdStepCompletionStatus(status);
    }

    public void collectData() {
        if (closeTillFirstStepView != null)
            closeTillFirstStepView.collectData();
        if (closeTillSecondStepView != null)
            closeTillSecondStepView.collectData();
        if (closeTillThirdStepView != null)
            closeTillThirdStepView.collectData();
    }

    public void sendAllSecondStepDataToParent(List<TillManagementOperation> operations) {
        if (closeTillDialogView != null)
            closeTillDialogView.setSecondStepData(operations);
    }

    public void sendAllThirdStepDataToParent(List<TillManagementOperation> operations) {
        if (closeTillDialogView != null)
            closeTillDialogView.setThirdStepData(operations);
    }

    public void setTillStatus(int status) {
        if (cashLogView != null)
            cashLogView.setTillStatus(status);
        if (cashOperationsView != null) {
            cashOperationsView.setTillStatus(status);
        }

    }

    public void setCashDetailsView(CashDetailsView cashDetailsView) {
        this.cashDetailsView = cashDetailsView;
    }

    public void setCashLogView(CashLogView cashLogView) {
        this.cashLogView = cashLogView;
    }

    public void setCloseTillDialogView(CloseTillDialogFragmentView closeTillDialogView) {
        this.closeTillDialogView = closeTillDialogView;
    }

    public void setCloseTillSecondStepView(CloseTillSecondStepView closeTillSecondStepView) {
        this.closeTillSecondStepView = closeTillSecondStepView;
    }

    public void setCloseTillThirdStepView(CloseTillThirdStepView closeTillThirdStepView) {
        this.closeTillThirdStepView = closeTillThirdStepView;
    }

    public void setCloseTillFirstStepView(CloseTillFirstStepView closeTillFirstStepView) {
        this.closeTillFirstStepView = closeTillFirstStepView;
    }

    public void setCashOperationsView(CashOperationsView cashOperationsView) {
        this.cashOperationsView = cashOperationsView;
    }
}
