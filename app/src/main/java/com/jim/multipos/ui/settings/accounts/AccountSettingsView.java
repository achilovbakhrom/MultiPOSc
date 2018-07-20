package com.jim.multipos.ui.settings.accounts;

import com.jim.multipos.core.BaseView;
import com.jim.multipos.data.db.model.Account;
import com.jim.multipos.ui.settings.accounts.model.AccountItem;

import java.util.List;

public interface AccountSettingsView extends BaseView {
    void setAccounts(List<AccountItem> accountList);
    void notifyList();
    void setSuccess();
}
