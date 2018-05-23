package com.jim.multipos.ui.settings.common;

import android.content.Context;

import com.jim.multipos.R;
import com.jim.multipos.core.BasePresenterImpl;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.Account;
import com.jim.multipos.data.db.model.PaymentType;

import javax.inject.Inject;

public class CommonConfigPresenterImpl extends BasePresenterImpl<CommonConfigView> implements CommonConfigPresenter {

    private final DatabaseManager databaseManager;
    private Context context;

    @Inject
    protected CommonConfigPresenterImpl(CommonConfigView commonConfigView, DatabaseManager databaseManager, Context context) {
        super(commonConfigView);
        this.databaseManager = databaseManager;
        this.context = context;
    }

    @Override
    public void changeDefaultsLanguage() {
        for (Account account : databaseManager.getAccounts()) {
            if (account.getStaticAccountType() == Account.CASH_ACCOUNT) {
                account.setName(context.getString(R.string.till));
                databaseManager.addAccount(account).subscribe();
            }
        }
        for (PaymentType paymentType : databaseManager.getPaymentTypes()) {
            if (paymentType.getTypeStaticPaymentType() == PaymentType.CASH_PAYMENT_TYPE) {
                paymentType.setName(context.getString(R.string.cash));
                databaseManager.addPaymentType(paymentType).subscribe();
            }
        }
        Account account = databaseManager.getSystemAccount().blockingGet();
        PaymentType paymentType = databaseManager.getSystemPaymentType().blockingGet();
        account.setName(context.getString(R.string.debt_report));
        databaseManager.addAccount(account).subscribe();
        paymentType.setName(context.getString(R.string.debt_report));
        databaseManager.addPaymentType(paymentType).subscribe();



    }
}
