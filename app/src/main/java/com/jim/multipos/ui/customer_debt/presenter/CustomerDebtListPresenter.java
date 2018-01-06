package com.jim.multipos.ui.customer_debt.presenter;

import com.jim.multipos.core.Presenter;
import com.jim.multipos.data.db.model.customer.Customer;
import com.jim.multipos.data.db.model.customer.Debt;
import com.jim.multipos.ui.customer_debt.view.CustomerDebtListFragment;

/**
 * Created by Sirojiddin on 30.12.2017.
 */

public interface CustomerDebtListPresenter extends Presenter {
    void initData(Customer customer);
    void initDebtDetails(Debt item, int position);
    void onPayToDebt();
    void onPaymentHistoryClicked();
    void onCustomerDebtsHistoryClicked();
    void closeDebtWithPayingAllAmount(Debt item);
    void filterBy(CustomerDebtListFragment.DebtSortingStates sortMode);
    void filterInvert();
}
