package com.jim.multipos.ui.cash_management.presenter;

import com.jim.multipos.core.Presenter;
import com.jim.multipos.data.db.model.Account;
import com.jim.multipos.data.db.model.till.Till;

/**
 * Created by Sirojiddin on 12.01.2018.
 */

public interface CashDetailsPresenter extends Presenter{
    void calculateCashDetails(Account account, Till till);
    void updateDetails();
}
