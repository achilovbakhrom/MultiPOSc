package com.jim.multipos.ui.admin_main_page.fragments.establishment.presenter;

import com.jim.multipos.core.BasePresenterImpl;
import com.jim.multipos.ui.admin_main_page.fragments.establishment.model.Establishment;
import com.jim.multipos.ui.admin_main_page.fragments.establishment.model.EstablishmentPos;
import com.jim.multipos.ui.products_expired.ExpiredProductsView;
import com.jim.multipos.utils.RxBus;
import com.jim.multipos.utils.rxevents.admin_main_page.AddMode;
import com.jim.multipos.utils.rxevents.admin_main_page.CompanyEditor;
import com.jim.multipos.utils.rxevents.admin_main_page.CompanyEvent;
import com.jim.multipos.utils.rxevents.admin_main_page.ItemEditClick;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class EstablishmentFragmentPresenterImpl extends BasePresenterImpl<EstablishmentFragmentView> implements EstablishmentFragmentPresenter{

    @Inject
    RxBus bus;

    CompositeDisposable disposable = new CompositeDisposable();
    EstablishmentFragmentView view;

    @Inject
    protected EstablishmentFragmentPresenterImpl(EstablishmentFragmentView establishmentFragmentView) {
        super(establishmentFragmentView);
        view = establishmentFragmentView;
    }

    @Override
    public void startObserving() {
        disposable.add(bus.toObservable().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(o -> {
                    if (o instanceof Establishment) {
                        view.setUpEditor((Establishment) o);
                    }
                    if (o instanceof AddMode){
                        if(((AddMode) o).getMode() == 1)
                            view.onAddMode(1);
                        else if(((AddMode) o).getMode() == 2)
                            view.onAddMode(2);
                    }
                    if (o instanceof EstablishmentPos){
                        view.setUpPosEditor((EstablishmentPos) o);
                    }
                }));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        disposable.dispose();
    }
}
