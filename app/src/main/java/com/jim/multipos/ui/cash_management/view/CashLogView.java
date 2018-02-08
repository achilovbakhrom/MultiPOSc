package com.jim.multipos.ui.cash_management.view;

import com.jim.multipos.core.BaseView;
import com.jim.multipos.data.db.model.Account;
import com.jim.multipos.data.db.model.till.Till;

import java.util.List;

/**
 * Created by Sirojiddin on 11.01.2018.
 */

public interface CashLogView extends BaseView {
    void setDataToAccountSpinner(List<String> accounts);
    void setDataToDetailsFragment(Account account, Till till);
    void setTillOpenDateTime(Long date);
    void changeAccount(Long accountId);
    void setAccountSelection(int position);
}
