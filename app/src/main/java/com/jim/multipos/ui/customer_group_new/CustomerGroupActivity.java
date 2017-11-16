package com.jim.multipos.ui.customer_group_new;

import android.os.Bundle;
import android.util.Log;

import com.jim.mpviews.MpToolbar;
import com.jim.multipos.R;
import com.jim.multipos.core.DoubleSideActivity;
import com.jim.multipos.data.db.model.customer.Customer;
import com.jim.multipos.data.db.model.customer.CustomerGroup;
import com.jim.multipos.ui.customer_group_new.fragments.AddCustomerGroupFragment;
import com.jim.multipos.ui.customer_group_new.fragments.CustomerGroupsFragment;
import com.jim.multipos.ui.customer_group_new.fragments.CustomersFragment;
import com.jim.multipos.utils.WarningDialog;

import javax.inject.Inject;

import lombok.Getter;

public class CustomerGroupActivity extends DoubleSideActivity implements CustomerGroupView {
    public static final String CUSTOMER_GROUP_ID = "CUSTOMER_GROUP_ID";
    public static final String CUSTOMER_GROUPS_FRAGMENT = "CUSTOMER_GROUPS_FRAGMENT";

    @Inject
    @Getter
    public CustomerGroupPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addFragmentToLeft(new AddCustomerGroupFragment());
        //addFragmentWithBackStackToRight(new CustomerGroupsFragment());
        addFragmentWithTagToRight(new CustomerGroupsFragment(), CUSTOMER_GROUPS_FRAGMENT);
    }

    @Override
    protected int getToolbarMode() {
        return MpToolbar.DEFAULT_TYPE;
    }

    @Override
    public void clearAddFragment() {
        ((AddCustomerGroupFragment) getCurrentFragmentLeft()).clearView();
    }

    @Override
    public void showSelectedCustomerGroup(CustomerGroup customerGroup) {
        ((AddCustomerGroupFragment) getCurrentFragmentLeft()).showCustomerGroup(customerGroup);
    }

    @Override
    public void addItem(CustomerGroup customerGroup) {
        if (getCurrentFragmentRight() instanceof CustomerGroupsFragment) {
            ((CustomerGroupsFragment) getCurrentFragmentRight()).addItem(customerGroup);
        } else {
            popBackStack();
            ((CustomerGroupsFragment) getFragmentByTag(CUSTOMER_GROUPS_FRAGMENT)).addItem(customerGroup);
        }
    }

    @Override
    public void removeItem(CustomerGroup customerGroup) {
        if (getCurrentFragmentRight() instanceof CustomerGroupsFragment) {
            ((CustomerGroupsFragment) getCurrentFragmentRight()).removeItem(customerGroup);
        } else {
            popBackStack();
            ((CustomerGroupsFragment) getFragmentByTag(CUSTOMER_GROUPS_FRAGMENT)).removeItem(customerGroup);
        }
    }

    @Override
    public void updateItem(CustomerGroup customerGroup) {
        if (getCurrentFragmentRight() instanceof CustomerGroupsFragment) {
            ((CustomerGroupsFragment) getCurrentFragmentRight()).updateItem(customerGroup);
        } else {
            popBackStack();
            ((CustomerGroupsFragment) getFragmentByTag(CUSTOMER_GROUPS_FRAGMENT)).updateItem(customerGroup);
        }
    }

    @Override
    public void showCustomersFragment(long customerGroupId) {
        if (getCurrentFragmentRight() instanceof CustomersFragment) {
            popBackStack();
        } else {
            Bundle bundle = new Bundle();
            bundle.putLong(CUSTOMER_GROUP_ID, customerGroupId);

            CustomersFragment fragment = new CustomersFragment();
            fragment.setArguments(bundle);

            addFragmentWithBackStackToRight(fragment);
        }
    }

    @Override
    public void addedCustomerToCustomerGroup(Customer customer) {
        ((CustomersFragment) getCurrentFragmentRight()).addedCustomerToCustomerGroup(customer);
    }

    @Override
    public void removedCustomerFromCustomerGroup(Customer customer) {
        ((CustomersFragment) getCurrentFragmentRight()).removedCustomerFromCustomerGroup(customer);
    }

    @Override
    public void setDefaultValueCustomerGroupRV() {
        if (getCurrentFragmentRight() instanceof CustomerGroupsFragment) {
            ((CustomerGroupsFragment) getCurrentFragmentRight()).setDefaultValueRV();
        }
    }

    @Override
    public void onBackPressed() {
        if (getCurrentFragmentRight() instanceof CustomerGroupsFragment) {
            finish();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void showActiveCustomerGroupWarningDialog() {
        WarningDialog warningDialog = new WarningDialog(this);
        warningDialog.onlyText(true);
        warningDialog.setWarningMessage(getString(R.string.you_can_only_delete_inactive_customer_groups));
        warningDialog.setOnYesClickListener(view -> {
            warningDialog.dismiss();
        });
        warningDialog.show();
    }
}
