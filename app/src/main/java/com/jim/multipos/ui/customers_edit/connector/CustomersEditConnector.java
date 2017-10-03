package com.jim.multipos.ui.customers_edit.connector;

import android.util.Log;

import com.jim.multipos.core.RxForPresenter;
import com.jim.multipos.data.db.model.customer.CustomerGroup;
import com.jim.multipos.ui.customers_edit.CustomersEditActivity;
import com.jim.multipos.utils.RxBus;
import com.jim.multipos.utils.RxBusLocal;
import com.jim.multipos.utils.rxevents.CustomerGroupEvent;

import java.util.ArrayList;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by user on 18.09.17.
 */

public abstract class CustomersEditConnector extends RxForPresenter {
    public static final String CUSTOMER_GROUP_ADDED = "customer_groups_added";

    public abstract void showCustomerGroups();

    private RxBus rxBus;
    private RxBusLocal rxBusLocal;

    @Override
    public void setRxListners() {
        ArrayList<Disposable> subscriptions = new ArrayList<>();
        subscriptions.add(rxBus.toObservable().subscribe(o -> {
            if (o instanceof CustomerGroupEvent) {
                CustomerGroupEvent customerGroupEvent = (CustomerGroupEvent) o;

                if (customerGroupEvent.getEventType().equals(CUSTOMER_GROUP_ADDED)) {
                    showCustomerGroups();
                }
            }
        }));

        initUnsubscribers(rxBus, CustomersEditActivity.class.getName(), subscriptions);
    }

    @Override
    public void initConnectors(RxBus rxBus, RxBusLocal rxBusLocal) {
        this.rxBus = rxBus;
        this.rxBusLocal = rxBusLocal;

        setRxListners();
    }
}
