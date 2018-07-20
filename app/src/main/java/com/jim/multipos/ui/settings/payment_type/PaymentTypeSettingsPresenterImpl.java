package com.jim.multipos.ui.settings.payment_type;

import com.jim.multipos.core.BasePresenterImpl;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.Account;
import com.jim.multipos.data.db.model.PaymentType;
import com.jim.multipos.ui.settings.payment_type.model.PaymentTypeItem;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class PaymentTypeSettingsPresenterImpl extends BasePresenterImpl<PaymentTypeSettingsView> implements PaymentTypeSettingsPresenter {

    private DatabaseManager databaseManager;
    private List<PaymentType> paymentTypes;
    private List<PaymentTypeItem> paymentTypeItems;
    private List<Account> accountList;

    @Inject
    protected PaymentTypeSettingsPresenterImpl(PaymentTypeSettingsView paymentTypeSettingsView, DatabaseManager databaseManager) {
        super(paymentTypeSettingsView);
        this.databaseManager = databaseManager;
        paymentTypes = new ArrayList<>();
        accountList = new ArrayList<>();
        paymentTypeItems = new ArrayList<>();
    }

    @Override
    public void initPaymentTypes() {
        paymentTypeItems.clear();
        databaseManager.getAllPaymentTypes().subscribe(paymentTypes1 -> {
            for (PaymentType paymentType: paymentTypes1){
                PaymentTypeItem item = new PaymentTypeItem();
                item.setPaymentType(paymentType);
                item.setName(paymentType.getName());
                item.setAccount(paymentType.getAccount());
                item.setActive(paymentType.getIsActive());
                paymentTypeItems.add(item);
            }
        });
        accountList.clear();
        accountList.addAll(databaseManager.getAllAccounts().blockingSingle());
        String[] accounts = new String[accountList.size()];
        for (int i = 0; i < accountList.size(); i++) {
            accounts[i] = accountList.get(i).getName();
        }
        view.setSpinner(accounts);
        view.setPaymentTypes(paymentTypeItems, accountList);
    }

    @Override
    public void savePaymentType(PaymentTypeItem item) {
        PaymentType paymentType = item.getPaymentType();
        paymentType.setName(item.getName());
        paymentType.setIsActive(item.isActive());
        paymentType.setAccount(item.getAccount());
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
            databaseManager.addPaymentType(paymentType).subscribe(aLong -> {
                PaymentTypeItem item = new PaymentTypeItem();
                item.setName(paymentType.getName());
                item.setPaymentType(paymentType);
                item.setAccount(paymentType.getAccount());
                item.setActive(paymentType.getIsActive());
                paymentTypeItems.add(item);
                view.notifyList();
            });

        }
    }
}
