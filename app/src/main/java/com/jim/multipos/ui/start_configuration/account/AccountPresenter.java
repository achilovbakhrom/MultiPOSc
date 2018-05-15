package com.jim.multipos.ui.start_configuration.account;

import com.jim.multipos.core.Presenter;
import com.jim.multipos.data.db.model.Account;

public interface AccountPresenter extends Presenter {
    void initAccounts();
    void addAccount(String name, boolean checked);
    void deleteAccount(Account account, int position);
    void setAppRunFirstTimeValue(boolean state);
}
