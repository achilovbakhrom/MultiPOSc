package com.jim.multipos.ui.admin_main_page.fragments.product.presenter;

import com.jim.multipos.core.BasePresenterImpl;
import com.jim.multipos.ui.admin_main_page.fragments.product.model.Product;
import com.jim.multipos.utils.RxBus;
import com.jim.multipos.utils.rxevents.admin_main_page.AddMode;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class ProductPresenterImpl extends BasePresenterImpl<ProductView> implements ProductPresenter{

    ProductView view;

    @Inject
    RxBus bus;

    CompositeDisposable disposable = new CompositeDisposable();

    @Inject
    protected ProductPresenterImpl(ProductView productView) {
        super(productView);
        view = productView;
    }

    @Override
    public void startObserving() {
        disposable.add(bus.toObservable().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(o -> {
                    if(o instanceof Product)
                        view.setUpEditor((Product) o);
                    if(o instanceof AddMode)
                        view.onAddMode();
                }));
    }

    @Override
    public void onDestroy() {
        disposable.dispose();
        super.onDestroy();
    }
}
