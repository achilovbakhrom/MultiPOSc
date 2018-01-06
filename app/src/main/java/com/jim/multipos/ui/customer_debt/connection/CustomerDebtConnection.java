package com.jim.multipos.ui.customer_debt.connection;

import android.content.Context;

import com.jim.multipos.data.db.model.customer.Customer;
import com.jim.multipos.ui.customer_debt.view.CustomerDebtListView;
import com.jim.multipos.ui.customer_debt.view.CustomerListView;

/**
 * Created by Sirojiddin on 30.12.2017.
 */

public class CustomerDebtConnection {
    private CustomerDebtListView debtListView;
    private CustomerListView customerListView;
    private Context context;

    public CustomerDebtConnection(Context context) {
        this.context = context;
    }

    public void setDebtListView(CustomerDebtListView debtListView) {
        this.debtListView = debtListView;
    }

    public void sendCustomerWithDebt(Customer customer) {
         if (this.debtListView != null)
             this.debtListView.initCustomerWithDebt(customer);
    }

    public void updateCustomersList() {
        if (this.customerListView != null)
            this.customerListView.updateList();
    }

    public void setCustomerListView(CustomerListView customerListView) {
        this.customerListView = customerListView;
    }

    public void setCustomerDebtListVisibility(int visibility) {
        if (this.debtListView != null)
            this.debtListView.setCustomerDebtListVisibility(visibility);
    }
}
