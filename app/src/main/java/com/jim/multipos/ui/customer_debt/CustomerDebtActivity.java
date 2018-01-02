package com.jim.multipos.ui.customer_debt;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.jim.mpviews.MpToolbar;
import com.jim.multipos.R;
import com.jim.multipos.core.BaseActivity;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.ui.customer_debt.view.CustomerDebtListFragment;
import com.jim.multipos.ui.customer_debt.view.CustomerListFragment;
import com.jim.multipos.utils.TestUtils;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Sirojiddin on 29.12.2017.
 */

public class CustomerDebtActivity extends BaseActivity implements CustomerDebtActivityView {

    @BindView(R.id.toolbar)
    MpToolbar toolbar;
    @Inject
    DatabaseManager databaseManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TestUtils.createCustomersWithDebt(databaseManager);
        setContentView(R.layout.customer_debt_layout);
        ButterKnife.bind(this);
        toolbar.setMode(MpToolbar.DEFAULT_MODE);
        addFragment(R.id.flLeftSide, new CustomerListFragment());
        addFragment(R.id.flRightSide, new CustomerDebtListFragment());

    }
}
