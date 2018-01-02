package com.jim.multipos.ui.customer_debt.presenter;

import com.jim.multipos.core.BasePresenterImpl;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.currency.Currency;
import com.jim.multipos.data.db.model.customer.Customer;
import com.jim.multipos.data.db.model.customer.Debt;
import com.jim.multipos.ui.customer_debt.view.CustomerDebtListView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Sirojiddin on 30.12.2017.
 */

public class CustomerDebtListPresenterImpl extends BasePresenterImpl<CustomerDebtListView> implements CustomerDebtListPresenter {

    private List<Debt> debtList;
    private Currency currency;
    private DatabaseManager databaseManager;
    @Inject
    protected CustomerDebtListPresenterImpl(CustomerDebtListView view, DatabaseManager databaseManager) {
        super(view);
        this.databaseManager = databaseManager;
        debtList = new ArrayList<>();
    }

    @Override
    public void initData(Customer customer) {
        currency = databaseManager.getMainCurrency();
        databaseManager.getDebtsByCustomerId(customer.getId()).subscribe((debts, throwable) -> {
            debtList = debts;
            view.fillRecyclerView(debtList, currency);
        });
    }
}
