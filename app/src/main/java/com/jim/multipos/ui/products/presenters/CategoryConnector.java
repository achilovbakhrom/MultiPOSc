package com.jim.multipos.ui.products.presenters;

import com.jim.multipos.core.RxForPresenter;
import com.jim.multipos.data.db.model.products.Category;
import com.jim.multipos.ui.products.ProductsActivity;
import com.jim.multipos.utils.RxBus;
import com.jim.multipos.utils.RxBusLocal;
import com.jim.multipos.utils.rxevents.CategoryEvent;
import com.jim.multipos.utils.rxevents.MessageEvent;

import java.util.ArrayList;

import io.reactivex.disposables.Disposable;

/**
 * Created by DEV on 08.09.2017.
 */

public abstract class CategoryConnector extends RxForPresenter {
    private final static String CLICK = "click";
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
                    if (o instanceof CategoryEvent) {
                        CategoryEvent event = (CategoryEvent) o;
                        if (event.getEventType().equals(CLICK)) {
                            clickedCategory(event.getCategory());
                        }
                    }
                    if (o instanceof MessageEvent){
                        MessageEvent event = (MessageEvent) o;
                        if (event.getCategory().equals("category")){
                            isVisible();
                        }
                    }
                }));

        initUnsubscribers(rxBus, ProductsActivity.class.getName(), subscriptions);
    }

    abstract void isVisible();
    abstract void clickedCategory(Category category);
}
