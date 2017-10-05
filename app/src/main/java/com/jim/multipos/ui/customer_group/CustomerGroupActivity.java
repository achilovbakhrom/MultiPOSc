package com.jim.multipos.ui.customer_group;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import com.jim.multipos.R;
import com.jim.multipos.core.BaseActivity;
import com.jim.multipos.data.db.model.customer.Customer;
import com.jim.multipos.data.db.model.customer.CustomerGroup;
import com.jim.multipos.di.BaseAppComponent;
import com.jim.multipos.ui.HasComponent;
import com.jim.multipos.ui.customer_group.connector.CustomerGroupConnector;
import com.jim.multipos.ui.customer_group.di.CustomerGroupActivityComponent;
import com.jim.multipos.ui.customer_group.di.CustomerGroupActivityModule;
import com.jim.multipos.ui.customer_group.fragments.AddCustomerGroupFragment;
import com.jim.multipos.ui.customer_group.fragments.CustomerGroupFragment;
import com.jim.multipos.ui.customer_group.fragments.CustomerGroupListFragment;
import com.jim.multipos.utils.RxBusLocal;
import com.jim.multipos.utils.managers.PosFragmentManager;
import com.jim.multipos.utils.rxevents.CustomerGroupEvent;

import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public class CustomerGroupActivity extends BaseActivity implements HasComponent<CustomerGroupActivityComponent>, CustomerGroupView {
    @Inject
    PosFragmentManager fragmentManager;
    @Inject
    RxBusLocal rxBusLocal;
    private Unbinder unbinder;
    private CustomerGroupActivityComponent component;
    private boolean isMembersOpen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_group);

        unbinder = ButterKnife.bind(this);

        fragmentManager.displayFragment(new AddCustomerGroupFragment(), R.id.leftContainer);
        fragmentManager.displayFragment(new CustomerGroupListFragment(), R.id.rightContainer);
    }

    @Override
    protected void onDestroy() {
        unbinder.unbind();

        super.onDestroy();
    }

    @Override
    public CustomerGroupActivityComponent getComponent() {
        return component;
    }

//    @Override
    protected void setupComponent(BaseAppComponent baseAppComponent) {
//        component = baseAppComponent.plus(new CustomerGroupActivityModule(this));
        component.inject(this);
    }

    @Override
    public void onBackPressed() {
        if (isMembersOpen) {
            fragmentManager.popBackStack();
            isMembersOpen = false;
        } else {
            finish();
        }
    }

    public void openCustomerGroupsFragment() {
        if (isMembersOpen) {
            fragmentManager.displayFragment(new CustomerGroupListFragment(), R.id.rightContainer);
            isMembersOpen = false;
        } else {
            fragmentManager.displayFragment(new CustomerGroupFragment(), R.id.rightContainer);
            isMembersOpen = true;
        }
    }

    public void closeActivity() {
        finish();
    }
}
