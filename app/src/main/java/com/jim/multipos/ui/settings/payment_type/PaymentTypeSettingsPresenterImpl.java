package com.jim.multipos.ui.settings.payment_type;

import com.jim.multipos.core.BasePresenterImpl;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.Account;
import com.jim.multipos.data.db.model.PaymentType;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class PaymentTypeSettingsPresenterImpl extends BasePresenterImpl<PaymentTypeSettingsView> implements PaymentTypeSettingsPresenter {

    private DatabaseManager databaseManager;
    private List<PaymentType> paymentTypes;
    private List<Account> accountList;

    @Inject
    protected PaymentTypeSettingsPresenterImpl(PaymentTypeSettingsView paymentTypeSettingsView, DatabaseManager databaseManager) {
        super(paymentTypeSettingsView);
        this.databaseManager = databaseManager;
        paymentTypes = new ArrayList<>();
        accountList = new ArrayList<>();
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
        view.setPaymentTypes(paymentTypes, accountList);
    }

    @Override
    public void savePaymentType(PaymentType paymentType) {
        databaseManager.addPaymentType(paymentType).subscribe();
        view.setSuccess();
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
}
