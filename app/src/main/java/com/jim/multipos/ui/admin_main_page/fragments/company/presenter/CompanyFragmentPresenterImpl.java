package com.jim.multipos.ui.admin_main_page.fragments.company.presenter;

import android.view.MotionEvent;

import com.jim.multipos.core.BasePresenterImpl;
import com.jim.multipos.ui.admin_main_page.fragments.company.model.CompanyModel;
import com.jim.multipos.utils.RxBus;
import com.jim.multipos.utils.rxevents.admin_main_page.AddMode;
import com.jim.multipos.utils.rxevents.admin_main_page.CompanyEditor;
import com.jim.multipos.utils.rxevents.admin_main_page.CompanyEvent;
import com.jim.multipos.utils.rxevents.admin_main_page.ItemEditClick;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class CompanyFragmentPresenterImpl extends BasePresenterImpl<CompanyFragmentView> implements CompanyFragmentPresenter {

    @Inject
    RxBus bus;

    CompositeDisposable disposable = new CompositeDisposable();
    CompanyFragmentView view;

    @Inject
    protected CompanyFragmentPresenterImpl(CompanyFragmentView companyFragmentView) {
        super(companyFragmentView);
        view = companyFragmentView;
    }

    @Override
    public void startObserving() {
        disposable.add(bus.toObservable().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(o -> {
                    if (o instanceof CompanyModel) {
                        view.onCompanyEvent((CompanyModel) o);
                    }else if(o instanceof AddMode)
                        view.onAddMode();
                }));

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        disposable.dispose();
    }
}
