package com.jim.multipos.ui.admin_auth_signup;

import com.jim.multipos.core.BasePresenterImpl;
import com.jim.multipos.data.network.MultiPosApiService;
import com.jim.multipos.data.network.model.Signup;
import com.jim.multipos.data.network.model.SignupConfirmationResponse;
import com.jim.multipos.data.network.model.SignupResponse;

import javax.inject.Inject;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class AdminAuthSignupActivityPresenterImpl extends BasePresenterImpl<AdminAuthSignupActivityView> implements AdminAuthSignupActivityPresenter {

    AdminAuthSignupActivityView view;

    @Inject
    protected AdminAuthSignupActivityPresenterImpl(AdminAuthSignupActivityView adminAuthActivityView) {
        super(adminAuthActivityView);
        view = adminAuthActivityView;
    }

}
