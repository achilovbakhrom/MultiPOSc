package com.jim.multipos.ui.customer_debt.connection;

import android.content.Context;

import com.jim.multipos.data.db.model.customer.Customer;
import com.jim.multipos.ui.customer_debt.view.CustomerDebtListView;

/**
 * Created by Sirojiddin on 30.12.2017.
 */

public class CustomerDebtConnection {
    private CustomerDebtListView debtListView;
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
}
