package com.jim.multipos.ui.customer_group.connector;

import com.jim.multipos.core.RxForPresenter;
import com.jim.multipos.utils.RxBus;
import com.jim.multipos.utils.RxBusLocal;
import com.jim.multipos.utils.rxevents.CustomerGroupEvent;

import java.util.ArrayList;

import io.reactivex.disposables.Disposable;

/**
 * Created by user on 08.09.17.
 */

public abstract class CustomerGroupListConnector extends RxForPresenter {
    public static final String CUSTOMER_GROUP_ADDED = "customer_group_added";

    public abstract void customerGroupAdded();

    private RxBus rxBus;
    private RxBusLocal rxBusLocal;

    @Override
    public void setRxListners() {
        ArrayList<Disposable> subscriptions = new ArrayList<>();

        subscriptions.add(rxBusLocal.toObservable().subscribe(o -> {
            if (o instanceof CustomerGroupEvent) {
                CustomerGroupEvent customerGroupEvent = (CustomerGroupEvent) o;

                if (customerGroupEvent.getEventType().equals(CUSTOMER_GROUP_ADDED)) {
                    customerGroupAdded();
                }
            }
        }));
    }

    @Override
    public void initConnectors(RxBus rxBus, RxBusLocal rxBusLocal) {
        this.rxBus = rxBus;
        this.rxBusLocal = rxBusLocal;

        setRxListners();
    }
}
