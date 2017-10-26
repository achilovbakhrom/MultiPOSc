package com.jim.multipos.ui.customer_group.connector;

import com.jim.multipos.core.RxForPresenter;
import com.jim.multipos.data.db.model.customer.CustomerGroup;
import com.jim.multipos.ui.customer_group.CustomerGroupActivity;
import com.jim.multipos.utils.RxBus;
import com.jim.multipos.utils.RxBusLocal;
import com.jim.multipos.utils.rxevents.CustomerGroupEvent;

import java.util.ArrayList;

import io.reactivex.disposables.Disposable;

/**
 * Created by user on 09.09.17.
 */

public abstract class CustomerGroupConnector extends RxForPresenter {
    public static final String SHOW_CUSTOMERS = "show_customers";
    public static final String CUSTOMER_GROUP_OPENED = "customer_group_opened";
    public static final String CUSTOMER_GROUP_ADDED = "customer_group_added";

    public abstract void showCustomers(CustomerGroup customerGroup);
    public abstract void customerGroupAdded(CustomerGroup customerGroup);

    private RxBus rxBus;
    private RxBusLocal rxBusLocal;

    @Override
    public void setRxListners() {
        ArrayList<Disposable> subscriptions = new ArrayList<>();
        subscriptions.add(rxBusLocal.toObservable().subscribe(o -> {
            if (o instanceof CustomerGroupEvent) {
                CustomerGroupEvent customerGroupEvent = (CustomerGroupEvent) o;
                if (customerGroupEvent.getEventType().equals(SHOW_CUSTOMERS)) {
                    showCustomers(customerGroupEvent.getCustomerGroup());
                } else if (customerGroupEvent.getEventType().equals(CUSTOMER_GROUP_ADDED)) {
                    customerGroupAdded(customerGroupEvent.getCustomerGroup());
                }
            }
        }));

        initUnsubscribers(rxBus, CustomerGroupActivity.class.getName(), subscriptions);
    }

    @Override
    public void initConnectors(RxBus rxBus, RxBusLocal rxBusLocal) {
        this.rxBus = rxBus;
        this.rxBusLocal = rxBusLocal;

        setRxListners();
    }
}
