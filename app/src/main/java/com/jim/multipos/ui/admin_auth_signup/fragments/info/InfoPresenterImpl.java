package com.jim.multipos.ui.admin_auth_signup.fragments.info;

import com.jim.multipos.R;
import com.jim.multipos.core.BasePresenterImpl;
import com.jim.multipos.data.network.MultiPosApiService;
import com.jim.multipos.data.network.model.Signup;
import com.jim.multipos.utils.Utils;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class InfoPresenterImpl extends BasePresenterImpl<InfoView> implements InfoPresenter {

    InfoView view;

    @Inject
    MultiPosApiService service;

    CompositeDisposable disposable = new CompositeDisposable();

    @Inject
    protected InfoPresenterImpl(InfoView infoView) {
        super(infoView);
        view = infoView;
    }

    @Override
    public void sendUserDetails(String email, String password, String fn, String ln, String gender, String dob, String country, String primary_email, String primary_phone) {
        if(!Utils.isEmailValid(primary_email))
            view.onError(R.string.incorrect_email);
        else
            disposable.add(service.signUp(new Signup(email, password, fn, ln, gender, primary_phone, primary_email, country, dob))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(responseData->{
                    if (responseData.code == 200)
                        view.onSuccess(responseData.getData().getMail());
                }, throwable -> {
                    view.onError(throwable.getMessage());
                }));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        disposable.dispose();
    }
}
