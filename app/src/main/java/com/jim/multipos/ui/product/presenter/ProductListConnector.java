package com.jim.multipos.ui.product.presenter;

import com.jim.multipos.core.RxForPresenter;
import com.jim.multipos.ui.product.ProductsActivity;
import com.jim.multipos.utils.RxBus;
import com.jim.multipos.utils.RxBusLocal;
import com.jim.multipos.utils.rxevents.CategoryEvent;
import com.jim.multipos.utils.rxevents.MessageEvent;
import com.jim.multipos.utils.rxevents.ProductEvent;
import com.jim.multipos.utils.rxevents.SubCategoryEvent;

import java.util.ArrayList;

import io.reactivex.disposables.Disposable;

/**
 * Created by DEV on 08.09.2017.
 */

public abstract class ProductListConnector extends RxForPresenter {
    private final static String ADD = "added";
    private final static String UPDATE = "update";
    private final static String SUBCAT_OPENED = "subcategory";
    private final static String PRODUCT_OPENED = "product";
    private RxBus rxBus;
    private RxBusLocal rxBusLocal;

    @Override
    public void initConnectors(RxBus rxBus, RxBusLocal rxBusLocal) {
        this.rxBus = rxBus;
        this.rxBusLocal = rxBusLocal;
        setRxListners();
    }

    @Override
    public void setRxListners() {
        ArrayList<Disposable> subscriptions = new ArrayList<>();
        subscriptions.add(
                rxBus.toObservable().subscribe(o -> {
                    if (o instanceof CategoryEvent) {
                        CategoryEvent event = (CategoryEvent) o;
                        if (event.getEventType().equals(ADD)) {
                            refreshCategoryList();
                        }
                        if (event.getEventType().equals(UPDATE)) {
                            refreshCategoryList();
                        }
                    }
                    if (o instanceof SubCategoryEvent) {
                        SubCategoryEvent event = (SubCategoryEvent) o;
                        /*if (event.getEventType().equals(ADD)) {
                            refreshSubCategoryList();
                        }
                        if (event.getEventType().equals(UPDATE)) {
                            refreshSubCategoryList();
                        }*/
                    }
                    if (o instanceof ProductEvent) {
                        ProductEvent event = (ProductEvent) o;
                        if (event.getEventType().equals(ADD)) {
                            refreshProductList();
                        }
                        if (event.getEventType().equals(UPDATE)) {
                            refreshProductList();
                        }
                    }
                }));
        subscriptions.add(
                rxBusLocal.toObservable().subscribe(o -> {
                    if (o instanceof MessageEvent) {
                        MessageEvent event = (MessageEvent) o;
                        if (event.getMessage().equals(SUBCAT_OPENED)) {
                            subCatFragmentOpened();
                        }
                    }
                    if (o instanceof MessageEvent) {
                        MessageEvent event = (MessageEvent) o;
                        if (event.getMessage().equals(PRODUCT_OPENED)) {
                            productFragmentOpened();
                        }
                    }
                }));

        initUnsubscribers(rxBus, ProductsActivity.class.getName(), subscriptions);
    }

    protected abstract void productFragmentOpened();

    protected abstract void subCatFragmentOpened();

    protected abstract void refreshProductList();

    protected abstract void refreshSubCategoryList();

    protected abstract void refreshCategoryList();
}
