package com.jim.multipos.ui.customer_debt.view;

import com.jim.multipos.core.BaseView;
import com.jim.multipos.data.db.model.currency.Currency;
import com.jim.multipos.data.db.model.customer.Customer;
import com.jim.multipos.data.db.model.customer.Debt;

import java.util.List;

/**
 * Created by Sirojiddin on 30.12.2017.
 */

public interface CustomerDebtListView extends BaseView {
    void initCustomerWithDebt(Customer customer);
    void fillRecyclerView(List<Debt> debtList, Currency currency);
}