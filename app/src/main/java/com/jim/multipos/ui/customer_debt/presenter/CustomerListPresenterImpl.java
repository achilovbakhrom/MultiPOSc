package com.jim.multipos.ui.customer_debt.presenter;

import android.view.View;

import com.jim.multipos.core.BasePresenterImpl;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.customer.Customer;
import com.jim.multipos.ui.customer_debt.view.CustomerListView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Sirojiddin on 29.12.2017.
 */

public class CustomerListPresenterImpl extends BasePresenterImpl<CustomerListView> implements CustomerListPresenter {

    private List<Customer> customerList, searchResults;
    private DatabaseManager databaseManager;

    @Inject
    protected CustomerListPresenterImpl(CustomerListView view, DatabaseManager databaseManager) {
        super(view);
        this.databaseManager = databaseManager;
    }

    @Override
    public void initData() {
        customerList = databaseManager.getCustomers().blockingSingle();
        view.fillCustomerListRecyclerView(customerList);
        if (customerList.size() == 0)
            view.setDebtListVisibility(View.GONE);
        else view.setDebtListVisibility(View.VISIBLE);
    }

    @Override
    public void setSearchText(String searchText) {
        if (searchText.isEmpty()) {
            searchResults = null;
            view.fillCustomerListRecyclerView(customerList);
        } else {
            searchResults = new ArrayList<>();
            for (int i = 0; i < customerList.size(); i++) {
                if (customerList.get(i).getName().toUpperCase().contains(searchText.toUpperCase())) {
                    searchResults.add(customerList.get(i));
                    continue;
                }
                if (customerList.get(i).getClientId().toString().toUpperCase().contains(searchText.toUpperCase())) {
                    searchResults.add(customerList.get(i));
                }
            }
            view.initSearchResults(searchResults, searchText);
        }
    }
}
