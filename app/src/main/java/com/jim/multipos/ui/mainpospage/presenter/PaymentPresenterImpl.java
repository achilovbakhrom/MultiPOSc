package com.jim.multipos.ui.mainpospage.presenter;

import com.jim.multipos.core.BasePresenterImpl;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.ui.mainpospage.view.PaymentView;

import javax.inject.Inject;

/**
 * Created by Portable-Acer on 27.10.2017.
 */

public class PaymentPresenterImpl extends BasePresenterImpl<PaymentView> implements PaymentPresenter {

    private DatabaseManager databaseManager;

    @Inject
    public PaymentPresenterImpl(PaymentView paymentView, DatabaseManager databaseManager) {
        super(paymentView);
        this.databaseManager = databaseManager;
    }

    @Override
    public void onDebtBorrowClicked() {

        view.openAddDebtDialog(databaseManager);
    }
}
