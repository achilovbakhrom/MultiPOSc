package com.jim.multipos.ui.product.presenter;

import com.jim.multipos.core.RxForPresenter;
import com.jim.multipos.data.db.model.products.Product;
import com.jim.multipos.ui.product.ProductsActivity;
import com.jim.multipos.utils.RxBus;
import com.jim.multipos.utils.RxBusLocal;
import com.jim.multipos.utils.rxevents.ProductEvent;

import java.util.ArrayList;

import io.reactivex.disposables.Disposable;

/**
 * Created by DEV on 07.09.2017.
 */

public abstract class AdvancedOptionConnector extends RxForPresenter {
    private final static String OPEN_ADVANCE = "open_advance";

    abstract void addProduct(Product product);

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
                rxBusLocal.toObservable().subscribe(o -> {
                    if (o instanceof ProductEvent) {
                        ProductEvent productEvent = (ProductEvent) o;
                        if (productEvent.getEventType().equals(OPEN_ADVANCE)) {
                            addProduct(productEvent.getProduct());
                        }
                    }
                }));

        initUnsubscribers(rxBus, ProductsActivity.class.getName(), subscriptions);
    }

}
