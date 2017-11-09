package com.jim.multipos.ui.customers_edit_new;

import com.jim.multipos.core.BaseView;
import com.jim.multipos.data.db.model.customer.Customer;

/**
 * Created by Portable-Acer on 04.11.2017.
 */

public interface CustomersEditView extends BaseView {
    void customerAdded(Customer customer);
    void customerRemoved(Customer customer);
    void updateRecyclerView();
}
