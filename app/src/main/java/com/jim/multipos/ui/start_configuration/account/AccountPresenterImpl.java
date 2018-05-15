package com.jim.multipos.ui.start_configuration.account;

import com.jim.multipos.core.BasePresenterImpl;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.Account;
import com.jim.multipos.data.db.model.PaymentType;
import com.jim.multipos.data.prefs.PreferencesHelper;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class AccountPresenterImpl extends BasePresenterImpl<AccountView> implements AccountPresenter {

    private DatabaseManager databaseManager;
    private PreferencesHelper preferencesHelper;
    private List<Account> accountList;

    @Inject
    protected AccountPresenterImpl(AccountView accountView, DatabaseManager databaseManager, PreferencesHelper preferencesHelper) {
        super(accountView);
        this.databaseManager = databaseManager;
        this.preferencesHelper = preferencesHelper;
        accountList = new ArrayList<>();
    }

    @Override
    public void initAccounts() {
        accountList = databaseManager.getAllAccounts().blockingSingle();
        view.setAccounts(accountList);
    }

    @Override
    public void addAccount(String name, boolean checked) {
        Account account = new Account();
        account.setName(name);
        account.setIsActive(checked);
        accountList.add(account);
        databaseManager.addAccount(account).subscribe();
        view.notifyList();
    }

    @Override
    public void deleteAccount(Account account, int position) {
        accountList.remove(position);
        List<PaymentType> paymentTypeList = databaseManager.getAllPaymentTypes().blockingSingle();
        for (int i = 0; i < paymentTypeList.size(); i++) {
            if (paymentTypeList.get(i).getAccount().getId().equals(account.getId())) {
                databaseManager.removePaymentType(paymentTypeList.get(i)).subscribe();
            }
        }
        databaseManager.removeAccount(account).subscribe();
        view.notifyList();
    }

    @Override
    public void setAppRunFirstTimeValue(boolean state) {
        preferencesHelper.setAppRunFirstTimeValue(false);
    }
}
