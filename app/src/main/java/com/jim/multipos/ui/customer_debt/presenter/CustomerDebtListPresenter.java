package com.jim.multipos.ui.customer_debt.presenter;

import com.jim.multipos.core.Presenter;
import com.jim.multipos.data.db.model.customer.Customer;

/**
 * Created by Sirojiddin on 30.12.2017.
 */

public interface CustomerDebtListPresenter extends Presenter {
    void initData(Customer customer);
}
