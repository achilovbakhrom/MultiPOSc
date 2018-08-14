package com.jim.multipos.ui.admin_main_page;

import com.jim.multipos.core.BasePresenterImpl;
import com.jim.multipos.utils.RxBus;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class AdminMainPagePresenterImpl extends BasePresenterImpl<AdminMainPageView> implements AdminMainPagePresenter {

    AdminMainPageView view;

    @Inject
    RxBus bus;

    CompositeDisposable disposable = new CompositeDisposable();

    @Inject
    protected AdminMainPagePresenterImpl(AdminMainPageView adminMainPageView) {
        super(adminMainPageView);
        view = adminMainPageView;
    }

    @Override
    public void startObserving() {
        disposable.add(bus.toObservable().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(o -> {
                    view.onEvent(o);
                }));
    }

    @Override
    public void clearDisposable() {
        disposable.dispose();
    }
}
