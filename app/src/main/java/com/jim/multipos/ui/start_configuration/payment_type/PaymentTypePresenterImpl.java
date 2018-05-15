package com.jim.multipos.ui.start_configuration.payment_type;

import com.jim.multipos.core.BasePresenterImpl;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.Account;
import com.jim.multipos.data.db.model.PaymentType;
import com.jim.multipos.data.prefs.PreferencesHelper;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class PaymentTypePresenterImpl extends BasePresenterImpl<PaymentTypeView> implements PaymentTypePresenter {

    private DatabaseManager databaseManager;
    private final PreferencesHelper preferencesHelper;
    private List<PaymentType> paymentTypes;
    private List<Account> accountList;

    @Inject
    protected PaymentTypePresenterImpl(PaymentTypeView paymentTypeView, DatabaseManager databaseManager, PreferencesHelper preferencesHelper) {
        super(paymentTypeView);
        this.databaseManager = databaseManager;
        this.preferencesHelper = preferencesHelper;
        paymentTypes = new ArrayList<>();
        accountList = new ArrayList<>();
    }

    @Override
    public void setAppRunFirstTimeValue(boolean state) {
        preferencesHelper.setAppRunFirstTimeValue(false);
    }

    @Override
    public void initPaymentTypes() {
        paymentTypes.clear();
        paymentTypes.addAll(databaseManager.getAllPaymentTypes().blockingSingle());
        accountList.clear();
        accountList.addAll(databaseManager.getAllAccounts().blockingSingle());
        String[] accounts = new String[accountList.size()];
        for (int i = 0; i < accountList.size(); i++) {
            accounts[i] = accountList.get(i).getName();
        }
        view.setSpinner(accounts);
        view.setPaymentTypes(paymentTypes);
    }

    @Override
    public void addPaymentType(String name, boolean checked, int position) {
        if (!accountList.get(position).getIsActive() && checked) {
            view.setError();
        } else {
            PaymentType paymentType = new PaymentType();
            paymentType.setName(name);
            paymentType.setIsActive(checked);
            paymentType.setAccount(accountList.get(position));
            paymentType.setIsNotSystem(true);
            databaseManager.addPaymentType(paymentType).subscribe();
            paymentTypes.add(paymentType);
            view.notifyList();
        }
    }

    @Override
    public void deletePaymentType(PaymentType paymentType, int position) {
        paymentTypes.remove(position);
        databaseManager.removePaymentType(paymentType).subscribe();
        view.notifyList();
    }
}
