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
    MultiPosApiService service;

    @Inject
    protected AdminAuthSignupActivityPresenterImpl(AdminAuthSignupActivityView adminAuthActivityView) {
        super(adminAuthActivityView);
        view = adminAuthActivityView;
    }

    @Override
    public void sendAdminDetails(Signup signup) {
        service.signUp(signup)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<SignupResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(SignupResponse value) {
                        view.onSuccess(value);
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.onError(e.getMessage());
                    }
                });
    }

    @Override
    public void confirm(String mail, int code) {
        service.confirmEmail(mail, code)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<SignupConfirmationResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(SignupConfirmationResponse value) {
                        if (value.isData())
                            view.onConfirmationSuccess();
                        else
                            view.onConfirmationIncorrectCode();
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.onConfirmationError(e.getMessage());
                    }
                });
    }
}
