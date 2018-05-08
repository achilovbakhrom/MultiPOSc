package com.jim.multipos.ui.settings.accounts;

import com.jim.multipos.core.BaseView;
import com.jim.multipos.data.db.model.Account;

import java.util.List;

public interface AccountSettingsView extends BaseView {
    void setAccounts(List<Account> accountList);
    void notifyList();
    void setSuccess();
}
