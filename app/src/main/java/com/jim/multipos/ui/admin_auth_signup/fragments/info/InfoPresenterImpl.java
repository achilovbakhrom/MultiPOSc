package com.jim.multipos.ui.admin_auth_signup.fragments.info;

import com.jim.multipos.core.BasePresenterImpl;
import com.jim.multipos.data.network.MultiPosApiService;
import com.jim.multipos.data.network.model.Signup;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class InfoPresenterImpl extends BasePresenterImpl<InfoView> implements InfoPresenter {

    InfoView view;

    @Inject
    MultiPosApiService service;

    @Inject
    protected InfoPresenterImpl(InfoView infoView) {
        super(infoView);
        view = infoView;
    }

    @Override
    public void sendUserDetails(String email, String password, String fn, String ln, String gender, String dob, String country, String primary_email, String primary_phone) {
        getDisposable().add(service.signUp(new Signup(email, password, fn, ln, gender, primary_phone, primary_email, country, dob))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(responseData->{
                if (responseData.code == 200)
                    view.onSuccess();
            }, throwable -> {
                view.onError(throwable.getMessage());
            }));

    }

}
