package com.jim.multipos.ui.customer_group_new;

import android.os.Bundle;

import com.jim.mpviews.MpToolbar;
import com.jim.multipos.core.DoubleSideActivity;
import com.jim.multipos.ui.customer_group_new.fragments.AddCustomerGroupFragment;
import com.jim.multipos.ui.customer_group_new.fragments.CustomerGroupsFragment;

public class CustomerGroupActivity extends DoubleSideActivity implements CustomerGroupView {

    /*@Inject
    @Getter
    public CustomerGroupPresenter presenter;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addFragmentToLeft(new AddCustomerGroupFragment());
        addFragmentWithBackStackToRight(new CustomerGroupsFragment());
    }

    @Override
    protected int getToolbarMode() {
        return MpToolbar.DEFAULT_TYPE;
    }
}
