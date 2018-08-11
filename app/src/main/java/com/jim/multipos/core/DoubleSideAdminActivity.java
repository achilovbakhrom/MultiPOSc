package com.jim.multipos.core;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import com.jim.multipos.R;

import butterknife.ButterKnife;

public abstract class DoubleSideAdminActivity extends BaseActivity {

//    @BindView(R.id.company_container)
//    LinearLayout company_container;
//    @BindView(R.id.dashboard_container)
//    LinearLayout dashboard_container;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_double_admin_activity_layout);
        ButterKnife.bind(this);
    }

    protected final void openComapnyFragment(Fragment leftFragment, Fragment rightFragment) {
        findViewById(R.id.company_fr_container).setVisibility(View.VISIBLE);
        findViewById(R.id.dashboard_fr_container).setVisibility(View.GONE);
        addFragment(R.id.flLeftContainer, leftFragment);
        addFragment(R.id.flRightContainer, rightFragment);
    }

    protected final void openDashboardFragment(Fragment top, Fragment left, Fragment right) {
        findViewById(R.id.company_fr_container).setVisibility(View.GONE);
        findViewById(R.id.dashboard_fr_container).setVisibility(View.VISIBLE);
        addFragment(R.id.dashboard_topContainer, top);
        addFragment(R.id.dashboard_leftContainer, left);
        addFragment(R.id.dashboard_rightContainer, right);
    }

}
