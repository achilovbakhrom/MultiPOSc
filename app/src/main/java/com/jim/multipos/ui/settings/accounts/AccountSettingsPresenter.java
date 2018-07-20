package com.jim.multipos.ui.settings.accounts;

import com.jim.multipos.core.Presenter;
import com.jim.multipos.data.db.model.Account;
import com.jim.multipos.ui.settings.accounts.model.AccountItem;

public interface AccountSettingsPresenter extends Presenter {
    void initAccounts();
    void saveChanges(AccountItem account, int position);
    void addAccount(String name, boolean checked);
}
