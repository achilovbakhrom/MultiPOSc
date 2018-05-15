package com.jim.multipos.ui.start_configuration.account;

import com.jim.multipos.core.BaseView;
import com.jim.multipos.data.db.model.Account;
import com.jim.multipos.utils.CompletionMode;

import java.util.List;

public interface AccountView extends BaseView {
    void setAccounts(List<Account> accountList);
    void notifyList();
    void setMode(CompletionMode mode);
    void checkCompletion();
}
