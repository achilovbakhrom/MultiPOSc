package com.jim.multipos.ui.settings.accounts;

import com.jim.multipos.core.BasePresenterImpl;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.Account;
import com.jim.multipos.data.db.model.PaymentType;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class AccountSettingsPresenterImpl extends BasePresenterImpl<AccountSettingsView> implements AccountSettingsPresenter {

    private final DatabaseManager databaseManager;
    private List<Account> accountList;

    @Inject
    protected AccountSettingsPresenterImpl(AccountSettingsView accountSettingsView, DatabaseManager databaseManager) {
        super(accountSettingsView);
        this.databaseManager = databaseManager;
        accountList = new ArrayList<>();
    }

    @Override
    public void initAccounts() {
        accountList = databaseManager.getAllAccounts().blockingSingle();
        view.setAccounts(accountList);
    }

    @Override
    public void saveChanges(Account account, int position) {
        databaseManager.addAccount(account).subscribe();
        List<PaymentType> paymentTypeList = databaseManager.getAllPaymentTypes().blockingSingle();
        for (int i = 0; i < paymentTypeList.size(); i++) {
            if (!account.getIsActive())
                if (paymentTypeList.get(i).getAccount().getId().equals(account.getId())) {
                    paymentTypeList.get(i).setIsActive(account.getIsActive());
                    databaseManager.addPaymentType(paymentTypeList.get(i)).subscribe();
                }
        }
        view.setSuccess();
    }

    @Override
    public void addAccount(String name, boolean checked) {
        Account account = new Account();
        account.setName(name);
        account.setIsActive(checked);
        databaseManager.addAccount(account).subscribe();
        accountList.add(account);
        view.notifyList();
    }
}
