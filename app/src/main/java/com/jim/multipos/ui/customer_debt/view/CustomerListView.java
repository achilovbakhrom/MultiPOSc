package com.jim.multipos.ui.customer_debt.view;

import com.jim.multipos.core.BaseView;
import com.jim.multipos.data.db.model.customer.Customer;

import java.util.List;

/**
 * Created by Sirojiddin on 29.12.2017.
 */

public interface CustomerListView extends BaseView {
    void fillCustomerListRecyclerView(List<Customer> customerList);
    void initSearchResults(List<Customer> searchResults, String searchText);
    void updateList();
    void setDebtListVisibility(int visibility);
}
