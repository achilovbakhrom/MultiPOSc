package com.jim.multipos.ui.start_configuration.connection;

import android.content.Context;

import com.jim.multipos.utils.CompletionMode;
import com.jim.multipos.ui.start_configuration.account.AccountView;
import com.jim.multipos.ui.start_configuration.currency.CurrencyView;
import com.jim.multipos.ui.start_configuration.payment_type.PaymentTypeView;
import com.jim.multipos.ui.start_configuration.pos_data.PosDataView;
import com.jim.multipos.ui.start_configuration.selection_panel.SelectionPanelView;

public class StartConfigurationConnection {

    private Context context;
    private SelectionPanelView selectionPanelView;
    private PosDataView posDataView;
    private CurrencyView currencyView;
    private AccountView accountView;
    private PaymentTypeView paymentTypeView;

    public StartConfigurationConnection(Context context) {
        this.context = context;
    }

    public void setSelectionPanelView(SelectionPanelView selectionPanelView) {
        this.selectionPanelView = selectionPanelView;
    }

    public void setPosDataView(PosDataView posDataView) {
        this.posDataView = posDataView;
    }

    public void setCurrencyView(CurrencyView currencyView) {
        this.currencyView = currencyView;
    }

    public void setAccountView(AccountView accountView) {
        this.accountView = accountView;
    }

    public void setPosDataCompletion(boolean state) {
        if (selectionPanelView != null) {
            selectionPanelView.setPosDataCompletion(state);
        }
    }

    public void setAccountCompletion(boolean state) {
        if (selectionPanelView != null) {
            selectionPanelView.setAccountCompletion(state);
        }
    }

    public void setCurrencyCompletion(boolean state) {
        if (selectionPanelView != null) {
            selectionPanelView.setCurrencyCompletion(state);
        }
    }

    public void setPaymentTypeCompletion(boolean state) {
        if (selectionPanelView != null) {
            selectionPanelView.setPaymentTypeCompletion(state);
        }
    }

    public void setPaymentTypeView(PaymentTypeView paymentTypeView) {
        this.paymentTypeView = paymentTypeView;
    }

    public void sendModeToPosData(CompletionMode mode) {
        if (posDataView != null) {
            posDataView.setMode(mode);
        }
    }

    public void sendModeToPaymentType(CompletionMode mode) {
        if (paymentTypeView != null) {
            paymentTypeView.setMode(mode);
        }
    }

    public void sendModeToAccount(CompletionMode mode) {
        if (accountView != null) {
            accountView.setMode(mode);
        }
    }

    public void sendModeToCurrency(CompletionMode mode) {
        if (currencyView != null) {
            currencyView.setMode(mode);
        }
    }

    public void checkCurrencyCompletion() {
        if (currencyView != null) {
            currencyView.checkCompletion();
        }
    }

    public void checkAccountCompletion() {
        if (accountView != null) {
            accountView.checkCompletion();
        }
    }

    public void checkPosDataCompletion() {
        if (posDataView != null) {
            posDataView.checkPosDataCompletion();
        }
    }

    public void checkPaymentTypeCompletion() {
        if (paymentTypeView != null) {
            paymentTypeView.checkCompletion();
        }
    }

    public void updateAccounts(){
        if (paymentTypeView != null) {
            paymentTypeView.updateAccounts();
        }
    }

    public void openNextFragment(int i) {
        if (selectionPanelView != null) {
            selectionPanelView.openNextFragment(i);
        }
    }
}
