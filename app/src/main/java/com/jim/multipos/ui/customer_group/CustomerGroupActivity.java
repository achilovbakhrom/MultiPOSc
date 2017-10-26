package com.jim.multipos.ui.customer_group;

import android.os.Bundle;

import com.jim.mpviews.MpToolbar;
import com.jim.multipos.core.DoubleSideActivity;
import com.jim.multipos.ui.customer_group.fragments.AddCustomerGroupFragment;
import com.jim.multipos.ui.customer_group.fragments.CustomerGroupFragment;
import com.jim.multipos.ui.customer_group.fragments.CustomerGroupListFragment;
import com.jim.multipos.utils.RxBusLocal;
import com.jim.multipos.utils.rxevents.CustomerGroupEvent;

import java.util.ArrayList;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;

import static com.jim.multipos.ui.customer_group.fragments.CustomerGroupListFragment.CUSTOMER_GROUP_DELETED;

public class CustomerGroupActivity extends DoubleSideActivity implements CustomerGroupView {
    @Inject
    RxBusLocal rxBusLocal;
    private boolean isMembersOpen = false;
    private ArrayList<Disposable> subscriptions = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        rxConnections();

        addFragmentToLeft(new AddCustomerGroupFragment());
        addFragmentWithBackStackToRight(new CustomerGroupListFragment());
    }

    @Override
    public void onBackPressed() {
        if (isMembersOpen) {
            popBackStack();
            //replaceFragmentToRight(new CustomerGroupListFragment());
            isMembersOpen = false;
        } else {
            finish();
        }
    }

    public void openCustomerGroupsFragment() {
        if (isMembersOpen) {
            //replaceFragmentToRight(new CustomerGroupListFragment());
            //addFragmentToRight(new CustomerGroupListFragment());
            popBackStack();
            isMembersOpen = false;
        } else {
            addFragmentWithBackStackToRight(new CustomerGroupFragment());
            isMembersOpen = true;
        }
    }

    public void closeActivity() {
        finish();
    }

    @Override
    protected int getToolbarMode() {
        return MpToolbar.DEFAULT_TYPE;
    }

    private void rxConnections() {
        subscriptions.add(rxBusLocal.toObservable().subscribe(o -> {
            if (o instanceof CustomerGroupEvent) {
                CustomerGroupEvent customerGroupEvent = (CustomerGroupEvent) o;

                if (customerGroupEvent.getEventType().equals(CUSTOMER_GROUP_DELETED)) {
                    if (isMembersOpen) {
                        popBackStack();
                        isMembersOpen = false;
                    }
                }
            }
        }));
    }
}