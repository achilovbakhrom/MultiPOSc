package com.jim.multipos.ui.admin_auth_signin;

import com.jim.multipos.core.BasePresenterImpl;

import javax.inject.Inject;

public class AdminAuthSigninActivityPresenterImpl extends BasePresenterImpl<AdminAuthSigninActivityView> implements AdminAuthSigninActivityPresenter{

    AdminAuthSigninActivityView view;

    @Inject
    protected AdminAuthSigninActivityPresenterImpl(AdminAuthSigninActivityView adminAuthSigninActivityView) {
        super(adminAuthSigninActivityView);
        view = adminAuthSigninActivityView;
    }
}
