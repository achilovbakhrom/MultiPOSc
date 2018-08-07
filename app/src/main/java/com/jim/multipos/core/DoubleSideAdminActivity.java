package com.jim.multipos.core;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.jim.multipos.R;

import butterknife.ButterKnife;

public abstract class DoubleSideAdminActivity extends BaseActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_double_admin_activity_layout);
        ButterKnife.bind(this);
    }

    protected final void addFragmentToLeft(Fragment fragment) {
        addFragment(R.id.flLeftContainer, fragment);
    }

    protected final void addFragmentToRight(Fragment fragment) {
        addFragment(R.id.flRightContainer, fragment);
    }

    protected final void replaceFragmentToLeft(Fragment fragment) {
        replaceFragment(R.id.flLeftContainer, fragment);
    }

    protected final void replaceFragmentToRight(Fragment fragment) {
        replaceFragment(R.id.flRightContainer, fragment);
    }

}
