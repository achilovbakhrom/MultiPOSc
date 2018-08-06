package com.jim.multipos.ui.admin_auth_signin;

import com.jim.multipos.core.BasePresenterImpl;
import com.jim.multipos.data.network.MultiPosApiService;
import com.jim.multipos.data.network.model.Signin;
import com.jim.multipos.data.network.model.SigninResponse;

import javax.inject.Inject;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class AdminAuthSigninActivityPresenterImpl extends BasePresenterImpl<AdminAuthSigninActivityView> implements AdminAuthSigninActivityPresenter{

    AdminAuthSigninActivityView view;

    @Inject
    MultiPosApiService service;

    @Inject
    protected AdminAuthSigninActivityPresenterImpl(AdminAuthSigninActivityView adminAuthSigninActivityView) {
        super(adminAuthSigninActivityView);
        view = adminAuthSigninActivityView;
    }

    @Override
    public void onSignUp(Signin signin) {
        service.signIn(signin).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<SigninResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(SigninResponse value) {
                        view.onSignInSucces(value);
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.onSignInError(e.getMessage());
                    }
                });
    }
}
