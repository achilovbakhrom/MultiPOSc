package com.jim.multipos.ui.admin_auth_signin;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.jim.multipos.R;
import com.jim.multipos.core.BaseActivity;

import javax.inject.Inject;

import butterknife.ButterKnife;

public class AdminAuthSigninActivity extends BaseActivity implements AdminAuthSigninActivityView{

    @Inject
    AdminAuthSigninActivityPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_signin_layout);

        ButterKnife.bind(this);
    }
}
