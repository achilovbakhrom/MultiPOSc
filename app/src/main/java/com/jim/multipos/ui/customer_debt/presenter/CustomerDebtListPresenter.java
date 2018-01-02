package com.jim.multipos.ui.customer_debt.presenter;

import com.jim.multipos.core.Presenter;
import com.jim.multipos.data.db.model.customer.Customer;
import com.jim.multipos.data.db.model.customer.Debt;

/**
 * Created by Sirojiddin on 30.12.2017.
 */

public interface CustomerDebtListPresenter extends Presenter {
    void initData(Customer customer);
    void initDebtDetails(Debt item, int position);
    void onPayToDebt();
}
