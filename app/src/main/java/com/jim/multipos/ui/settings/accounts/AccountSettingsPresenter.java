package com.jim.multipos.ui.settings.accounts;

import com.jim.multipos.core.Presenter;
import com.jim.multipos.data.db.model.Account;

public interface AccountSettingsPresenter extends Presenter {
    void initAccounts();
    void saveChanges(Account account, int position);
    void addAccount(String name, boolean checked);
}
