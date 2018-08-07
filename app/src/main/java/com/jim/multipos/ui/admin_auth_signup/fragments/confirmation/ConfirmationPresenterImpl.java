package com.jim.multipos.ui.admin_auth_signup.fragments.confirmation;

import com.jim.multipos.core.BasePresenterImpl;
import com.jim.multipos.data.network.MultiPosApiService;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class ConfirmationPresenterImpl extends BasePresenterImpl<ConfirmationView> implements ConfirmationPresenter {

    ConfirmationView view;

    @Inject
    MultiPosApiService service;

    CompositeDisposable disposable = new CompositeDisposable();

    @Inject
    protected ConfirmationPresenterImpl(ConfirmationView confirmationView) {
        super(confirmationView);
        view = confirmationView;
    }

    @Override
    public void onConfirmation(String email, int code) {
        disposable.add(service.confirmEmail(email, code).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    switch (response.getCode()) {
                        case 200:
                            view.onSuccess();
                            break;
                        case 400:
                            view.onError("Email Error");
                            break;
                    }
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
