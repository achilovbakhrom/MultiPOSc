package com.jim.multipos.ui.customers.customer;

import com.jim.multipos.core.BaseView;
import com.jim.multipos.data.db.model.customer.Customer;
import com.jim.multipos.data.db.model.customer.CustomerGroup;
import com.jim.multipos.ui.customers.model.CustomerAdapterDetails;

import java.util.List;

public interface CustomerFragmentView extends BaseView {
    void refreshList(List<CustomerAdapterDetails> items, List<CustomerGroup> groups, long id);
    void sendEvent(int type, Customer customer);
    void notifyItemAdd(int position);
    void notifyItemChanged(int position);
    void notifyItemRemove(int position);
    void refreshList();
    void closeActivity();
    void openWarning();
}
