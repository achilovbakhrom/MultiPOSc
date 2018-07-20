package com.jim.multipos.ui.settings.accounts;

import com.jim.multipos.core.BasePresenterImpl;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.Account;
import com.jim.multipos.data.db.model.PaymentType;
import com.jim.multipos.ui.settings.accounts.model.AccountItem;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class AccountSettingsPresenterImpl extends BasePresenterImpl<AccountSettingsView> implements AccountSettingsPresenter {

    private DatabaseManager databaseManager;
    private List<Account> accountList;
    private List<AccountItem> accountItems;

    @Inject
    protected AccountSettingsPresenterImpl(AccountSettingsView accountSettingsView, DatabaseManager databaseManager) {
        super(accountSettingsView);
        this.databaseManager = databaseManager;
        accountList = new ArrayList<>();
        accountItems = new ArrayList<>();
    }

    @Override
    public void initAccounts() {
        accountList = databaseManager.getAllAccounts().blockingSingle();
        for (Account account : accountList) {
            AccountItem item = new AccountItem();
            item.setActive(account.getIsActive());
            item.setName(account.getName());
            item.setAccount(account);
            accountItems.add(item);
        }
        view.setAccounts(accountItems);
    }

    @Override
    public void saveChanges(AccountItem item, int position) {
        Account account = item.getAccount();
        account.setIsActive(item.isActive());
        account.setName(item.getName());
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
        databaseManager.addAccount(account).subscribe(account1 -> {
            AccountItem item = new AccountItem();
            item.setActive(account.getIsActive());
            item.setName(account.getName());
            item.setAccount(account);
            accountItems.add(item);
            view.notifyList();
        });

    }
}
