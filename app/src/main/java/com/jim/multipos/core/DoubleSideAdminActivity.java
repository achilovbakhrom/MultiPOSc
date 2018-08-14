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

    protected final void openCompanyFragment(Fragment leftFragment, Fragment rightFragment) {
        findViewById(R.id.company_fr_container).setVisibility(View.VISIBLE);
        findViewById(R.id.dashboard_fr_container).setVisibility(View.GONE);
        replaceFragment(R.id.flLeftContainer, leftFragment);
        replaceFragment(R.id.flRightContainer, rightFragment);
    }

    protected final void openEditContainer(int id) {
        View v = findViewById(id);
        v.setVisibility(View.VISIBLE);
        v.animate().translationX(v.getWidth()).setDuration(500);
    }

    protected final void openDashboardFragment(Fragment top, Fragment left, Fragment right) {
        findViewById(R.id.company_fr_container).setVisibility(View.GONE);
        findViewById(R.id.dashboard_fr_container).setVisibility(View.VISIBLE);
        replaceFragment(R.id.dashboard_topContainer, top);
        replaceFragment(R.id.dashboard_leftContainer, left);
        replaceFragment(R.id.dashboard_rightContainer, right);
    }

    protected final void openEstablishmentFragment(Fragment left, Fragment right) {
        openCompanyFragment(left, right);
    }


}
