package com.jim.multipos.ui.customer_group_new.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.jim.multipos.R;
import com.jim.multipos.core.BaseFragment;
import com.jim.multipos.data.db.model.customer.Customer;
import com.jim.multipos.data.db.model.customer.CustomerGroup;
import com.jim.multipos.ui.customer_group_new.CustomerGroupActivity;
import com.jim.multipos.ui.customer_group_new.adapters.CustomersAdapter;

import butterknife.BindView;

import static com.jim.multipos.ui.customer_group_new.CustomerGroupActivity.CUSTOMER_GROUP_ID;

/**
 * A simple {@link Fragment} subclass.
 */
public class CustomersFragment extends BaseFragment implements CustomersAdapter.OnClickListener {
    @BindView(R.id.rvCustomerGroups)
    RecyclerView rvCustomerGroups;

    @Override
    protected boolean isAndroidInjectionEnabled() {
        return false;
    }

    @Override
    protected int getLayout() {
        return R.layout.customer_group_fragment;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        rvCustomerGroups.setLayoutManager(new LinearLayoutManager(getContext()));

        if (getArguments().getLong(CUSTOMER_GROUP_ID) == -1) {
            CustomerGroup customerGroup = new CustomerGroup();
            customerGroup.setId(-1L);
            customerGroup.setCustomers(((CustomerGroupActivity) getActivity()).getPresenter().getTempCustomers());

            rvCustomerGroups.setAdapter(new CustomersAdapter(this,
                    customerGroup,
                    ((CustomerGroupActivity) getActivity()).getPresenter().getCustomers()));
        } else {
            rvCustomerGroups.setAdapter(new CustomersAdapter(this,
                    ((CustomerGroupActivity) getActivity()).getPresenter().getCustomerGroupById(getArguments().getLong(CUSTOMER_GROUP_ID)),
                    ((CustomerGroupActivity) getActivity()).getPresenter().getCustomers()));
        }
    }

    @Override
    public void addCustomerToCustomerGroup(CustomerGroup customerGroup, Customer customer) {
        ((CustomerGroupActivity) getActivity()).getPresenter().addCustomerToCustomerGroup(customerGroup, customer);
    }

    @Override
    public void removeCustomerFromCustomerGroup(CustomerGroup customerGroup, Customer customer) {
        ((CustomerGroupActivity) getActivity()).getPresenter().removeCustomerFromCustomerGroup(customerGroup, customer);
    }

    public void addedCustomerToCustomerGroup(Customer customer) {
        ((CustomersAdapter) rvCustomerGroups.getAdapter()).addedCustomerToCustomerGroup(customer);
    }

    public void removedCustomerFromCustomerGroup(Customer customer) {
        ((CustomersAdapter) rvCustomerGroups.getAdapter()).removedCustomerFromCustomerGroup(customer);
    }
}
